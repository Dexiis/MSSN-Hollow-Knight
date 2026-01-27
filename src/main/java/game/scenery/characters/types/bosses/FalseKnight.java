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
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class FalseKnight extends Enemy implements IVisualizable {
    private static final float WALK_SPEED = 50f;
    public static final float ATTACK_DURANTION = 1000f;
    private Attack attackBehaviour;

    private static final int SPRITE_SIZE = 150;
    private static final int SPRITE_COUNT = 8;
    private static final int PIXEL_CORRECTION = 10;
    private static final PImage[][] spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];
    private PImage sprite;
    private int spriteTime = 0;
    private int spriteIndex = 0;

    private STATE state;

    private enum STATE {
        IDLE, TURNING, STARTLED, ANTICIPATION, ATTACK, DEATH
    }

    public FalseKnight(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 3;

        this.attackBehaviour = new Attack(1);

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(WALK_SPEED);
        this.behaviours.add(new AgressiveSeek(1));
        this.behaviours.add(new Wander(1));
        this.behaviours.add(this.attackBehaviour);

        // Enche o array de sprites iterativamente
//        PImage sprites = p.loadImage("images/FalseKnightSprites.png");
//        for (int y = 0; y < SPRITE_COUNT; y++)
//            for (int x = 0; x < SPRITE_COUNT; x++)
//                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
//
//        this.sprite = spriteArray[0][0];
//        this.state = STATE.IDLE;
    }

    @Override
    protected void idling() {

    }

    @Override
    protected void turning() {

    }

    @Override
    protected void attacking() {

    }

    @Override
    protected void death() {

    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
    }
}