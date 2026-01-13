package game.characters.types;

import game.characters.Entity;
import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class Enemy extends Entity {
	
	protected Enemy(PVector position) {
		super(position);
		this.mass = 1; //Todo Escolher a massa
	}

	public abstract void display(PApplet p, SubPlot plt);
}
