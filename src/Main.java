import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;

public class Main extends PApplet {

    float minimumLatitude, minimumLongitude, maximumLatitude, maximumLongitude;
    private ArrayList<City> cities;
    private MongoConnect.MongoPipe mongoPipe;
    int startYear;
    int endYear;
    float theta;
    PFont myFont;
    Camera mainCamera;

    {
        maximumLatitude = MIN_FLOAT;
        maximumLongitude = MIN_FLOAT;
        minimumLatitude = MAX_FLOAT;
        minimumLongitude = MAX_FLOAT;
        startYear = MAX_INT;
        endYear = MIN_INT;
        cities = new ArrayList<>();
        mongoPipe = new MongoConnect.MongoPipe(this);
        mainCamera = new Camera();
        theta = (float) -1.5;
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(1000, 700, P3D);
        pixelDensity(2);
    }

    public void setup() {
        background(0);
        frameRate(60);

        myFont = createFont("IBM Plex Mono", 13);

        mongoPipe.connect();
        cities = mongoPipe.run();

        for (City currentCity : cities) {
            currentCity.createTimeline();
            currentCity.generateKeyframes();
        }
    }

    public void draw() {
        // Camera control
        pushMatrix();
        translate(-mainCamera.position.x, -mainCamera.position.y, -mainCamera.position.z);
        if (keyPressed) {
            if (key == 'w') mainCamera.position.y -= 5;
            if (key == 's') mainCamera.position.y += 5;
            if (key == 'a') mainCamera.position.x -= 5;
            if (key == 'd') mainCamera.position.x += 5;
            if (key == 'z') mainCamera.position.z += 5;
            if (key == 'x') mainCamera.position.z -= 5;

        }
        mainCamera.draw();

        frame.setTitle("fps: " + frameRate);
        background(0);
        theta += 0.005;

        float currentPosition = map(mouseX, 200, width - 200, -1000, endYear);
        currentPosition = map(sin(theta), -1, 1, -100, endYear);

        for (City currentCity : cities) {
//            float x = map(
//                    currentCity.longitude,
//                    minimumLongitude,
//                    maximumLongitude,
//                    120,
//                    width - 120);
//            float y = map(currentCity.latitude,
//                    maximumLatitude,
//                    minimumLatitude,
//                    120,
//                    height - 120);
//            float currentValue = currentCity.basicTimeline.getValue(currentPosition);
//            float circleDiameter = map(currentValue,
//                    0,
//                    500000,
//                    2,
//                    20);
//            if (currentCity.country.equals("Mexico")) {
//                fill(0, 0);
//                stroke(255, 0, 0);
//            } else {
//                fill(0, 0);
//                stroke(255);
//            }
//

            currentCity.show(currentPosition);

        }
        popMatrix();

        fill(255, 255);
        textFont(myFont);
        text("Current year: " + round(currentPosition), 40, 40);
    }

    public void keyPressed() {

    }

}
