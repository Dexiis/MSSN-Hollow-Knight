import game.Game;
import processing.core.PApplet;

public class MainMenu extends PApplet {

	private final String play = "Start Playing";

	public void settings() {
		size(360, 300);
	}

	public void setup() {
		textAlign(CENTER, CENTER);
		textSize(16);
		rectMode(CENTER);
	}

	public void draw() {
		background(200);

		fill(0);

		drawButton(width / 2, 80, 300, 40, play, 1);
	}

	void drawButton(float x, float y, float w, float h, String label, int id) {
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y - h / 2 && mouseY < y + h / 2)
			fill(150, 200, 255);
		else
			fill(180);

		rect(x, y, w, h, 5);

		fill(0);
		text(label, x, y);
	}

	public void mousePressed() {
		float x = width / 2;
		float w = 300;
		float h = 40;

		if (checkButton(80, x, w, h)) {
			PApplet.main(Game.class);
		}
	}

	private boolean checkButton(float buttonY, float x, float w, float h) {
		return mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > buttonY - h / 2 && mouseY < buttonY + h / 2;
	}

	public static void main(String[] args) {
		PApplet.main(Game.class.getName());
	}
}