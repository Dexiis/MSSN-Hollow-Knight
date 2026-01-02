package App.Characters.Attributes.Behaviours;

import App.Characters.*;
import App.Characters.Attributes.*;

import processing.core.PVector;

public class Flee extends Behaviour {

	public Flee(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Body me) {
		Body bodyTarget = me.getEye().getTarget();
		PVector desiredVelocity = PVector.sub(me.getPosition(), bodyTarget.getPosition());
		
		return desiredVelocity.setMag(me.getDNA().getMaxSpeed());
	}
}