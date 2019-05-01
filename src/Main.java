import processing.core.PApplet;
import java.util.ArrayList;

public class Main extends PApplet {

    float minimumLatitude;
    float minimumLongitude;
    float maximumLatitude;
    float maximumLongitude;

    private ArrayList<City> cities = new ArrayList<>();
    private MongoConnect.MongoPipe mongoPipe;
    {
        mongoPipe = new MongoConnect.MongoPipe(this);
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {

        maximumLatitude = MIN_FLOAT;
        maximumLongitude = MIN_FLOAT;
        minimumLatitude = MAX_FLOAT;
        minimumLongitude = MAX_FLOAT;

        size(1000, 700);
        pixelDensity(2);
    }

    public void setup() {

        background(0);

        mongoPipe.connect();
        cities = mongoPipe.run();

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
