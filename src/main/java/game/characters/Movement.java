package game.characters;

import game.core.*;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Movement {

	protected PVector position;
	protected PVector velocity = new PVector(0,0);
	protected PVector acceleration = new PVector();
	protected float mass;
	private static final double G = 6.67e-11;

	protected Movement(PVector position) {
		this.position = position.copy();
	}

	public void applyForce(PVector force) {
		acceleration.add(PVector.div(force, mass));
	}

	public void move(float dt) {
		velocity.add(acceleration.mult(dt));
		position.add(PVector.mult(velocity, dt));
		acceleration.mult(0);
	}

	public PVector attraction(Movement body) {
		PVector direction = PVector.sub(this.position, body.position);
		float distance = direction.mag();

		float minDistance = 1e6f;
		distance = Math.max(distance, minDistance);

		float strength = (float) (G * this.mass * body.mass / Math.pow(distance, 2));

		return direction.normalize().mult(strength);
	}

	public PVector getPosition() {
		return position;
	}

	public PVector getVelocity() {
		return velocity;
	}

	public PVector getAcceleration() {
		return acceleration;
	}

	public float getMass() {
		return mass;
	}

	public void setAcceleration(PVector acceleration) {
		this.acceleration = acceleration;
	}

	public void setVelocity(PVector velocity) {
		this.velocity = velocity;
	}

	public void setPosition(PVector position) {
		this.position = position;
	}

	public void displayVelocityVector(PApplet p, SubPlot plt) {
		float arrowSize = 10;
		float arrowAngle = PApplet.radians(20);

		float[] pp = plt.getPixelCoord(position.x, position.y);
		float px = pp[0];
		float py = pp[1];

		float[] v = plt.getVectorCoord(velocity.x, -velocity.y);
		float vx = v[0];
		float vy = v[1];

		float endX = px + vx;
		float endY = py + vy;

		p.stroke(0, 255, 0);
		p.strokeWeight(2);

		p.line(px, py, endX, endY);

		PVector lineVector = new PVector(vx, vy);
		lineVector.normalize();
		lineVector.mult(arrowSize);

		PVector fin1 = lineVector.copy();
		fin1.rotate(arrowAngle);
		p.line(endX, endY, endX - fin1.x, endY - fin1.y);

		PVector fin2 = lineVector.copy();
		fin2.rotate(-arrowAngle);
		p.line(endX, endY, endX - fin2.x, endY - fin2.y);
	}

}