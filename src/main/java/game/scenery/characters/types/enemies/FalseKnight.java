package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.behaviours.Attack;
import game.scenery.characters.types.enemies.attributes.behaviours.Seek;
import game.scenery.characters.types.enemies.attributes.behaviours.Wander;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

public class FalseKnight extends Enemy implements IVisualizable {
    public static final float WALK_SPEED = 50f;
    public static final float ATTACK_DURANTION = 1000f;
    private Attack attackBehaviour;
    private STATE latestState;

    private STATE state;

    private enum STATE {
        IDLE, TURNING, RUN, JUMP, LAND, ANTICIPATION, ATTACK, JUMP_ATTACK, DEATH
    }

    public FalseKnight(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 200, 300);
        this.mass = 1f;
        this.health = 3;

        this.attackBehaviour = new Attack(1);

        this.dna = new DNA(this);
        this.dna.setMaxSpeed(WALK_SPEED);
        this.behaviours.add(new Seek(1));
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

    /**
     * Gere a máquina de estados finita (FSM) do inimigo.
     * <p>
     * Verifica qual é o estado atual na variável {@code state} e invoca o mét.odo
     * abstrato ou concreto correspondente. Atualiza também o registo do último estado conhecido.
     */
    @Override
    protected void stateMachine() {
        switch (state) {
            case STATE.IDLE:
                idling();
                break;
            case STATE.TURNING:
                turning();
                break;
            case STATE.RUN:
                running();
                break;
            case STATE.JUMP:
                jumping();
                break;
            case STATE.LAND:
                landing();
                break;
            case STATE.ANTICIPATION:
                anticipating();
                break;
            case STATE.ATTACK:
                attacking();
                break;
            case STATE.JUMP_ATTACK:
                jumpAttacking();
                break;
            case STATE.DEATH:
                death();
                break;
        }
        latestState = state;
    }

    @Override
    protected void idling() {
//        if (!isAttacking() && attackBehaviour.checkBehaviour(this) && p.millis() - attackTime > ATTACK_COOLDOWN) {
//            state = Enemy.STATE.ANTICIPATION;
//            resetAnimation(p.millis());
//        }
//        if (p.millis() - spriteTime > 120) {
//            this.sprite = spriteArray[spriteIndex][0];
//            spriteTime = p.millis();
//            spriteIndex++;
//            if (spriteIndex > 5) spriteIndex = 0;
//        }
    }

    @Override
    protected void turning() {
//        if (p.millis() - spriteTime > 120) {
//            this.sprite = spriteArray[spriteIndex][5];
//            spriteTime = p.millis();
//            spriteIndex++;
//            if (spriteIndex > 0) {
//                multValue = currentDirection == Direction.RIGHT ? -1 : 1;
//                spriteIndex = 0;
//                state = Enemy.STATE.IDLE;
//            }
//        }
    }

    private void running() {

    }

    private void jumping() {

    }

    private void landing() {

    }

    @Override
    protected void anticipating() {
//        if (p.millis() - spriteTime > 80) {
//            this.sprite = spriteArray[spriteIndex][2];
//            spriteTime = p.millis();
//            spriteIndex++;
//            if (spriteIndex > 5) {
//                spriteIndex = 0;
//                state = Enemy.STATE.ATTACK;
//
//                PVector targetVector = PVector.sub(this.getEye().getTarget().getPosition(), this.getPosition()).normalize();
//                attackAngle = targetVector.heading();
//            }
//        }
    }

    @Override
    protected void attacking() {
//        this.getDna().setMaxSpeed(ATTACK_SPEED);
//        if (p.millis() - spriteTime > 120) {
//            this.sprite = spriteArray[spriteIndex][3];
//            spriteTime = p.millis();
//            spriteIndex++;
//            if (spriteIndex > 2) {
//                spriteIndex = 0;
//            }
//        }
//        if (!attackBehaviour.checkBehaviour(this)) {
//            state = Enemy.STATE.IDLE;
//            attackTime = p.millis();
//        }
    }

    private void jumpAttacking() {
//        this.getDna().setMaxSpeed(ATTACK_SPEED);
//        if (p.millis() - spriteTime > 120) {
//            this.sprite = spriteArray[spriteIndex][3];
//            spriteTime = p.millis();
//            spriteIndex++;
//            if (spriteIndex > 2) {
//                spriteIndex = 0;
//            }
//        }
//        if (!attackBehaviour.checkBehaviour(this)) {
//            state = Enemy.STATE.IDLE;
//            attackTime = p.millis();
//        }
    }

    @Override
    protected void death() {
//        if (p.millis() - spriteTime > 120) {
//            this.sprite = spriteArray[spriteIndex][4];
//            spriteTime = p.millis();
//            spriteIndex++;
//            if (spriteIndex > 2) {
//                setDead(true);
//                spriteIndex = 0;
//                state = Enemy.STATE.IDLE;
//            }
//        }
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);

        if (isDying()) setDead(true);
    }
}