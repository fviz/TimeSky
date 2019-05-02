import java.time.Duration;
import java.util.ArrayList;

import static processing.core.PApplet.map;
import static processing.core.PConstants.MAX_INT;
import static processing.core.PConstants.MIN_INT;

public class Timeline {

    Duration mainDuration = Duration.ofMillis(1000);
    int startYear;
    int endYear;
    ArrayList<Keyframe> keyframes = new ArrayList<>();
    Keyframe spawn;
    Keyframe death;

    Timeline(int startYearInput, int endYearInput) {
        this.startYear = startYearInput;
        this.endYear = endYearInput;
    }

    void printDuration() {
        System.out.println(this.mainDuration);
    }

    void addKeyframe(int positionInput, String typeInput, int intValue) {
        float positionPercentage = map(positionInput, this.startYear, this.endYear, 0, 1);

        Keyframe newKeyframe = new Keyframe(positionInput, typeInput, intValue);
        keyframes.add(newKeyframe);
    }

    void addKeyframe(Keyframe keyframeInput) {
        Keyframe newKeyframe = keyframeInput;
        keyframes.add(newKeyframe);
    }

    void setSpawn(Keyframe newSpawn) {
        keyframes.add(newSpawn);
        spawn = newSpawn;
    }

    void setDeath(Keyframe newDeath) {
        keyframes.add(newDeath);
        death = newDeath;
    }

    float getValue(float positionToCheck) {
        Keyframe onKeyframe;
        Keyframe betweenStart;
        Keyframe betweenEnd;

        betweenStart = new Keyframe(MIN_INT, "update", 0);
        betweenEnd = death;

        for (Keyframe registeredKeyframe : keyframes) {
            if (positionToCheck == registeredKeyframe.position) {
                onKeyframe = registeredKeyframe;
                return onKeyframe.value;
            } else {
                if (positionToCheck > registeredKeyframe.position && registeredKeyframe.position > betweenStart.position) {
                    betweenStart = registeredKeyframe;
                    continue;
                }
                if (positionToCheck < registeredKeyframe.position && registeredKeyframe.position < betweenEnd.position) {
                    betweenEnd = registeredKeyframe;
                    continue;
                }
            }
        }


        float distance = betweenEnd.position - betweenStart.position;
        float difference = betweenEnd.value - betweenStart.value;
        float positionDistance = positionToCheck - betweenStart.position;
        float positionDifference = (difference * positionDistance) / distance;

        float returnValue = betweenStart.value + positionDifference;

        return returnValue;
    }

}
