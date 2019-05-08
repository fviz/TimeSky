import processing.core.PVector;

class EventParticle {

    Main main;

    PVector origin;
    float lifespan;
    PVector acceleration;
    PVector opacity;
    boolean activated;
    int timesFired;

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
        main.noStroke();
        main.fill(255);
        main.ellipse(origin.x, origin.y, 4, 4);
        if (timesFired < 1) {
            main.fill(255, opacity.x);
            main.ellipse(origin.x, origin.y, 16, 16);
        }
    }

    void run() {
        if (activated) {
            update();
            show();
        }
    }

    void fire() {
        if (!activated) {
            activated = true;
        }
        run();
    }

}
