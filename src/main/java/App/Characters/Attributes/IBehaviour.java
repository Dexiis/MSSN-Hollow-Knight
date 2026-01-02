package App.Characters.Attributes;

import App.Characters.Body;
import processing.core.PVector;

public interface IBehaviour {
	public PVector getDesiredVelocity(Body me);

	public void setWeight(float weight);

	public float getWeight();
}
