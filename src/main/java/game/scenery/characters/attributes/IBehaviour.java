package game.scenery.characters.attributes;

import game.scenery.characters.types.Enemy;
import processing.core.PVector;

public interface IBehaviour {
    PVector getDesiredVelocity(Enemy me);

    void setWeight(float weight);

    float getWeight();
}
