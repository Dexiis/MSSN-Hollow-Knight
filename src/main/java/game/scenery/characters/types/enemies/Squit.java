package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.Entity;
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
import processing.core.PImage;
import processing.core.PVector;

public class Squit extends Enemy implements IVisualizable {
    private static final float IDLE_SPEED = 150f;
    private static final float ATTACK_SPEED = 275f;
    public static final float ATTACK_COOLDOWN = 2000f;

    private static final int SPRITE_SIZE = 150;
    private static final int SPRITE_COUNT = 6;
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
    private float attackAngle;

    private enum STATE {
        IDLE, TURNING, STARTLED, ANTICIPATION, ATTACK, DEATH
    }

    public Squit(PVector position, PApplet p) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 3;
        this.entityType = TYPE.AIR;

        this.attackBehaviour = new Attack(1);

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(IDLE_SPEED);
        this.behaviours.add(new SafeSeek(1));
        this.behaviours.add(new Wander(1));
        this.behaviours.add(this.attackBehaviour);

        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("images/SquitSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
        this.state = STATE.IDLE;
    }

    public void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        setAttacking(state == STATE.ATTACK);
        this.getDNA().setMaxSpeed(IDLE_SPEED);
        if(isColliding()) state = STATE.IDLE;

        // Define a direção do Squit
        currentDirection = this.getVelocity().x < 0 ? Direction.LEFT : Direction.RIGHT;
        if (currentDirection != latestDirection) state = STATE.TURNING;
        latestDirection = currentDirection;

        if(isDying()) state = STATE.DEATH;
        if(state != latestState) resetAnimation(p.millis());

        switch (state) {
            case STATE.IDLE:
                if(!isAttacking() && attackBehaviour.checkBehaviour(this) && p.millis() - attackTime > ATTACK_COOLDOWN)
                    state = STATE.STARTLED;
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][0];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 2) spriteIndex = 0;
                }
                break;
            case STATE.TURNING:
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][5];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 0) {
                        multValue *= -1;
                        spriteIndex = 0;
                        state = STATE.IDLE;
                    }
                }
                break;
            case STATE.STARTLED:
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][1];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 3) {
                        spriteIndex = 0;
                        state = STATE.ANTICIPATION;
                    }
                }
                break;
            case STATE.ANTICIPATION:
                if (p.millis() - spriteTime > 80) {
                    this.sprite = spriteArray[spriteIndex][2];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 5) {
                        spriteIndex = 0;
                        state = STATE.ATTACK;

                        PVector targetVector = PVector.sub(this.getEye().getTarget().getPosition(), this.getPosition()).normalize();
                        attackAngle = targetVector.heading();
                    }
                }
                break;
            case STATE.ATTACK:
                this.getDNA().setMaxSpeed(ATTACK_SPEED);
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][3];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 2) {
                        spriteIndex = 0;
                    }
                }
                if(!attackBehaviour.checkBehaviour(this)) {
                    state = STATE.IDLE;
                    attackTime = p.millis();
                }
                break;
            case STATE.DEATH:
                if (p.millis() - spriteTime > 120) {
                    this.sprite = spriteArray[spriteIndex][4];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 2) {
                        setDead(true);
                        spriteIndex = 0;
                        state = STATE.IDLE;
                    }
                }
                break;
        }
        latestState = state;

        // Diminuir o tamanho da sprite
        float spriteScale = 0.7f;

        p.pushMatrix();

        p.translate(pp[0], pp[1]);

        // Roda o Squit durante o ataque em direção ao jogador
        if (state == STATE.ATTACK) {
            int multiplier = Math.abs(PApplet.degrees(attackAngle)) < 90 ? -1 : 1;

            p.scale(spriteScale, multiplier * spriteScale);
            p.rotate(-multiplier * attackAngle + PApplet.radians(225));
        } else {
            p.scale(multValue * spriteScale, spriteScale);
        }

        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f);

        p.popMatrix();

    }
}