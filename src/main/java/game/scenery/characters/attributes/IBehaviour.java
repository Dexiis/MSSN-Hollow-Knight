package game.scenery.characters.attributes;

import game.scenery.characters.Entity;
import processing.core.PVector;

public interface IBehaviour {
    PVector getDesiredVelocity(Entity me);

    void setWeight(float weight);

    float getWeight();
}
