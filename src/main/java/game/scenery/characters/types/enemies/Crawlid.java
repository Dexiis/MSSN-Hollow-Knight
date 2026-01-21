package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.attributes.DNA;
import game.scenery.characters.attributes.behaviours.Seek;
import game.scenery.characters.attributes.behaviours.Wander;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

public class Crawlid extends Enemy implements IVisualizable {
    private static final float SPEED = 75f;
    public static final float ATTACK_DURANTION = 2000f;

    public Crawlid(PVector position) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 100);
        this.mass = 3f;
        this.health = 6;

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(SPEED);
        this.behaviours.add( new Seek(1, true));
        this.behaviours.add( new Wander(1, true));
    }

    public HurtBox attack(Direction direction) {
        //TODO ATTACK
        //TODO Acho que não tem tbh
        return null;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
    }
}