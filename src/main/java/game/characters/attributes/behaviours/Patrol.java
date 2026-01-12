package game.characters.attributes.behaviours;

import game.characters.attributes.*;

import java.util.ArrayList;

import game.characters.Entity;
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
	public PVector getDesiredVelocity(Entity me) {
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