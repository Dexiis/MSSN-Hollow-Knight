package game.characters.types;

import game.core.SubPlot;
import game.characters.Entity;
import processing.core.PApplet;
import processing.core.PVector;

public class TheKnight extends Entity {
	public static final int INITIAL_LIFE = 100;;

	public TheKnight(PVector posisiton, PVector velocity, float mass, float radius, int color) {
		super(posisiton, velocity, mass, radius, color, INITIAL_LIFE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Implement run() so that is a separated thread
	}
}