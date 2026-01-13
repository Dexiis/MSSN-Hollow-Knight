package game.characters.types;

import game.characters.Entity;
import game.characters.hitbox.Hitbox;
import game.characters.hitbox.LinePainter;
import game.characters.hitbox.Point;
import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;

public class TheKnight extends Entity {

	public TheKnight(PVector position) {
		super(position);

		ArrayList<Point> points = new ArrayList<>();
		points.add(new Point(0, 0));
		points.add(new Point(50, 0));
		points.add(new Point(50, 50));
		points.add(new Point(0, 50));

		this.hitbox = new Hitbox(points);
		this.mass = 10.0f;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		//TODO ADICIONAR SPRITES
	}

	public void draw(LinePainter painter) {
		hitbox.draw(painter);
	}
}