package App.Characters.Attributes.Behaviours;

import App.Characters.*;
import App.Characters.Attributes.*;

import processing.core.PVector;

public class Align extends Behaviour {

	public Align(float weight) {
		super(weight);
	}

	public PVector getDesiredVelocity(Body me) {
		PVector desiredVelocity = me.getVelocity().copy();
		for (Body body : me.getEye().getFarSight())
			desiredVelocity.add(body.getVelocity());

		return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
	}

}
