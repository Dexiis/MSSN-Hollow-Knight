package game.scenery.characters.types.enemies.mobs;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.KnightMovement;
import game.scenery.characters.types.State;
import game.scenery.characters.types.enemies.Mob;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa o inimigo Squit.
 * <p>
 * Este mob voa em direção ao jogador e possui comportamentos de procura e vagueio.
 * Durante o ataque, roda-se em direção ao alvo e move-se numa trajetória linear.
 * Possui um estado adicional de sobressalto (startled) antes de atacar.
 * </p>
 */
public class Squit extends Mob implements IVisualizable {
    private float attackAngle;

    protected boolean colliding = false;

    /**
     * Constrói um novo Squit na posição especificada.
     * <p>
     * Inicializa os atributos físicos (hitbox, massa, vida), velocidades,
     * sprites e comportamentos (Seek, Wander, Attack) do inimigo.
     * </p>
     *
     * @param position a posição inicial do inimigo no mundo
     * @param p        o contexto gráfico do Processing
     */
    public Squit(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 3;

        ATTACK_COOLDOWN = 2000f;
        ATTACK_DURATION = 5000f;

        PIXEL_CORRECTION = 0;
        SPRITE_SIZE = 150;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        this.dna = new DNA(this);
        IDLE_SPEED = this.dna.getMaxSpeed();
        ATTACK_SPEED_BOOST = 2;

        loadSpriteSheet("images/SquitSprites.png", p, spriteArray);
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    /**
     * Gere o estado de inatividade (idle) do inimigo.
     * <p>
     * Verifica se as condições para iniciar um ataque estão reunidas e,
     * caso positivo, transita para o estado de sobressalto (startled).
     * Atualiza a animação idle com base no tempo decorrido.
     * </p>
     */
    @Override
    protected void idling() {
        if (attackBehaviour.checkBehaviour(this) && now - attackTime > ATTACK_COOLDOWN) {
            state = State.STARTLED;
            resetAnimation();
        }

        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) spriteIndex = 0;
        }

        applyBehaviour(seekBehaviour, dt);
        applyBehaviour(wanderBehaviour, dt);
    }

    /**
     * Gere o estado de viragem (turning) do inimigo.
     * <p>
     * Executa a animação de mudança de direção. Quando a animação termina,
     * atualiza o multiplicador de escala para inverter o sprite e retorna
     * ao estado idle.
     * </p>
     */
    @Override
    protected void turning() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][5];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 1) {
                multValue = currentDirection == KnightMovement.RIGHT ? -1 : 1;
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
        applyBehaviour(seekBehaviour, dt);
        applyBehaviour(wanderBehaviour, dt);
    }

    /**
     * Gere o estado de sobressalto (startled) do inimigo.
     * <p>
     * Executa a animação de reação inicial ao detetar o jogador. Quando a
     * animação termina, transita para o estado de antecipação.
     * </p>
     */
    @Override
    protected void startled() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][1];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 3) {
                spriteIndex = 0;
                state = State.ANTICIPATION;
            }
        }
    }

    /**
     * Gere o estado de antecipação antes do ataque.
     * <p>
     * Executa a animação de preparação para o ataque. No final da animação,
     * calcula o ângulo de ataque em direção ao alvo e transita para o estado
     * de ataque.
     * </p>
     */
    @Override
    protected void anticipating() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][2];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 5) {
                spriteIndex = 0;
                PVector targetVector = PVector.sub(this.getEye().getTarget().getPosition(), this.getPosition()).normalize();
                attackAngle = targetVector.heading();

                attackBehaviour.saveTargetPosition(this);
                this.getDna().setMaxSpeed(IDLE_SPEED * ATTACK_SPEED_BOOST);
                this.getDna().setMaxForce(IDLE_SPEED * ATTACK_SPEED_BOOST);
                attackTime = now;
                state = State.ATTACK;
            }
        }
    }

    /**
     * Gere o estado de ataque do inimigo.
     * <p>
     * Define a velocidade máxima para a velocidade de ataque e executa a
     * animação de ataque. Quando o alvo sai do alcance de ataque, retorna
     * ao estado idle.
     * </p>
     */
    @Override
    protected void attacking() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][3];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) spriteIndex = 0;
        }

        if (now - attackTime > ATTACK_DURATION && spriteIndex == 0) {
            this.getDna().setMaxSpeed(IDLE_SPEED);
            this.getDna().setMaxForce(IDLE_SPEED);
            state = State.IDLE;
            this.acceleration = new PVector(0, 0);
        } else applyBehaviour(attackBehaviour, dt);
    }

    /**
     * Gere o estado de morte do inimigo.
     * <p>
     * Executa a animação de morte. Quando a animação termina, marca o
     * inimigo como morto e retorna ao estado idle.
     * </p>
     */
    @Override
    protected void death() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) {
                setDead(true);
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
    }

    /**
     * Renderiza o inimigo no ecrã.
     * <p>
     * Desenha a hitbox, atualiza o estado de ataque, gere mudanças de direção,
     * verifica se está a morrer e executa a máquina de estados. Durante o ataque,
     * roda o sprite em direção ao jogador. Aplica uma escala de 0.7 ao sprite
     * para ajustar o tamanho visual.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto LinePainter para desenhar a hitbox
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(this.getPosition().x, this.getPosition().y);

        if (colliding) {
            this.getDna().setMaxSpeed(IDLE_SPEED);
            this.getDna().setMaxForce(IDLE_SPEED);
            state = State.IDLE;
        }

        if (isDying()) state = State.DEATH;
        if (state != latestState) resetAnimation();

        // Diminuir o tamanho da sprite
        float spriteScale = 0.7f;

        p.pushMatrix();

        p.translate(pp[0], pp[1]);

        // Roda o Squit durante o ataque em direção ao jogador
        if (state == State.ATTACK) {
            int multiplier = Math.abs(PApplet.degrees(attackAngle)) < 90 ? -1 : 1;

            p.scale(spriteScale, multiplier * spriteScale);
            p.rotate(-multiplier * attackAngle + PApplet.radians(225));
        } else {
            p.scale(multValue * spriteScale, spriteScale);
        }

        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f);

        p.popMatrix();

        directionChange(IDLE_SPEED);
        stateMachine();
        this.getEye().look();
        this.hitbox.draw(painter, plt);
    }
}