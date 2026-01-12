package game.characters.attributes;

import game.characters.Entity;
import processing.core.PVector;

public interface IBehaviour {
	PVector getDesiredVelocity(Entity me);

	void setWeight(float weight);

	float getWeight();
}
