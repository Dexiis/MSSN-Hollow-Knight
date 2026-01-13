package game.characters.types.bosses;

import game.characters.hitbox.Hitbox;
import game.characters.hitbox.Point;
import game.characters.types.*;
import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class GruzMother extends Boss {

	public GruzMother(PVector position) {
		super(position);

		ArrayList<Point> points = new ArrayList<>();
		points.add(new Point(0, 0));
		points.add(new Point(20, 0));
		points.add(new Point(20, 20));
		points.add(new Point(0, 20)); //TODO Escolher o tamanho

		this.hitbox = new Hitbox(points);
		this.health = 1; //Todo Escolher a vida
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		//TODO Associar Sprites
	}

}