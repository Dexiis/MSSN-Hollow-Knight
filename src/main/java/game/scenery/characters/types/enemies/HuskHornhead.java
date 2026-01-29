package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.behaviours.Seek;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class HuskHornhead extends Enemy implements IVisualizable {
    private final Seek seekBehaviour;

    public HuskHornhead(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 75, 95);
        this.mass = 1f;
        this.health = 5;

        IDLE_SPEED = 75f;
        ATTACK_SPEED = 300f;
        ATTACK_COOLDOWN = 2000f;

        SPRITE_SIZE = 150;
        PIXEL_CORRECTION = 7;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(IDLE_SPEED);

        this.seekBehaviour = new Seek(1);
        this.behaviours.add(wanderBehaviour);
        this.behaviours.add(seekBehaviour);
        this.behaviours.add(attackBehaviour);

        loadSpriteSheet("images/HuskSprites.png", p, spriteArray);
    }

    @Override
    protected void idling() {
        if (!isAttacking() && attackBehaviour.checkBehaviour(this) && p.millis() - attackTime > ATTACK_COOLDOWN) {
            state = STATE.ANTICIPATION;
            resetAnimation(p.millis());
        }
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 5) spriteIndex = 0;
        }
    }

    @Override
    protected void turning() {
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][1];
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
    protected void anticipating() {
        if (p.millis() - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][2];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 0;
                this.hitbox = new Hitbox(new Point(position.x, position.y), 75, 50);
                this.position = new PVector(position.x, position.y - 22);
                PIXEL_CORRECTION = -31;
                state = STATE.ATTACK;
            }
        }
    }

    @Override
    protected void attacking() {
        this.getDna().setMaxSpeed(ATTACK_SPEED);
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][3];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 3) {
                spriteIndex = 0;
            }
        }
        if (!attackBehaviour.checkBehaviour(this)) {
            this.hitbox = new Hitbox(new Point(position.x, position.y), 75, 95);
            this.position = new PVector(position.x, position.y + 22);
            PIXEL_CORRECTION = 7;
            state = STATE.IDLE;
            attackTime = p.millis();
        }
    }

    @Override
    protected void death() {
        if (p.millis() - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = p.millis();
            spriteIndex++;
            if (spriteIndex > 7) {
                setDead(true);
                spriteIndex = 0;
                state = STATE.IDLE;
            }
        }
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        this.hitbox.draw(painter, plt);

        seekBehaviour.setEnabled(true);

        setAttacking(state == STATE.ATTACK);
        this.getDna().setMaxSpeed(IDLE_SPEED);

        directionChange();

        if (isDying()) state = STATE.DEATH;
        if (state != latestState) resetAnimation(p.millis());

        // Diminuir o tamanho da sprite
        float spriteScale = 0.6f;

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * spriteScale, spriteScale);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f + PIXEL_CORRECTION);

        stateMachine();

        p.popMatrix();
    }
}