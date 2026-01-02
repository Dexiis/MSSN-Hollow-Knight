package main.java.App.Characters.Attributes.Behaviours;

import main.java.App.Characters.*;
import main.java.App.Characters.Attributes.*;

import processing.core.PVector;

public class Cohesion extends Behaviour {

	public Cohesion(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Body me) {
		PVector target = me.getPosition().copy();
		for (Body body : me.getEye().getFarSight())
			target.add(body.getPosition());
		target.div(me.getEye().getFarSight().size() + 1);

		return PVector.sub(target, me.getPosition());
	}

}