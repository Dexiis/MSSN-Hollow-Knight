package game.scenery.characters.attributes;

import game.scenery.characters.Entity;

public class DNA {

    protected float maxSpeed;
    protected float maxForce;
    protected float visionDistance;
    protected float visionAttack;
    protected float visionAngle;
    protected float deltaTWander;
    protected float radiusWander;
    protected float deltaPhiWander;
    protected float visionAttackAngle;

    public DNA(Entity me) { //TODO APLICAR ESTA CLASSE A CADA GAJO INDIVIDUALMENTE
        maxSpeed = random(100f, 200f);
        maxForce = random(150f, 250f);

        visionDistance = random(2000f, 3000f); //vou dar push para veres
        visionAttack = 0.20f * visionDistance;
        visionAngle = (float) Math.PI * 2f;
        visionAttackAngle = (float) Math.PI * 2f;

        deltaTWander = 2f;
        radiusWander = random(100f, 150f);

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
        return visionAttack;
    }

    public void setVisionNearDistance(float visionNearDistance) {
        this.visionAttack = visionNearDistance;
    }

    public float getVisionAngle() {
        return visionAngle;
    }

    public void setVisionAngle(float visionAngle) {
        this.visionAngle = visionAngle;
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
        return visionAttackAngle;
    }

    public void setVisionNearAngle(float visionNearAngle) {
        this.visionAttackAngle = visionNearAngle;
    }

    public static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }

}
