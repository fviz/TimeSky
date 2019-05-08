
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import static processing.core.PConstants.MAX_INT;
import static processing.core.PConstants.MIN_INT;

class City {

    // Meta properties
    String name;
    String country;
    float latitude;
    float longitude;
    int startYear;
    int endYear;
    PVector position;

    // Functionality
    EventParticle spawnParticle;
    Timeline basicTimeline;
    ArrayList<Year> years;
    Main main;

    City(Main mainReference) {
        main = mainReference;
        years = new ArrayList<>();
    }

    void show(float positionInput) {
        if (this.basicTimeline.spawn.position < positionInput) {
            this.spawnParticle.fire(this.basicTimeline.getValue(positionInput));
        }
    }

    void addYears(int yearInput, int populationInput) {
        Year newYear = new Year(yearInput, populationInput);
        this.years.add(newYear);
    }

    void createTimeline() {
        this.basicTimeline = new Timeline(
                this.main.startYear,
                this.main.endYear
        );
    }

    void generateKeyframes() {
        Keyframe spawn;
        Keyframe death;
        ArrayList<Keyframe> toAdd = new ArrayList<>();
        spawn = new Keyframe(
                MAX_INT,
                "spawn",
                1000
        );
        death = new Keyframe(
                main.endYear,
                "death",
                1000
        );
        spawn.setName("Spawn");
        death.setName("Death");

        int lastYear = MIN_INT;

        for (Year year : years) {

            boolean add = true;

            Keyframe newKeyframe = new Keyframe(
                    year.year,
                    "update",
                    year.population);

            if (newKeyframe.position < spawn.position) {
                spawn.position = newKeyframe.position;
                spawn.value = newKeyframe.value;
                add = false;
            }

            if (newKeyframe.position > lastYear) {
                death.value = newKeyframe.value;
            }

            if (add) {
                toAdd.add(newKeyframe);
            }
        }
        basicTimeline.setSpawn(spawn);
        for (Keyframe keyframeToAdd : toAdd) {
            basicTimeline.addKeyframe(keyframeToAdd);
        }
        basicTimeline.setDeath(death);
    }

    void calculatePosition() {
        float x = PApplet.map(
                this.longitude,
                main.minimumLongitude,
                main.maximumLongitude,
                120,
                main.width - 120);
        float y = PApplet.map(
                this.latitude,
                main.maximumLatitude,
                main.minimumLatitude,
                120,
                main.height - 120);
        PApplet.println(this.latitude);
        position = new PVector(x, y);
    }

    void createSpawnParticle() {
        spawnParticle = new EventParticle(main, position);
    }

}
