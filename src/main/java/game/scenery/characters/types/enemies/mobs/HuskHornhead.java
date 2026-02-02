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
 * investindo na direção do jogador. Durante o ataque, altera a sua caixa de colisão
 * (hitbox) e posição para refletir a animação de investida.
 * </p>
 */
public class HuskHornhead extends Mob implements IVisualizable {

    /**
     * Instancia um novo HuskHornhead na posição especificada.
     * <p>
     * Inicializa os atributos físicos (caixa de colisão, massa, vida), velocidades,
     * sprites e comportamentos (Seek, Wander, Attack) do inimigo. Carrega também
     * a folha de sprites necessária para a animação.
     * </p>
     *
     * @param position a posição inicial do inimigo no mundo.
     * @param p        o contexto gráfico do Processing.
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
        ATTACK_SPEED_BOOST = 3;

        loadSpriteSheet("images/HuskSprites.png", p, spriteArray);
    }

    /**
     * Gere o estado de inatividade (idle) do inimigo.
     * <p>
     * Verifica se as condições para iniciar um ataque estão reunidas e,
     * caso positivo, transita para o estado de antecipação. Atualiza a
     * animação de repouso com base no tempo decorrido e aplica os comportamentos de movimento.
     * </p>
     */
    @Override
    protected void idling() {
        if (attackBehaviour.checkBehaviour(this) && now - attackTime > ATTACK_COOLDOWN) {
            state = State.ANTICIPATION;
            resetAnimation();
        }
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 6) spriteIndex = 0;
        }

        applyBehaviour(seekBehaviour, dt);
        applyBehaviour(wanderBehaviour, dt);
    }

    /**
     * Controla a ação de viragem (turning) do inimigo.
     * <p>
     * Executa a animação de mudança de direção. Quando a animação termina,
     * atualiza o multiplicador de escala para inverter o sprite e retorna
     * ao estado de repouso.
     * </p>
     */
    @Override
    protected void turning() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][1];
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
     * Gere o estado de antecipação prévio ao ataque.
     * <p>
     * Executa a animação de preparação. No final desta sequência, ajusta a
     * caixa de colisão e a posição da entidade para refletir a postura de
     * investida, define as velocidades de ataque e transita para o estado ofensivo.
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

                this.position = new PVector(position.x, position.y - 22);
                this.hitbox = new Hitbox(new Point(position.x, position.y), 95, 65);
                PIXEL_CORRECTION = -31;

                this.getDna().setMaxSpeed(IDLE_SPEED * ATTACK_SPEED_BOOST);
                this.getDna().setMaxForce(IDLE_SPEED * ATTACK_SPEED_BOOST);
                attackBehaviour.saveTargetPosition(this);
                attackTime = now;
                state = State.ATTACK;
            }
        }
    }

    /**
     * Controla a execução do ataque do inimigo.
     * <p>
     * Monitoriza as condições de ataque e executa a animação de investida.
     * Ao terminar a duração do ataque ou se o alvo sair de alcance, restaura as
     * propriedades físicas originais (velocidade, hitbox) e reinicia o estado de repouso.
     * </p>
     */
    @Override
    protected void attacking() {
        if (!attackBehaviour.checkBehaviour(this)) {
            state = State.IDLE;
            resetAnimation();
        }
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][3];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 3) spriteIndex = 0;
        }

        if (now - attackTime > ATTACK_DURATION && spriteIndex == 0) {
            this.getDna().setMaxSpeed(IDLE_SPEED);
            this.getDna().setMaxForce(IDLE_SPEED);
            state = State.IDLE;

            this.setPosition(new PVector(position.x, position.y + 22));
            this.hitbox = new Hitbox(new Point(position.x, position.y), 85, 110);
            PIXEL_CORRECTION = 7;

            this.acceleration = new PVector(0, 0);
        } else applyBehaviour(attackBehaviour, dt);

    }

    /**
     * Processa a sequência de morte do inimigo.
     * <p>
     * Reproduz a animação de falecimento frame a frame. Ao concluir a animação,
     * marca a entidade como morta e redefine o estado para repouso, permitindo
     * a remoção segura do jogo.
     * </p>
     */
    @Override
    protected void death() {
        if (now - spriteTime > 80) {
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
     * Renderiza o inimigo no ecrã.
     * <p>
     * Atualiza a lógica interna (máquina de estados, visão, direção) e desenha
     * o sprite atual na posição correta, aplicando as transformações de escala
     * e translação necessárias.
     * </p>
     *
     * @param p       o contexto gráfico do Processing.
     * @param painter o objeto responsável pelo desenho de linhas de depuração.
     * @param plt     o objeto auxiliar para conversão de coordenadas.
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(this.getPosition().x, this.getPosition().y);

        if (isDying()) state = State.DEATH;
        if (state != latestState) resetAnimation();

        this.getEye().look();
        directionChange(IDLE_SPEED);
        stateMachine();

        float spriteScale = 0.7f;
        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * spriteScale, spriteScale);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f + PIXEL_CORRECTION);

        p.popMatrix();
    }
}