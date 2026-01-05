package game.characters.attributes.behaviours;

import game.characters.attributes.*;

import game.characters.Entity;
import processing.core.PVector;

public class Wander extends Behaviour {

	public Wander(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Entity me) {

		float newPhiWander = me.getPhiWander();
		newPhiWander += 2 * (Math.random() - 0.5) * me.getDNA().getDeltaPhiWander();
		me.setPhiWander(newPhiWander);

		PVector center = me.getVelocity().copy();
		center.normalize().mult(me.getDNA().getDeltaTWander());
		center.add(me.getPosition());

		PVector targetDisplacement = new PVector(me.getDNA().getRadiusWander() * (float) Math.cos(newPhiWander),
				me.getDNA().getRadiusWander() * (float) Math.sin(newPhiWander));
		PVector targetPosition = PVector.add(center, targetDisplacement);

		PVector desiredVelocity = PVector.sub(targetPosition, me.getPosition());

		desiredVelocity.setMag(me.getDNA().getMaxSpeed());

		return desiredVelocity;
	}
}