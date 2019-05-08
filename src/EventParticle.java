import processing.core.PVector;

class EventParticle {

    Main main;

    PVector origin;
    float lifespan;
    PVector acceleration;
    PVector opacity;
    boolean activated;
    int timesFired;

    float value;

    EventParticle(Main mainReference, PVector positionInput) {
        main = mainReference;
        origin = positionInput.copy();
        acceleration = new PVector((float) 1, 0);
        opacity = new PVector(255, 0);
        lifespan = 255;
        activated = false;
    }

    void update() {
        opacity.sub(acceleration);
        lifespan -= 1;
        if (lifespan <= 0) {
            activated = false;
            lifespan = 255;
            opacity = new PVector(255, 0);
            timesFired++;
        }
    }

    void show() {
        if (timesFired < 1) {
            main.fill(200, 80, 100, opacity.x);
            main.ellipse(origin.x, origin.y, 16, 16);
        }
        // TODO: Separate main body particle from spawn particle
        double diameter = (int) (main.map(this.value, 0, 5000000, 8, 32));
        double logResult = loga(diameter, 32);
        diameter = diameter * logResult;
        main.noStroke();
        main.fill(255);
        main.ellipse(origin.x, origin.y, (int) diameter, (int) diameter);

    }

    void run() {
        if (activated) {
            update();
            show();
        }
    }

    void fire(float populationInput) {
        if (!activated) {
            activated = true;
        }
        this.value = populationInput;
        run();
    }

    double loga(double a, double b) {
        return Math.log(a) / Math.log(b);
    }

}
