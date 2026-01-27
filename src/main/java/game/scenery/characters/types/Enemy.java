package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import game.scenery.characters.attributes.DNA;
import game.scenery.characters.attributes.behaviours.Attack;
import game.scenery.characters.attributes.behaviours.Wander;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class Enemy extends Entity {

    protected ArrayList<Behaviour> behaviours = new ArrayList<>();

    protected enum STATE {
        IDLE, TURNING, STARTLED, ANTICIPATION, ATTACK, DEATH
    }

    protected static float IDLE_SPEED;
    protected static float ATTACK_SPEED;
    protected static float ATTACK_COOLDOWN;

    protected static int SPRITE_SIZE;
    protected static final int SPRITE_COUNT = 8;
    protected static int PIXEL_CORRECTION;
    protected PImage sprite;
    protected int spriteTime = 0;
    protected int spriteIndex = 0;

    protected STATE state;
    protected STATE latestState;
    protected Direction currentDirection;
    protected Direction latestDirection;
    protected int multValue = 1;

    protected final Wander wanderBehaviour;
    protected final Attack attackBehaviour;
    protected int attackTime;

    protected final PApplet p;

    protected Enemy(PVector position, PApplet p) {
        super(position);

        this.p = p;

        this.dna = new DNA(this);
        this.attackBehaviour = new Attack(1);
        this.wanderBehaviour = new Wander(1);

        this.state = STATE.IDLE;
    }

    protected abstract void idling();
    protected abstract void turning();
    protected void startled() {}
    protected void anticipating() {}
    protected abstract void attacking();
    protected abstract void death();

    protected void stateMachine() {
        switch (state) {
            case STATE.IDLE:
                idling();
                break;
            case STATE.TURNING:
                turning();
                break;
            case STATE.STARTLED:
                startled();
                break;
            case STATE.ANTICIPATION:
                anticipating();
                break;
            case STATE.ATTACK:
                attacking();
                break;
            case STATE.DEATH:
                death();
                break;
        }
        latestState = state;
    }

    protected void loadSpriteSheet(String filename, PApplet p, PImage[][] spriteArray) {
        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage(filename);
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
    }

    protected void directionChange() {
        currentDirection = this.getVelocity().x < 0 ? Direction.LEFT : Direction.RIGHT;
        if (currentDirection != latestDirection) state = STATE.TURNING;
        latestDirection = currentDirection;
    }

    protected void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }

    public ArrayList<Behaviour> getBehaviours() {
        return this.behaviours;
    }

}
