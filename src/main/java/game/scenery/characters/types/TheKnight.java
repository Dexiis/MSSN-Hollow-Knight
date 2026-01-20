package game.scenery.characters.types;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.IVisualizable;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.HashMap;
import java.util.Map;

public class TheKnight extends Entity implements IVisualizable {
    private static final float JUMP_STRENGTH = 1000f;
    private static final float SPEED = 275f;
    public static final float ATTACK_DURATION = 100f;
    public static final float ATTACK_COOLDOWN = 500f;
    private static final int PIXEL_CORRECTION = 6;

    private final Map<Direction, Boolean> directions;
    private Direction lastFacingDirection; // LEFT or RIGHT

    private HurtBox attack = null;
    private float attackTime = 0f;

    private boolean isGrounded = false;

    public static final int SPRITE_SIZE = 80;
    private static final int SPRITE_COUNT = 12;
    private static final PImage[][] spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];
    private PImage sprite;
    private int spriteTime = 0;
    private int spriteIndex = 0;

    private MovementState movement;
    private MovementState lastMovement;

    public TheKnight(PVector position, PApplet p) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 30, 80);
        this.mass = 1f;
        this.health = 10;

        this.directions = new HashMap<>();
        this.directions.put(Direction.UP, false);
        this.directions.put(Direction.DOWN, false);
        this.directions.put(Direction.RIGHT, false);
        this.directions.put(Direction.LEFT, false);
        this.directions.put(Direction.UPRELEASED, false);

        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("img/TheKnightSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++) {
            for (int x = 0; x < SPRITE_COUNT; x++) {
                spriteArray[x][y] = sprites.get(x * TheKnight.SPRITE_SIZE, y * TheKnight.SPRITE_SIZE, TheKnight.SPRITE_SIZE, TheKnight.SPRITE_SIZE);
            }
        }

        this.sprite = spriteArray[0][0];
        movement = MovementState.IDLE;
        lastMovement = movement;
    }

    public Direction getLastFacingDirection() {
        return lastFacingDirection;
    }

    public void setLastFacingDirection(Direction lastFacingDirection) {
        this.lastFacingDirection = lastFacingDirection;
    }

    public boolean getIsGrounded() {
        return isGrounded;
    }

    public void setIsGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public Map<Direction, Boolean> getDirections() {
        return directions;
    }

    public void setMovingDirection(Direction direction, boolean aux) {
        directions.put(direction, aux);
    }

    public HurtBox getAttack() {
        return attack;
    }

    public void setAttack(HurtBox attack) {
        this.attack = attack;
    }

    public float getAttackTime() {
        return attackTime;
    }

    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }

    public MovementState getMovement() {
        return this.movement;
    }

    public void setLastMovement(MovementState lastMovement) {
        this.lastMovement = lastMovement;
    }

    public MovementState getLastMovement() {
        return this.lastMovement;
    }

    public void setMovement(MovementState movement) {
        this.movement = movement;
    }

    public void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }

    //TODO RETORNAR PVECTOR
    public void jump() {
        this.setVelocity(new PVector(this.getVelocity().x, JUMP_STRENGTH));
    }

    //TODO RETORNAR PVECTOR
    public void moveRight() {
        setVelocity(new PVector(Math.min(SPEED, getVelocity().x + SPEED), getVelocity().y));
    }

    //TODO RETORNAR PVECTOR
    public void moveLeft() {
        setVelocity(new PVector(Math.max(-SPEED, getVelocity().x - SPEED), getVelocity().y));
    }

    //TODO RETORNAR PVECTOR
    public void stopMovement() {
        setVelocity(new PVector(0.5f * getVelocity().x, getVelocity().y));
    }

    public void playerAttack(int now) {
        if (now - attackTime > TheKnight.ATTACK_COOLDOWN) {
            this.attack = attack();
            attackTime = now;
        }
    }

    private HurtBox attack() {
        HurtBox.Builder builder = new HurtBox.Builder();
        Direction facingDirection;
        if (directions.get(Direction.DOWN)) facingDirection = Direction.DOWN;
        else if ((directions.get(Direction.RIGHT) && directions.get(Direction.LEFT))) facingDirection = Direction.UP;
        else if (directions.get(Direction.LEFT)) facingDirection = Direction.LEFT;
        else if (directions.get(Direction.RIGHT)) facingDirection = Direction.RIGHT;
        else facingDirection = Direction.UP;

        switch (facingDirection) {
            case DOWN:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, -100).addPoint(25, -150).addPoint(-25, -150).addPoint(-55, -100);
                break;
            case LEFT:
                builder.addPoint(0, -65).addPoint(0, 65).addPoint(-100, 55).addPoint(-150, 25).addPoint(-150, -25).addPoint(-100, -55);
                break;
            case RIGHT:
                builder.addPoint(0, 65).addPoint(0, -65).addPoint(100, -55).addPoint(150, -25).addPoint(150, 25).addPoint(100, 55);
                break;
            default:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, 100).addPoint(25, 150).addPoint(-25, 150).addPoint(-55, 100);
                break;
        }

        return builder.build();
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {

        // Máquina de estados para aplicar sprites baseado no movimento com animação
        switch (movement) {
            case MovementState.IDLE:
                this.sprite = spriteArray[0][0];
                break;
            case MovementState.RUNNING:
                if (p.millis() - spriteTime > 40) {
                    this.sprite = spriteArray[spriteIndex][0];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 7) spriteIndex = 0;
                }
                break;
            case MovementState.JUMPING:
                break;
            case MovementState.FALLING:
                if (p.millis() - spriteTime > 40) {
                    this.sprite = spriteArray[spriteIndex][9];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 7) spriteIndex = 5;
                }
                break;
        }

        int multValue = 1;
        if (lastFacingDirection == Direction.LEFT) multValue = -1;

        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        p.pushMatrix();

        p.translate(pp[0] - multValue * SPRITE_SIZE / 2f, pp[1] - SPRITE_SIZE / 2f + PIXEL_CORRECTION);
        p.scale(multValue, 1);
        p.image(this.sprite, 0, 0);

        p.popMatrix();

        this.hitbox.draw(painter, plt);
    }
}