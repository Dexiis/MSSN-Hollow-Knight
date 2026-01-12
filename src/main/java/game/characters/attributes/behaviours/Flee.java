package game.characters.attributes.behaviours;

import game.characters.attributes.*;

import game.characters.Entity;
import processing.core.PVector;

public class Flee extends Behaviour {

	public Flee(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Entity me) {
		Entity characterTarget = me.getEye().getTarget();
		PVector desiredVelocity = PVector.sub(me.getPosition(), characterTarget.getPosition());
		
		return desiredVelocity.setMag(me.getDNA().getMaxSpeed());
	}
}