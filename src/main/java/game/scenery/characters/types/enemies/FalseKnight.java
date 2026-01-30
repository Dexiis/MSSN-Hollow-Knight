package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.KnightMovement;
import game.scenery.characters.types.State;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.bossBehaviours.JumpAttack;
import game.scenery.characters.types.enemies.attributes.bossBehaviours.JumpFlee;
import game.scenery.characters.types.enemies.attributes.bossBehaviours.NormalAttack;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Representa o chefe FalseKnight.
 * <p>
 * Este chefe possui múltiplos padrões de ataque incluindo ataques corpo a corpo,
 * saltos e ataques aéreos. Gere estados complexos incluindo antecipação, salto,
 * aterragem e diferentes animações de morte.
 * </p>
 */
public class FalseKnight extends Enemy implements IVisualizable {
    public static final float ATTACK_DURATION = 1000f;
    public static final int JUMP_COOLDOWN = 2000;

    private float jumpTime = 0f;

    private HurtBox attack;

    private NormalAttack normalAttack;
    private JumpAttack jumpAttack;
    private JumpFlee jumpFlee;
    private ArrayList<Behaviour> behaviours = new ArrayList<>();

    private boolean grounded;

    /**
     * Constrói um novo FalseKnight na posição especificada.
     * <p>
     * Inicializa os atributos físicos (hitbox, massa, vida), comportamentos de chefe
     * (BossAttack, JumpAttack, JumpFlee), sprites e DNA específico para este chefe.
     * </p>
     *
     * @param position a posição inicial do chefe no mundo
     * @param p        o contexto gráfico do Processing
     */
    public FalseKnight(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 435, 280);
        this.mass = 1f;
        this.health = 30;
        ATTACK_COOLDOWN = 2000;

        this.normalAttack = new NormalAttack(1);
        this.jumpAttack = new JumpAttack(1);
        this.jumpFlee = new JumpFlee(1);

        this.behaviours.add(normalAttack);
        this.behaviours.add(jumpAttack);
        this.behaviours.add(jumpFlee);

        PIXEL_CORRECTION = 200;
        SPRITE_SIZE = 800;
        SPRITE_COUNT = 10;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        this.dna = new DNA(this);

        //Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("images/FalseKnightSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
        this.state = State.IDLE;
    }

    /**
     * Verifica se o chefe está no chão.
     *
     * @return {@code true} se estiver no chão, {@code false} caso contrário
     */
    public boolean isGrounded() {
        return grounded;
    }

    /**
     * Define se o chefe está no chão.
     *
     * @param grounded {@code true} se estiver no chão, {@code false} caso contrário
     */
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    /**
     * Aplica uma lista de comportamentos de direção ao inimigo.
     * <p>
     * Soma as velocidades desejadas de todos os comportamentos fornecidos na lista
     * e executa o movimento resultante dessa combinação.
     * </p>
     *
     * @param dt         o intervalo de tempo para a atualização física
     */
    /**
     * Aplica uma lista de comportamentos de direção ao inimigo.
     * <p>
     * Soma as velocidades desejadas de todos os comportamentos fornecidos na lista
     * e executa o movimento resultante dessa combinação.
     * </p>
     *
     * @param dt o intervalo de tempo para a atualização física
     */
    public void applyBehaviours(float dt) {
        if (getEye() != null) getEye().look();
        PVector vd = new PVector();

        for (Behaviour behaviour : behaviours) {
            if (this.state == State.JUMPING && behaviour instanceof JumpFlee) {
                PVector vdd = behaviour.getDesiredVelocity(this);
                vd.add(vdd);
            } else if (this.state == State.ATTACK && behaviour instanceof NormalAttack) {
                PVector vdd = behaviour.getDesiredVelocity(this);
                vd.add(vdd);
            } else if (this.state == State.JUMP_ATTACK && behaviour instanceof JumpAttack) {
                PVector vdd = behaviour.getDesiredVelocity(this);
                vd.add(vdd);
            }
        }
        move(dt, vd);
    }

    /**
     * Gere a máquina de estados finita (FSM) do chefe.
     * <p>
     * Verifica qual é o estado atual na variável {@code state} e invoca a função
     * correspondente. Atualiza também o registo do último estado conhecido.
     * </p>
     */
    private void stateMachine() { //TODO
        switch (state) {
            case State.IDLE:
                idling();
                break;
            case State.TURNING:
                turning();
                break;
            case State.JUMP:
                jumping();
                break;
            case State.LAND:
                landing();
                break;
            case State.ANTICIPATION:
                anticipating();
                break;
            case State.ATTACK:
                attacking();
                break;
            case State.JUMP_ATTACK:
                jumpAttacking();
                break;
            case State.LAND_DEATH:
                landDeath();
                break;
            case State.FALL_DEATH:
                fallDeath();
                break;
        }
        latestState = state;
    }

    /**
     * Gere o estado de inatividade (idle) do chefe.
     * <p>
     * Verifica se as condições para iniciar um ataque estão reunidas e transita
     * para o estado de antecipação. Caso contrário, pode aleatoriamente iniciar
     * um salto ou um ataque aéreo. Atualiza a animação idle com base no tempo decorrido.
     * </p>
     */
    private void idling() {
        if (normalAttack.checkBehaviour(this) && now - attackTime > ATTACK_COOLDOWN) {
            attackTime = now;
            state = State.ANTICIPATION;
            resetAnimation();
        } else if (jumpAttack.checkBehaviour(this) && now - jumpTime > JUMP_COOLDOWN) {
            jumpTime = now;
            if (p.random(1) > 0.80f) state = State.JUMP;
            if (p.random(1) < 0.40f) state = State.JUMP_ATTACK;
            resetAnimation();
        }

        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 4) spriteIndex = 0;
        }
    }

    /**
     * Gere o estado de viragem (turning) do chefe.
     * <p>
     * Executa a animação de mudança de direção. Quando a animação termina,
     * atualiza o multiplicador de escala para inverter o sprite e retorna
     * ao estado idle.
     * </p>
     */
    private void turning() {
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][1];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 1) {
                multValue = currentDirection == KnightMovement.RIGHT ? 1 : -1;
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
    }

    /**
     * Gere o estado de salto do chefe.
     * <p>
     * Executa a animação de salto. Mantém o sprite final da animação até
     * o chefe aterrar, momento em que transita para o estado de aterragem.
     * </p>
     */
    private void jumping() {
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][3];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 6) {
                spriteIndex = 6;
                if (isGrounded()) {
                    spriteIndex = 0;
                    state = State.LAND;
                    jumpTime = now;
                }
            }
        }
    }

    /**
     * Gere o estado de aterragem do chefe.
     * <p>
     * Executa a animação de impacto com o chão após um salto.
     * Quando a animação termina, retorna ao estado idle.
     * </p>
     */
    private void landing() {
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) {
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
    }

    /**
     * Gere o estado de antecipação antes do ataque.
     * <p>
     * Executa a animação de preparação para o ataque. Quando a animação
     * termina, transita para o estado de ataque.
     * </p>
     */
    private void anticipating() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][5];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 5) {
                spriteIndex = 0;
                state = State.ATTACK;
            }
        }
    }

    /**
     * Gere o estado de ataque corpo a corpo do chefe.
     * <p>
     * Gera a HurtBox de ataque com base no frame atual da animação.
     * Executa a animação de ataque e retorna ao estado idle quando
     * o alvo sai do alcance.
     * </p>
     */
    private void attacking() {
        attack = attack(spriteIndex);
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][6];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) {
                state = State.IDLE;
                attackTime = now;
                spriteIndex = 0;
            }
        }
    }

    /**
     * Cria a HurtBox de ataque corpo a corpo baseada no frame de animação atual.
     *
     * @param spriteIndex o índice do sprite atual
     * @return a HurtBox do ataque, ou {@code null} se ainda não aplicável
     */
    private HurtBox attack(int spriteIndex) {
        return null;
    }

    /**
     * Gere o estado de ataque aéreo do chefe.
     * <p>
     * Gera a HurtBox de ataque durante o salto com base no frame atual.
     * Executa a animação de ataque aéreo e retorna ao estado idle quando
     * o alvo sai do alcance.
     * </p>
     */
    private void jumpAttacking() {
        attack = jumpAttack(spriteIndex);

        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][7];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 9) {
                spriteIndex = 0;
                state = State.IDLE;
                jumpTime = now;
            }
        }

    }

    /**
     * Cria a HurtBox de ataque aéreo baseada no frame de animação atual.
     *
     * @param spriteIndex o índice do sprite atual
     * @return a HurtBox do ataque aéreo, ou {@code null} se ainda não aplicável
     */
    private HurtBox jumpAttack(int spriteIndex) {
        return null;
    }

    /**
     * Gere o estado de morte no chão do chefe.
     * <p>
     * Executa a animação de morte quando o chefe está no chão.
     * Quando a animação termina, marca o chefe como morto.
     * </p>
     */
    private void landDeath() {
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][9];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 4) {
                setDead(true);
                spriteIndex = 0;
            }
        }
    }

    /**
     * Gere o estado de morte em queda do chefe.
     * <p>
     * Executa a animação de morte quando o chefe está a cair.
     * Quando a animação termina, marca o chefe como morto.
     * </p>
     */
    private void fallDeath() {
        if (now - spriteTime > 120) {
            this.sprite = spriteArray[spriteIndex][8];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) {
                setDead(true);
                spriteIndex = 0;
            }
        }
    }

    /**
     * Renderiza o chefe no ecrã.
     * <p>
     * Desenha a hitbox, gere mudanças de direção, executa a máquina de estados
     * e aplica uma escala de 0.7 ao sprite. Verifica se o chefe está a morrer
     * e marca-o como morto quando apropriado.
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

        if (isDying()) {
            if (grounded) state = State.LAND_DEATH;
            else state = State.FALL_DEATH;
        }
        
        directionChange();
        stateMachine();

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * 0.7f, 0.7f);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f - PIXEL_CORRECTION);

        p.popMatrix();


    }
}