package game.characters.types;

import game.characters.hitbox.Hitbox;
import game.characters.hitbox.Point;
import game.core.SubPlot;
import game.characters.Entity;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class TheKnight extends Entity {
	public static final int INITIAL_LIFE = 7;

	public TheKnight(PVector posisiton, PVector velocity, float mass, float radius, int color) {
		super(posisiton);

		ArrayList<Point> points = new ArrayList<>();
		points.add(new Point(0, 0));
		points.add(new Point(20, 0));
		points.add(new Point(20, 20));
		points.add(new Point(0, 20)); //TODO Escolher o tamanho

		this.hitbox = new Hitbox(points);
		this.health = 1; //Todo Escolher a vida
		this.mass = 1; //Todo Escolher a massa
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		//TODO Associar Sprites
	}
}