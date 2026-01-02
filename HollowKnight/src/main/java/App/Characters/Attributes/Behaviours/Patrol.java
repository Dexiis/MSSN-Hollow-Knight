package main.java.App.Characters.Attributes.Behaviours;

import main.java.App.Characters.*;
import main.java.App.Characters.Attributes.*;

import java.util.ArrayList;
import processing.core.PVector;

public class Patrol extends Behaviour {

	private ArrayList<PVector> path;
	private int currentIndex = 0;

	public Patrol(float weight) {
		super(weight);
		defineDefaultPath();
	}

	public void definePath(ArrayList<PVector> newPath) {
		this.path = newPath;
		if (!path.isEmpty()) 
			this.currentIndex = 0;
	}

	private void defineDefaultPath() {
		//TODO
	}

	@Override
	public PVector getDesiredVelocity(Body me) {
		if (path.isEmpty())
			return new PVector(0, 0);

		PVector desiredVelocity = PVector.sub(path.get(currentIndex), me.getPosition());
		
		float distance = desiredVelocity.mag();
		float radius = me.getDNA().getRadiusArrive();

		if (distance < radius) {
			desiredVelocity.mult(distance / radius);
			currentIndex = (currentIndex + 1) % path.size();
		}
		
		return desiredVelocity;
	}
}