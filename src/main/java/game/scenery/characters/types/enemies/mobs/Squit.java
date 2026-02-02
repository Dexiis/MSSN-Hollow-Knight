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
 * Representa a entidade inimiga Squit.
 * <p>
 * Este inimigo voa em direção ao jogador e possui comportamentos de procura e vagueio.
 * Durante a ofensiva, roda o corpo em direção ao alvo e move-se numa trajetória linear.
 * Distingue-se por possuir um estado intermédio de sobressalto (startled) antes de atacar.
 * </p>
 */
public class Squit extends Mob implements IVisualizable {
    private float attackAngle;

    protected boolean colliding = false;

    /**
     * Inicializa uma nova instância de Squit na posição indicada.
     * <p>
     * Configura os atributos físicos (caixa de colisão, massa, vida), as velocidades,
     * os sprites e os comportamentos (procura, vagueio, ataque). Carrega também
     * a folha de sprites necessária para a animação.
     * </p>
     *
     * @param position a posição inicial do inimigo no mundo.
     * @param p        o contexto gráfico do Processing.
     */
    public Squit(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 80, 40);
        this.mass = 1f;
        this.health = 3;

        ATTACK_COOLDOWN = 2000f;
        ATTACK_DURATION = 2000f;

        PIXEL_CORRECTION = 0;
        SPRITE_SIZE = 150;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        this.dna = new DNA(this);
        IDLE_SPEED = this.dna.getMaxSpeed();
        ATTACK_SPEED_BOOST = 2;

        loadSpriteSheet("images/SquitSprites.png", p, spriteArray);
    }

    /**
     * Atualiza o estado de colisão da entidade.
     * <p>
     * Define se o inimigo se encontra atualmente a colidir com outra estrutura
     * ou objeto, influenciando a sua lógica de movimento.
     * </p>
     *
     * @param colliding {@code true} se houver colisão, {@code false} caso contrário.
     */
    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    /**
     * Gere o estado de inatividade (idle) do inimigo.
     * <p>
     * Verifica se as condições para iniciar uma ofensiva estão reunidas e,
     * caso afirmativo, transita para o estado de sobressalto (startled). Atualiza a
     * animação de repouso com base no tempo decorrido.
     * </p>
     */
    @Override
    protected void idling() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) spriteIndex = 0;
        }

        if (attackBehaviour.checkBehaviour(this) && now - attackTime > ATTACK_COOLDOWN) {
            state = State.STARTLED;
            resetAnimation();
        }

        applyBehaviour(seekBehaviour, dt);
        applyBehaviour(wanderBehaviour, dt);
    }

    /**
     * Controla a ação de viragem da personagem.
     * <p>
     * Executa a animação de mudança de direção. Quando a sequência termina,
     * atualiza o multiplicador de escala para espelhar o sprite e retorna
     * ao estado de repouso.
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
     * Controla o estado de sobressalto (startled).
     * <p>
     * Executa a animação de reação inicial ao detetar o jogador. Quando esta
     * ação termina, a entidade transita para o estado de antecipação.
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
     * Gere o estado de preparação prévio ao ataque.
     * <p>
     * Reproduz a animação de preparação. No final desta sequência, calcula
     * o ângulo de ataque em direção ao alvo e avança para o estado ofensivo.
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

                this.getDna().setMaxSpeed(IDLE_SPEED * ATTACK_SPEED_BOOST);
                this.getDna().setMaxForce(IDLE_SPEED * ATTACK_SPEED_BOOST);
                attackBehaviour.saveTargetPosition(this);
                attackTime = now;
                state = State.ATTACK;
            }
        }
    }

    /**
     * Controla a execução do ataque.
     * <p>
     * Ajusta a velocidade para o modo de ataque e executa a respetiva animação.
     * Se o tempo de ataque expirar, restaura as propriedades originais e
     * regressa ao estado de repouso.
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
     * Processa a sequência de morte do inimigo.
     * <p>
     * Reproduz a animação de falecimento frame a frame. Ao concluir a sequência,
     * sinaliza a entidade como morta e redefine o estado para repouso.
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
     * Desenha a caixa de colisão, atualiza a lógica de estados, gere a direção
     * e verifica colisões. Durante o ataque, aplica uma rotação específica
     * ao sprite para o alinhar com o alvo.
     * </p>
     *
     * @param p       o contexto gráfico do Processing.
     * @param painter o objeto auxiliar para desenho de linhas de depuração.
     * @param plt     o objeto auxiliar para conversão de coordenadas.
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

        this.getEye().look();
        directionChange(IDLE_SPEED);
        stateMachine();

        float spriteScale = 0.7f;
        p.pushMatrix();

        p.translate(pp[0], pp[1]);

        // Roda o Squit durante o ataque em direção ao jogador
        if (state == State.ATTACK) {
            int multiplier = Math.abs(PApplet.degrees(attackAngle)) < 90 ? -1 : 1;

            p.scale(spriteScale, multiplier * spriteScale);
            p.rotate(-multiplier * attackAngle + PApplet.radians(225));
        } else p.scale(multValue * spriteScale, spriteScale);

        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f);

        p.popMatrix();
    }
}