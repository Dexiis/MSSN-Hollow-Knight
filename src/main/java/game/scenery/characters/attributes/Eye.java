package game.scenery.characters.attributes;

import game.scenery.characters.Entity;
import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Eye {
    private List<Entity> allTrackingBodies = new ArrayList<>();
    private List<Entity> farSight = new ArrayList<>();
    private List<Entity> nearSight = new ArrayList<>();
    private final Entity me;
    protected Entity target;

    public Eye(Entity me, Entity target) {
        this.me = me;
        this.target = target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public void setAllTrackingBodies(List<Entity> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    public List<Entity> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    public void addTarget(Entity target) {
        this.allTrackingBodies.add(target);
    }

    public List<Entity> getFarSight() {
        return farSight;
    }

    public List<Entity> getNearSight() {
        return nearSight;
    }

    public void look() {
        farSight = new ArrayList<Entity>();
        nearSight = new ArrayList<Entity>();
        for (Entity character : allTrackingBodies) {
            if (farSight(character.getPosition())) farSight.add(character);
            if (nearSight(character.getPosition())) nearSight.add(character);
        }
    }

    private boolean farSight(PVector t) {
        return inSight(t, me.getDNA().visionDistance, me.getDNA().visionAngle);
    }

    private boolean nearSight(PVector t) {
        return inSight(t, me.getDNA().visionAttack, me.getDNA().visionAttackAngle);
    }

    private boolean inSight(PVector t, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(t, me.getPosition());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVelocity());
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
    }

    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.pushMatrix();

        float[] pp = plt.getPixelCoord(me.getPosition().x, me.getPosition().y);
        p.translate(pp[0], pp[1]);

        p.rotate(-me.getVelocity().heading());
        p.noFill();
        p.stroke(255, 0, 0);
        p.strokeWeight(3);

        float[] dd1 = plt.getDimInPixel(me.getDNA().visionDistance, me.getDNA().visionDistance);
        p.rotate(me.getDNA().visionAngle);
        p.line(0, 0, dd1[0], 0);
        p.rotate(-2 * me.getDNA().visionAngle);
        p.line(0, 0, dd1[0], 0);
        p.rotate(me.getDNA().visionAngle);
        p.arc(0, 0, 2 * dd1[0], 2 * dd1[0], -me.getDNA().visionAngle, me.getDNA().visionAngle);

        float[] dd2 = plt.getDimInPixel(me.getDNA().visionAttack, me.getDNA().visionAttack);
        p.stroke(255, 0, 255);
        if (me.getDNA().visionAttackAngle >= Math.PI) {
            p.circle(0, 0, 2 * dd2[0]);
        } else {
            p.rotate(me.getDNA().visionAttackAngle);
            p.line(0, 0, dd2[0], 0);
            p.rotate(-2 * me.getDNA().visionAttackAngle);
            p.line(0, 0, dd2[0], 0);
            p.rotate(me.getDNA().visionAttackAngle);
            p.arc(0, 0, 2 * dd2[0], 2 * dd2[0], -me.getDNA().visionAttackAngle, me.getDNA().visionAttackAngle);
        }
        p.popMatrix();
        p.popStyle();
    }
}
