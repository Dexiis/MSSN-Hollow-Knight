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
    private float attackAngle;

    private static final PImage[][] spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

    public Squit(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 3;
        this.entityType = TYPE.AIR;

        IDLE_SPEED = 150f;
        ATTACK_SPEED = 275f;
        ATTACK_COOLDOWN = 2000f;

        SPRITE_SIZE = 150;
        PIXEL_CORRECTION = 0;

        this.dna.setMaxSpeed(IDLE_SPEED);

        this.behaviours.add(new SafeSeek(1));
        this.behaviours.add(new Wander(1));
        this.behaviours.add(this.attackBehaviour);

        loadSpriteSheet("images/SquitSprites.png", p, spriteArray);
    }

    @Override
    protected void turning() {
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][5];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 0) {
                multValue = currentDirection == Direction.RIGHT ? -1 : 1;
                spriteIndex = 0;
                state = STATE.IDLE;
            }
        }
    }

    @Override
    protected void idling() {
        if(!isAttacking() && attackBehaviour.checkBehaviour(this) && p.millis() - attackTime > ATTACK_COOLDOWN) {
            state = STATE.STARTLED;
            resetAnimation(p.millis());
        }
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 2) spriteIndex = 0;
        }
    }

    @Override
    protected void attacking() {
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
    }

    @Override
    protected void startled() {
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][1];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 3) {
                spriteIndex = 0;
                state = STATE.ANTICIPATION;
            }
        }
    }

    @Override
    protected void anticipating() {
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
    }

    @Override
    protected void death() {
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
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);
        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        setAttacking(state == STATE.ATTACK);
        this.getDNA().setMaxSpeed(IDLE_SPEED);
        if(isColliding()) state = STATE.IDLE;

        directionChange();

        if(isDying()) state = STATE.DEATH;
        if(state != latestState) resetAnimation(p.millis());

        stateMachine();

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