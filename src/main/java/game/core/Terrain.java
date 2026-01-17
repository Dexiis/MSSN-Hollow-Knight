package game.core;

import game.characters.Entity;
import game.characters.types.TheKnight;
import game.hitbox.Hitbox;
import game.hitbox.LinePainter;
import game.hitbox.LineSegment;
import game.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Terrain extends Hitbox {
    private final static int PIXEL_CORRECTION = 1;
    private boolean log = false;

    public Terrain(ArrayList<PVector> points) {
        super(points);
    }

    public Terrain(PVector center, float width, float height) {
        super(new Point(center.x,center.y), width, height);
    }

    public Terrain(PVector center, float width, float height, boolean log) {
        super(new Point(center.x,center.y), width, height);
        this.log = log;
    }

    public void elaborateIntersects(Entity entity) {
        Hitbox otherHitbox = entity.getHitbox();

        boolean[] results = elaborateIntersects(otherHitbox);
        boolean intersected = results[0];
        if (intersected) {
            if (log) {
                System.out.println(results[0]);
                System.out.println(results[1]);
                System.out.println(results[2]);
                System.out.println(results[3]);
                System.out.println(results[4]);
            }
            boolean onBottom = results[1];
            boolean onRight = results[2];
            boolean onTop = results[3];
            boolean onLeft = results[4];

            float xDistance = Math.abs(otherHitbox.getPosition().x - this.getPosition().x);
            float yDistance = Math.abs(otherHitbox.getPosition().y - this.getPosition().y);

            //Anulações
            if (onTop && onBottom) onTop = onBottom = false;
            if (onRight && onLeft) onRight = onLeft = false;

            float xDifference = Math.abs((otherHitbox.getWidth() / 2) - (xDistance - (this.width / 2)));
            float yDifference = Math.abs(otherHitbox.getHeight() / 2 - (yDistance - this.height / 2));

            //Eixo vencedor Top - Right
            if (onTop && onRight) {
                if (xDifference > yDifference) onRight = false;
                else onTop = false;
            }

            //Eixo vencedor Top - Left
            if (onTop && onLeft) {
                if (xDifference > yDifference) onLeft = false;
                else onTop = false;
            }

            //Eixo vencedor Bottom - Right
            if (onBottom && onRight) {
                if (xDifference > yDifference) onRight = false;
                else onBottom = false;
            }

            //Eixo vencedor Bottom - Left
            if (onBottom && onLeft) {
                if (xDifference > yDifference) onLeft = false;
                else onBottom = false;
            }

            //Lógica em cima
            if (onTop) {
                entity.setVelocity(new PVector(entity.getVelocity().x, 0));
                entity.setPosition(new PVector(entity.getPosition().x, this.getPosition().y + this.height / 2 + entity.getHitbox().getHeight() / 2 - PIXEL_CORRECTION));
                if (entity instanceof TheKnight) ((TheKnight) entity).setIsGrounded(true);
            }

            //Lógica em baixo
            if (onBottom) {
                entity.setVelocity(new PVector(entity.getVelocity().x, 0));
                entity.setPosition(new PVector(entity.getPosition().x, this.getPosition().y - this.height / 2 - entity.getHitbox().getHeight() / 2 - PIXEL_CORRECTION));
            }

            //Lógica à direita
            if (onRight) {
                entity.setVelocity(new PVector(0, entity.getVelocity().y));
                entity.setPosition(new PVector(this.getPosition().x + this.width / 2 + entity.getHitbox().getWidth() / 2 + PIXEL_CORRECTION, entity.getPosition().y));
            }

            //Lógica à esquerda
            if (onLeft) {
                entity.setVelocity(new PVector(0, entity.getVelocity().y));
                entity.setPosition(new PVector(this.getPosition().x - this.width / 2 - entity.getHitbox().getWidth() / 2 - PIXEL_CORRECTION, entity.getPosition().y));
            }
        }
    }

    public boolean[] elaborateIntersects(Hitbox other) {
        boolean[] intersects = {false, false, false, false, false};
        if (!this.roughHitbox.isIntersecting(other.getRoughHitbox())) intersects[0] = true;
        else return intersects;

        for (int i = 0; i < this.lines.size(); i++) {
            LineSegment line = lines.get(i);
            for (LineSegment otherLine : other.getLines())
                if (line.intersects(otherLine)) intersects[i + 1] = true;
        }

        return intersects;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        //TODO SPRITES??
        super.display(p, painter, plt);
    }
}