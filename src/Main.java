import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import processing.core.PApplet;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Map;
import java.util.ArrayList;

public class Main extends PApplet {

    MongoClient mongoClient=MongoClients.create();
    MongoDatabase database=mongoClient.getDatabase("cities");
    MongoCollection<Document> coll=database.getCollection("cities");

    ArrayList<City> cities = new ArrayList<City>();

    private float minimumLatitude;
    private float minimumLongitude;
    private float maximumLatitude;
    private float maximumLongitude;

    Block<Document> parseBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            City newCity = new City();

            // Read data from MongoDB
            newCity.name = document.getString("name");
            newCity.country = document.getString("country");
            String latitude = document.getString("latitude");
            String longitude = document.getString("longitude");
            Document years = (Document) document.get("years");

            // Cleaning the string from any non-printable characters to avoid Java complaints
            String cleanLatitude = latitude.replaceAll("\\P{Print}", "");
            String cleanLongitude = longitude.replaceAll("\\P{Print}", "");
            newCity.latitude = Float.parseFloat(cleanLatitude);
            newCity.longitude = Float.parseFloat(cleanLongitude);

            // Process years object
            for (Map.Entry<String, Object> entry : years.entrySet()) {

                String[] rawYear = entry.getKey().split("_");
                int population = Integer.parseInt((String) entry.getValue());
                String beforeAfter = rawYear[0];
                int year = Integer.parseInt(rawYear[1]);

                if (beforeAfter.equals("BC")) {
                    year = -year;
                }
                newCity.addYears(year, population);
            }

            maximumLatitude = max(maximumLatitude, newCity.latitude);
            maximumLongitude = max(maximumLongitude, newCity.longitude);
            minimumLatitude = min(minimumLatitude, newCity.latitude);
            minimumLongitude = min(minimumLongitude, newCity.longitude);

            cities.add(newCity);

        }
    };

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(1000, 700);
        pixelDensity(2);
    }

    public void setup() {
        background(0);

        maximumLatitude = MIN_FLOAT;
        maximumLongitude = MIN_FLOAT;
        minimumLatitude = MAX_FLOAT;
        minimumLongitude = MAX_FLOAT;

        coll.find().forEach(parseBlock);

        println(minimumLatitude, maximumLatitude);
        println(minimumLongitude, maximumLongitude);

        Keyframe newKeyframe = new Keyframe(2, "spawn");
    }

    public void draw() {
        // TODO: Timeline

        for (City currentCity : cities) {
            float x = map(currentCity.longitude, minimumLongitude, maximumLongitude, 120, width - 120);
            float y = map(currentCity.latitude, maximumLatitude, minimumLatitude, 120, height - 120);
            if (currentCity.country.equals("Brazil")) {
                stroke(255, 0, 0);
            } else {
                stroke(255);
            }
            point(x, y);
        }
    }

}
