package main.java.App.Characters;

import main.java.App.Characters.Attributes.*;
import main.java.App.Core.*;

import java.util.List;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public abstract class Body extends Movement {

	protected int color;
	protected float radius;
	protected float[] positions;
	private PShape shape;
	protected DNA dna;
	protected Eye eye;

	protected float phiWander;
	private double[] window;

	protected Body(PVector position, PVector velocity, float mass, float radius, int color) {
		super(position, velocity, mass);
		this.color = color;
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public PShape getShape() {
		return shape;
	}

	public void setShape(PShape shape) {
		this.shape = shape;
	}

	public void setShape(PApplet p, SubPlot plt, float radius, int color) {
		this.radius = radius;
		this.color = color;
		setShape(p, plt);
	}

	public void setShape(PApplet p, SubPlot plt) {
		float[] rr = plt.getVectorCoord(radius, radius);
		shape = p.createShape();
		shape.beginShape();
		shape.noStroke();
		shape.fill(color);
		shape.vertex(-rr[0], rr[0] / 2);
		shape.vertex(rr[0], 0);
		shape.vertex(-rr[0], -rr[0] / 2);
		shape.vertex(-rr[0] / 2, 0);
		shape.endShape(PConstants.CLOSE);
	}

	public void setEye(Eye eye) {
		this.eye = eye;
	}

	public Eye getEye() {
		return this.eye;
	}

	public void setPhiWander(float newPhiWander) {
		this.phiWander = newPhiWander;
	}

	public float getPhiWander() {
		return phiWander;
	}

	public DNA getDNA() {
		return dna;
	}

	public void applyBehaviour(Behaviour behaviour, float dt) {
		if (eye != null)
			eye.look();
		PVector vd = behaviour.getDesiredVelocity(this);
		move(dt, vd);
	}

	public void applyBehaviours(List<Behaviour> behaviours, float dt) {
		if (eye != null)
			eye.look();
		PVector vd = new PVector();
		float sumWeights = 0;
		for (Behaviour behaviour : behaviours)
			sumWeights += behaviour.getWeight();

		for (Behaviour behaviour : behaviours) {
			PVector vdd = behaviour.getDesiredVelocity(this);
			vdd.mult(behaviour.getWeight() / sumWeights);
			vd.add(vdd);
		}
		move(dt, vd);
	}

	public void move(float dt, PVector vd) {
		vd.normalize().mult(dna.getMaxSpeed());
		PVector fs = PVector.sub(vd, velocity);
		applyForce(fs.limit(dna.getMaxForce()));
		super.move(dt);

		if (position.x < window[0])
			position.x += window[1] - window[0];
		if (position.y < window[2])
			position.y += window[3] - window[2];
		if (position.x >= window[1])
			position.x -= window[1] - window[0];
		if (position.y >= window[3])
			position.y -= window[3] - window[2];
	}

	public abstract void display(PApplet p, SubPlot plt);
}