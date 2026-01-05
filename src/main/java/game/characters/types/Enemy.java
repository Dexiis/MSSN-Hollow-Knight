package game.characters.types;

import game.core.SubPlot;

import game.characters.Entity;
import processing.core.PApplet;
import processing.core.PVector;

public class Enemy extends Entity {
	
	public Enemy(PVector position, PVector velocity, float mass, float radius, int color, int life) {
		super(position, velocity, mass, radius, color, life);
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {

	}
}
