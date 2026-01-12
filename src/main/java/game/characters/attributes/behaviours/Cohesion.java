package game.characters.attributes.behaviours;

import game.characters.attributes.*;

import game.characters.Entity;
import processing.core.PVector;

public class Cohesion extends Behaviour {

	public Cohesion(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Entity me) {
		PVector target = me.getPosition().copy();
		for (Entity character : me.getEye().getFarSight())
			target.add(character.getPosition());
		target.div(me.getEye().getFarSight().size() + 1);

		return PVector.sub(target, me.getPosition());
	}

}