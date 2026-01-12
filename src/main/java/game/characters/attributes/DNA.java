package game.characters.attributes;

import game.characters.Entity;

public class DNA {

	protected float maxSpeed;
	protected float maxForce;
	protected float visionDistance;
	protected float visionNearDistance;
	protected float visionAngle;
	protected float deltaTPursuit;
	protected float radiusArrive;
	protected float deltaTWander;
	protected float radiusWander;
	protected float deltaPhiWander;
	protected float visionNearAngle;

	public DNA(Entity boid) { //TODO ESTA CLASSE PROVAVELMENTE VAI DESAPARECER
			maxSpeed = random(3f, 5f);
			maxForce = random(7f, 10f);

			visionDistance = random(9f, 11f);
			visionNearDistance = 0.25f * visionDistance;
			visionAngle = (float) Math.PI * 0.2f;
			visionNearAngle = (float) Math.PI * 0.6f;

			deltaTPursuit = random(0.5f, 1f);

			radiusArrive = random(5, 10);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

			deltaPhiWander = (float) Math.PI / 8;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

	public float getVisionDistance() {
		return visionDistance;
	}

	public void setVisionDistance(float visionDistance) {
		this.visionDistance = visionDistance;
	}

	public float getVisionNearDistance() {
		return visionNearDistance;
	}

	public void setVisionNearDistance(float visionNearDistance) {
		this.visionNearDistance = visionNearDistance;
	}

	public float getVisionAngle() {
		return visionAngle;
	}

	public void setVisionAngle(float visionAngle) {
		this.visionAngle = visionAngle;
	}

	public float getDeltaTPursuit() {
		return deltaTPursuit;
	}

	public void setDeltaTPursuit(float deltaTPursuit) {
		this.deltaTPursuit = deltaTPursuit;
	}

	public float getRadiusArrive() {
		return radiusArrive;
	}

	public void setRadiusArrive(float radiusArrive) {
		this.radiusArrive = radiusArrive;
	}

	public float getDeltaTWander() {
		return deltaTWander;
	}

	public void setDeltaTWander(float deltaTWander) {
		this.deltaTWander = deltaTWander;
	}

	public float getRadiusWander() {
		return radiusWander;
	}

	public void setRadiusWander(float radiusWander) {
		this.radiusWander = radiusWander;
	}

	public float getDeltaPhiWander() {
		return deltaPhiWander;
	}

	public void setDeltaPhiWander(float deltaPhiWander) {
		this.deltaPhiWander = deltaPhiWander;
	}

	public float getVisionNearAngle() {
		return visionNearAngle;
	}

	public void setVisionNearAngle(float visionNearAngle) {
		this.visionNearAngle = visionNearAngle;
	}

	public static float random(float min, float max) {
		return (float) (min + (max - min) * Math.random());
	}

}
