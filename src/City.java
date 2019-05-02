import java.util.ArrayList;

import static processing.core.PConstants.MAX_INT;
import static processing.core.PConstants.MIN_INT;

public class City {

    String name;
    String country;
    float latitude;
    float longitude;
    ArrayList<Year> years;
    int startYear;
    int endYear;
    Main main;

    Timeline basicTimeline;

    City(Main mainReference) {
        this.main = mainReference;
        this.years = new ArrayList<>();
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

}
