package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.attributes.DNA;
import game.scenery.characters.attributes.behaviours.AgressiveSeek;
import game.scenery.characters.attributes.behaviours.Attack;
import game.scenery.characters.attributes.behaviours.Wander;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

public class HuskHornhead extends Enemy implements IVisualizable {
    private static final float SPEED = 75f;
    public static final float ATTACK_DURANTION = 2000f;

    public HuskHornhead(PVector position) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 100);
        this.mass = 2f;
        this.health = 6;

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(SPEED);
        this.behaviours.add(new AgressiveSeek(1));
        this.behaviours.add(new Wander(1));
        this.behaviours.add(new Attack(1));
    }

    public void attack(Direction direction) {
        //TODO ATTACK
        //TODO APONTA O CORNO PARA A FRENTE E COMEÇA A CORRER
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
    }
}