package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

public class Aspids extends Enemy implements IVisualizable {
    private static final float SPEED = 50f;
    public static final float ATTACK_DURANTION = 2000f;

    public Aspids(PVector position) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 2;
    }

    public HurtBox attack(Direction direction) {
        //TODO ATTACK
        return null;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
    }
}