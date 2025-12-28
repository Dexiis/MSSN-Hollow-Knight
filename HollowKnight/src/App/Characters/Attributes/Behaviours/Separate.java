package App.Characters.Attributes.Behaviours;

import App.Characters.*;
import App.Characters.Attributes.*;

import processing.core.PVector;

public class Separate extends Behaviour {

	public Separate(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Body me) {
		PVector desiredVelocity = new PVector();
		for (Body body : me.getEye().getNearSight()) {
			PVector direction = PVector.sub(me.getPosition(), body.getPosition());
			float dir = direction.mag();
			direction.div(dir * dir);
			desiredVelocity.add(direction);
		}
		
		return desiredVelocity;
	}

}