package game;

import game.core.*;

import processing.core.PApplet;

public class Game extends PApplet {

	private float lastUpdateTime;
	private final double[] window = { -16, 9, -9, 16 };
	private final float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;

	public void settings() {
		size(1600, 900);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		background(0);
	}

	public void draw() {

	}

	private void setWindow(double x1, double y1, double x2, double y2) {
		window[0] = x1;
		window[1] = y1;
		window[2] = x2;
		window[3] = y2;
		plt.setWindow(window);
	}

	public void keyPressed() {

	}

	public void mousePressed() {

	}

}