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

import java.util.ArrayList;
import java.util.Map;

import static processing.core.PApplet.max;
import static processing.core.PApplet.min;
import static processing.core.PConstants.MAX_INT;
import static processing.core.PConstants.MIN_INT;

public class MongoConnect {

    static class MongoPipe {

        MongoClient client;
        MongoDatabase database;
        MongoCollection<Document> collection;
        ArrayList<City> cities;
        private final Main main;

        MongoPipe(Main mainReference) {
            this.main = mainReference;
            this.cities = new ArrayList<>();
        }

        void connect() {
            this.client = MongoClients.create();
            this.database = getDatabase("cities");
            this.collection = getCollection("cities");
        }

        @SuppressWarnings("deprecation")
        ArrayList<City> run() {
            this.collection.find().forEach(parseCities);
            return this.cities;
        }

        MongoCollection<Document> getCollection(String collectionNameInput) {
            return database.getCollection(collectionNameInput);
        }

        MongoDatabase getDatabase(String databaseNameInput) {
            return client.getDatabase(databaseNameInput);
        }

        float parseCoordinate(String coordinateRaw) {
            String coordinateClean = coordinateRaw.replaceAll("\\P{Print}", "");
            float parsedValue = Float.parseFloat(coordinateClean);

            return parsedValue;
        }

        int[] parseYear(Map.Entry<String, Object> yearInput) {
            int[] returnData = new int[2];

            String[] rawYear = yearInput.getKey().split("_");
            int population = Integer.parseInt((String) yearInput.getValue());
            String beforeAfter = rawYear[0];
            int year = Integer.parseInt(rawYear[1]);

            if (beforeAfter.equals("BC")) {
                year = -year;
            }

            returnData[0] = year;
            returnData[1] = population;

            return returnData;
        }

        Block<Document> parseCities = new Block<Document>() {
            @Override public void apply(final Document document) {
                City newCity = new City(main);

                int startYearParse = MAX_INT;
                int endYearParse = MIN_INT;

                // Read basic data from MongoDB
                newCity.name = document.getString("name");
                newCity.country = document.getString("country");
                newCity.latitude = parseCoordinate(document.getString("latitude"));
                newCity.longitude = parseCoordinate(document.getString("longitude"));
                Document years = (Document) document.get("years");

                // Process years object
                for (Map.Entry<String, Object> entry : years.entrySet()) {

                    int[] parsedYearData = parseYear(entry);
                    newCity.addYears(parsedYearData[0], parsedYearData[1]);

                    startYearParse = min(startYearParse, parsedYearData[0]);
                    endYearParse = max(endYearParse, parsedYearData[0]);
                }

                newCity.startYear = startYearParse;
                newCity.endYear = endYearParse;

                main.startYear = min(main.startYear, newCity.startYear);
                main.endYear = max(main.endYear, newCity.endYear);

                main.minimumLatitude = min(main.minimumLatitude, newCity.latitude);
                main.minimumLongitude = min(main.minimumLongitude, newCity.longitude);
                main.maximumLatitude = max(main.maximumLatitude, newCity.latitude);
                main.maximumLongitude = max(main.maximumLongitude, newCity.longitude);
                newCity.calculatePosition();
                newCity.createSpawnParticle();
                cities.add(newCity);

            }

        };

    }

}
