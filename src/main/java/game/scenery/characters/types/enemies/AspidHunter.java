package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.attributes.DNA;
import game.scenery.characters.attributes.behaviours.Attack;
import game.scenery.characters.attributes.behaviours.SafeSeek;
import game.scenery.characters.attributes.behaviours.Wander;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

public class AspidHunter extends Enemy implements IVisualizable {
    private static final float SPEED = 75f;
    public static final float ATTACK_DURATION = 2000f;

    public AspidHunter(PVector position) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 3;

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(SPEED);
        this.behaviours.add(new SafeSeek(1));
        this.behaviours.add(new Wander(1));
        this.behaviours.add(new Attack(1));
    }

    public HurtBox attack(Direction direction) {
        //TODO ATTACK Cria gajos??
        return null;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
    }
}