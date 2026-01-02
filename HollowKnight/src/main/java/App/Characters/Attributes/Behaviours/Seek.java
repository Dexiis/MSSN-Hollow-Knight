package main.java.App.Characters.Attributes.Behaviours;

import main.java.App.Characters.*;
import main.java.App.Characters.Attributes.*;

import processing.core.PVector;

public class Seek extends Behaviour {

	public Seek(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Body me) {
		Body bodyTarget = me.getEye().getTarget();
		
		return PVector.sub(bodyTarget.getPosition(), me.getPosition());
	}

}