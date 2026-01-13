package game.characters.types;

import game.core.SubPlot;
import game.characters.Entity;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class Boss extends Entity {

	protected Boss(PVector posisiton) {
		super(posisiton);
		this.mass = 1; //Todo Escolher a massa
	}

	public abstract void display(PApplet p, SubPlot plt);
}