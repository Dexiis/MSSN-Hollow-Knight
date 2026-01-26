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
import processing.core.PImage;
import processing.core.PVector;

public class HuskHornhead extends Enemy implements IVisualizable {
    private static final float WALK_SPEED = 75f;
    private static final float ATTACK_SPEED = 300f;
    public static final float ATTACK_COOLDOWN = 2000f;

    private static final int SPRITE_SIZE = 150;
    private static final int SPRITE_COUNT = 8;
    private static final int PIXEL_CORRECTION = 10;
    private static final PImage[][] spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];
    private PImage sprite;
    private int spriteTime = 0;
    private int spriteIndex = 0;

    private STATE state;
    private STATE latestState;
    private Direction currentDirection;
    private Direction latestDirection;
    private int multValue = 1;

    private final Attack attackBehaviour;
    private int attackTime;

    private enum STATE {
        WALKING, TURNING, ANTICIPATION, ATTACK, DEATH
    }

    public HuskHornhead(PVector position, PApplet p) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 100);
        this.mass = 1f;
        this.health = 5;

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(WALK_SPEED);

        this.attackBehaviour = new Attack(1);
        this.behaviours.add(new AgressiveSeek(1));
        this.behaviours.add(new Wander(1));
        this.behaviours.add(attackBehaviour);

        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("img/HuskSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
        this.state = STATE.WALKING;
    }

    public void attack(Direction direction) {
        //TODO ATTACK
        //TODO APONTA O CORNO PARA A FRENTE E COMEÇA A CORRER
    }

    public void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        // Define a direção do Squit
        currentDirection = this.getVelocity().x < 0 ? Direction.LEFT : Direction.RIGHT;
        if (currentDirection != latestDirection) state = STATE.TURNING;
        latestDirection = currentDirection;

        setAttacking(state == STATE.ATTACK);
        this.getDNA().setMaxSpeed(WALK_SPEED);

        if(isDying()) state = STATE.DEATH;
        if(state != latestState) resetAnimation(p.millis());

        switch (state) {
            case STATE.WALKING:
                if(!isAttacking() && attackBehaviour.checkBehaviour(this) && p.millis() - attackTime > ATTACK_COOLDOWN) {
                    state = STATE.ANTICIPATION;
                    resetAnimation(p.millis());
                }
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][0];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 5) spriteIndex = 0;
                }
                break;
            case STATE.TURNING:
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][1];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 0) {
                        multValue *= -1;
                        spriteIndex = 0;
                        state = STATE.WALKING;
                    }
                }
                break;
            case STATE.ANTICIPATION:
                if (p.millis() - spriteTime > 80) {
                    this.sprite = spriteArray[spriteIndex][2];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 4) {
                        spriteIndex = 0;
                        state = STATE.ATTACK;
                    }
                }
                break;
            case STATE.ATTACK:
                this.getDNA().setMaxSpeed(ATTACK_SPEED);
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][3];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 3) {
                        spriteIndex = 0;
                    }
                }
                if(!attackBehaviour.checkBehaviour(this)) {
                    state = STATE.WALKING;
                    attackTime = p.millis();
                }
                break;
            case STATE.DEATH:
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][4];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 7) {
                        setDead(true);
                        spriteIndex = 0;
                        state = STATE.WALKING;
                    }
                }
                break;
        }
        latestState = state;

        // Diminuir o tamanho da sprite
        float spriteScale = 0.6f;

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * spriteScale, spriteScale);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f + PIXEL_CORRECTION);

        p.popMatrix();
    }
}