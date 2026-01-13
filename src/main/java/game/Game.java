package game;

import game.characters.hitbox.LineDrawer;
import game.characters.hitbox.Point;
import game.characters.types.TheKnight;
import game.scenery.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class Game extends PApplet {

	private Map map;
	private TheKnight player;
	private LineDrawer painter;

	public void settings() {
		size(1280, 720);
	}

	public void setup() {
		map = new Map();
		painter = new LineDrawer(this);
		player = map.getPlayer();
	}

	public void draw() {
		background(255);

		PVector gravity = new PVector(0, 980 * player.getMass());
		player.applyForce(gravity);

		player.move(1.0f / 60.0f);
		checkCollisions();

		map.draw(painter);
	}

	private void checkCollisions() {
		if (player.getHitbox().intersects(map.getGround())) {
			player.setVelocity(new PVector(player.getVelocity().x, 0));

			float groundY = map.getGround().getPosition().y;
			player.setPosition(new PVector(player.getPosition().x, groundY - 50));

			player.getHitbox().setPosition(new Point(player.getPosition().x, player.getPosition().y));
		}
	}

	public static void main(String[] args) {
		PApplet.main("game.Game");
	}
}