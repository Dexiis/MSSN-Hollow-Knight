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
    private final AgressiveSeek seekBehaviour;

    private static final int EDGE_DETECTION = 150;

    private static final PImage[][] spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

    public HuskHornhead(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 100);
        this.mass = 1f;
        this.health = 5;
        this.entityType = TYPE.GROUND;

        IDLE_SPEED = 75f;
        ATTACK_SPEED = 300f;
        ATTACK_COOLDOWN = 2000f;

        SPRITE_SIZE = 150;
        PIXEL_CORRECTION = 10;

        this.rightEdge = new Hitbox(new Point(position.x + 50, position.y), 5, 10);
        this.leftEdge = new Hitbox(new Point(position.x - 50, position.y), 5, 10);

        this.dna.setMaxSpeed(IDLE_SPEED);

        this.seekBehaviour = new AgressiveSeek(1);
        this.behaviours.add(wanderBehaviour);
        this.behaviours.add(seekBehaviour);
        this.behaviours.add(attackBehaviour);

        loadSpriteSheet("images/HuskSprites.png", p, spriteArray);
    }

    @Override
    protected void idling() {
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
                state = STATE.ATTACK;
            }
        }
    }

    @Override
    protected void attacking() {
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
        this.rightEdge.draw(painter, plt);
        this.leftEdge.draw(painter, plt);

        this.leftEdge.setPosition(new PVector(getPosition().x - EDGE_DETECTION, getPosition().y - SPRITE_SIZE / 2));
        this.rightEdge.setPosition(new PVector(getPosition().x + EDGE_DETECTION, getPosition().y - SPRITE_SIZE / 2));

        seekBehaviour.setEnabled(true);

        if(!isLeftEdgeColliding()) {
            // Se for para continuar a andar em direção ao void para
            if(seekBehaviour.getDesiredVelocity(this).x < 0) seekBehaviour.setEnabled(false);
            if(wanderBehaviour.getDesiredVelocity(this).x < 0) setVelocity(new PVector(-getVelocity().x, getVelocity().y));
        }

        if(!isRightEdgeColliding())  {
            // Se for para continuar a andar em direção ao void para
            if(seekBehaviour.getDesiredVelocity(this).x > 0) seekBehaviour.setEnabled(false);
            if(wanderBehaviour.getDesiredVelocity(this).x > 0) setVelocity(new PVector(-getVelocity().x, getVelocity().y));
        }

        setAttacking(state == STATE.ATTACK);
        this.getDNA().setMaxSpeed(IDLE_SPEED);

        directionChange();

        if(isDying()) state = STATE.DEATH;
        if(state != latestState) resetAnimation(p.millis());

        stateMachine();

        // Diminuir o tamanho da sprite
        float spriteScale = 0.6f;

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * spriteScale, spriteScale);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f + PIXEL_CORRECTION);

        p.popMatrix();
    }
}