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
 * Representa o inimigo HuskHornhead.
 * <p>
 * Este mob possui comportamentos de procura e vagueio, e pode realizar ataques
 * investindo em direção ao jogador. Durante o ataque, altera a sua hitbox e
 * posição para refletir a animação de investida.
 * </p>
 */
public class HuskHornhead extends Mob implements IVisualizable {

    /**
     * Constrói um novo HuskHornhead na posição especificada.
     * <p>
     * Inicializa os atributos físicos (hitbox, massa, vida), velocidades,
     * sprites e comportamentos (Seek, Wander, Attack) do inimigo.
     * </p>
     *
     * @param position a posição inicial do inimigo no mundo
     * @param p        o contexto gráfico do Processing
     */
    public HuskHornhead(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 85, 110);
        this.mass = 1f;
        this.health = 5;

        ATTACK_COOLDOWN = 2000f;
        ATTACK_DURATION = 3000f;

        SPRITE_SIZE = 150;
        PIXEL_CORRECTION = 7;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        this.dna = new DNA(this);
        IDLE_SPEED = this.dna.getMaxSpeed();
        ATTACK_SPEED_BOOST = 2;

        loadSpriteSheet("images/HuskSprites.png", p, spriteArray);
    }

    /**
     * Gere o estado de antecipação antes do ataque.
     * <p>
     * Executa a animação de preparação para o ataque. No final da animação,
     * ajusta a hitbox e a posição do inimigo para refletir a postura de
     * investida e transita para o estado de ataque.
     * </p>
     */
    @Override
    protected void anticipating() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][2];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex = 0;

                this.hitbox = new Hitbox(new Point(position.x, position.y), 95, 65);
                this.position = new PVector(position.x, position.y - 22);
                PIXEL_CORRECTION = -31;

                state = State.ATTACK;
                this.getDna().setMaxSpeed(IDLE_SPEED * ATTACK_SPEED_BOOST);
                this.getDna().setMaxForce(IDLE_SPEED * ATTACK_SPEED_BOOST);
                applyBehaviour(attackBehaviour, dt);
                attackTime = now;
            }
        }
    }

    /**
     * Gere o estado de ataque do inimigo.
     * <p>
     * Define a velocidade máxima para a velocidade de ataque e executa a
     * animação de investida. Quando o alvo sai do alcance de ataque,
     * restaura a hitbox e posição originais e retorna ao estado idle.
     * </p>
     */
    @Override
    protected void attacking() {
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][3];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 3) spriteIndex = 0;
        }

        if (now - attackTime > ATTACK_DURATION) {
            state = State.IDLE;
            this.getDna().setMaxSpeed(IDLE_SPEED);
            this.getDna().setMaxForce(IDLE_SPEED);
            this.hitbox = new Hitbox(new Point(position.x, position.y), 85, 110);
            PIXEL_CORRECTION = 7;
        }
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
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) {
                setDead(true);
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
    }

    /**
     * Gere o estado de inatividade (idle) do inimigo.
     * <p>
     * Verifica se as condições para iniciar um ataque estão reunidas e,
     * caso positivo, transita para o estado de antecipação. Atualiza a
     * animação idle com base no tempo decorrido.
     * </p>
     */
    @Override
    protected void idling() {
        if (attackBehaviour.checkBehaviour(this) && now - attackTime > ATTACK_COOLDOWN) {
            state = State.ANTICIPATION;
            resetAnimation();
        }
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 5) spriteIndex = 0;
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
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][1];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 1) {
                multValue = currentDirection == KnightMovement.RIGHT ? -1 : 1;
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
    }

    /**
     * Renderiza o inimigo no ecrã.
     * <p>
     * Desenha a hitbox, atualiza o estado de ataque, gere mudanças de direção,
     * verifica se está a morrer e executa a máquina de estados. Aplica uma
     * escala de 0.7 ao sprite para ajustar o tamanho visual.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto LinePainter para desenhar a hitbox
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        this.hitbox.draw(painter, plt);

        float[] pp = plt.getPixelCoord(this.getPosition().x, this.getPosition().y);

        if (isDying()) state = State.DEATH;
        if (state != latestState) resetAnimation();

        // Diminuir o tamanho da sprite
        float spriteScale = 0.7f;

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * spriteScale, spriteScale);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f + PIXEL_CORRECTION);

        stateMachine();

        directionChange();

        p.popMatrix();
    }
}