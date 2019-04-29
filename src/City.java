import java.util.ArrayList;

public class City {

    String name;
    String country;
    float latitude;
    float longitude;
    ArrayList<Year> years = new ArrayList<Year>();

    void addYears(int yearInput, int populationInput) {
        Year newYear = new Year(yearInput, populationInput);
        this.years.add(newYear);
    }

}
