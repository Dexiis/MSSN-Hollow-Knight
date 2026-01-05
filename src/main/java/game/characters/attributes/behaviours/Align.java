package game.characters.attributes.behaviours;

import game.characters.attributes.*;

import game.characters.Entity;
import processing.core.PVector;

public class Align extends Behaviour {

	public Align(float weight) {
		super(weight);
	}

	public PVector getDesiredVelocity(Entity me) {
		PVector desiredVelocity = me.getVelocity().copy();
		for (Entity character : me.getEye().getFarSight())
			desiredVelocity.add(character.getVelocity());

		return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
	}

}
