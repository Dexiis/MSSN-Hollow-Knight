package game.scenery.characters.types.enemies;

import game.core.SubPlot;
import game.scenery.World;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.KnightMovement;
import game.scenery.characters.types.State;
import game.scenery.characters.types.TheKnight;
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

/**
 * Representa o chefe FalseKnight.
 * <p>
 * Este chefe possui múltiplos padrões de ataque incluindo ataques corpo a corpo,
 * saltos e ataques aéreos. Gere estados complexos incluindo antecipação, salto,
 * aterragem e diferentes animações de morte.
 * </p>
 */
public class FalseKnight extends Enemy implements IVisualizable {
    public static final float JUMP_COOLDOWN = 1200f;
    public static final float DEATH_DURATION = 5000f;

    private float jumpTime = 0f;
    private float deathTime = 0f;

    private final NormalAttack normalAttack;
    private final JumpAttack jumpAttack;
    private final JumpFlee jumpFlee;

    private boolean grounded;
    private boolean walled;

    /**
     * Constrói um novo FalseKnight na posição especificada.
     * <p>
     * Inicializa os atributos físicos (hitbox, massa, vida), comportamentos de chefe
     * (BossAttack, JumpAttack, JumpFlee), sprites e DNA específico para este chefe.
     * </p>
     *
     * @param position a posição inicial do chefe no mundo.
     * @param p        o contexto gráfico do Processing.
     */
    public FalseKnight(PVector position, PApplet p) {
        super(position, p);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 300, 280);
        this.mass = 1f;
        this.health = 30;

        ATTACK_COOLDOWN = 1000f;

        this.normalAttack = new NormalAttack(1);
        this.jumpAttack = new JumpAttack(1);
        this.jumpFlee = new JumpFlee(1);

        PIXEL_CORRECTION = 282;
        SPRITE_SIZE = 1000;
        SPRITE_COUNT = 10;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        this.dna = new DNA(this);

        //Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("images/FalseKnightSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
        this.grounded = false;
        this.walled = false;
        this.state = State.JUMP;

        this.currentDirection = KnightMovement.LEFT;
        this.latestDirection = KnightMovement.LEFT;
        this.multValue = -1;
    }

    /**
     * Verifica se o chefe está no chão.
     * <p>
     * Devolve o estado da variável que indica se a entidade está em contacto com
     * a superfície inferior.
     * </p>
     *
     * @return {@code true} se estiver no chão, {@code false} caso contrário.
     */
    public boolean isGrounded() {
        return grounded;
    }

    /**
     * Define se o chefe está no chão.
     * <p>
     * Atualiza o estado de contacto com o solo, influenciando a física e as
     * transições de estado (como a aterragem).
     * </p>
     *
     * @param grounded {@code true} se estiver no chão, {@code false} caso contrário.
     */
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    /**
     * Verifica se o chefe está em contacto com uma parede.
     * <p>
     * Retorna a indicação de colisão lateral, usada para limitar o movimento
     * ou desencadear comportamentos específicos.
     * </p>
     *
     * @return {@code true} se estiver a tocar numa parede, {@code false} caso contrário.
     */
    public boolean isWalled() {
        return walled;
    }

    /**
     * Define se o chefe está em contacto com uma parede.
     * <p>
     * Atualiza o estado de colisão lateral da entidade.
     * </p>
     *
     * @param walled {@code true} se estiver a tocar numa parede, {@code false} caso contrário.
     */
    public void setWalled(boolean walled) {
        this.walled = walled;
    }

    /**
     * Processa a lógica de ataque e colisão.
     * <p>
     * Obtém a caixa de dano (HurtBox) ativa para o frame atual, posiciona-a corretamente
     * no mundo e verifica se intersecta o jogador. Se houver colisão, aplica dano ao jogador.
     * </p>
     *
     * @param p       o contexto gráfico do Processing.
     * @param painter o objeto auxiliar para desenho de linhas.
     * @param plt     o objeto auxiliar para conversão de coordenadas.
     */
    private void handleAttack(PApplet p, LinePainter painter, SubPlot plt) {
        HurtBox attack = attack();
        if (attack != null) {
            attack.setPosition(this.position);
            TheKnight player = World.getInstance().getPlayer();
            if (attack.intersected(player.getHitbox())) player.damage(this.position);
        }
    }

    /**
     * Gere a máquina de estados finita (FSM) do chefe.
     * <p>
     * Verifica qual é o estado atual na variável {@code state} e invoca a operação
     * correspondente. Atualiza também o registo do último estado conhecido.
     * </p>
     */
    private void stateMachine() {
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
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 4) spriteIndex = 0;
        }

        float action = p.random(1);
        if (normalAttack.checkBehaviour(this) && now - attackTime > ATTACK_COOLDOWN) {
            state = State.ANTICIPATION;
            resetAnimation();
        } else if (jumpAttack.checkBehaviour(this) && now - jumpTime > JUMP_COOLDOWN && action < 0.5f) {
            applyBehaviour(jumpAttack, dt);
            state = State.JUMP_ATTACK;
            jumpTime = now;
            resetAnimation();
        } else if (jumpFlee.checkBehaviour(this) && now - jumpTime > JUMP_COOLDOWN && action > 0.75f) {
            applyBehaviour(jumpFlee, dt);
            state = State.JUMP;
            jumpTime = now;
            resetAnimation();
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
        if (now - spriteTime > 80) {
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
     * Gere o estado de antecipação antes do ataque.
     * <p>
     * Executa a animação de preparação para o ataque. Quando a animação
     * termina, transita para o estado de ataque.
     * </p>
     */
    private void anticipating() {
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 5) {
                spriteIndex = 0;
                state = State.ATTACK;
                attackTime = now;
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
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][5];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) {
                spriteIndex = 0;
                state = State.IDLE;
            }
        }
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
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][6];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 3 && !grounded) spriteIndex = 3;
            else if (spriteIndex > 9) spriteIndex = 9;
            if (spriteIndex > 3) setVelocity(new PVector());
        }

        if (spriteIndex == 9) {
            spriteIndex = 0;
            state = State.IDLE;
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
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][2];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 6) {
                spriteIndex = 6;
                if (isGrounded()) {
                    spriteIndex = 0;
                    state = State.LAND;
                    jumpTime = now;
                    setVelocity(new PVector());
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
        if (now - spriteTime > 80) {
            this.sprite = spriteArray[spriteIndex][3];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) {
                spriteIndex = 0;
                state = State.IDLE;
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
        setVelocity(new PVector(0, 0));
        if (now - spriteTime > 160) {
            this.sprite = spriteArray[spriteIndex][7];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 2) spriteIndex = 2;
        }

        if (grounded) {
            spriteIndex = 0;
            state = State.LAND_DEATH;
        }
    }

    /**
     * Gere o estado de morte no chão do chefe.
     * <p>
     * Executa a animação de morte quando o chefe está no chão.
     * Quando a animação termina, marca o chefe como morto.
     * </p>
     */
    private void landDeath() {
        setVelocity(new PVector(0, 0));
        if (now - spriteTime > 160) {
            this.sprite = spriteArray[spriteIndex][8];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 4) spriteIndex = 4;
        }

        if (now - deathTime > DEATH_DURATION) setDead(true);
    }

    /**
     * Constrói e retorna a HurtBox correspondente ao estado e frame atuais.
     * <p>
     * Define as coordenadas e dimensões da área de ataque para cada frame das
     * animações de ataque (idle, turning, jump, anticipation, attack, jump_attack).
     * </p>
     *
     * @return a HurtBox configurada ou {@code null} se não houver ataque no frame atual.
     */
    private HurtBox attack() {
        HurtBox.Builder builder = new HurtBox.Builder();

        switch (this.state) {
            case IDLE -> {
                switch (spriteIndex) {
                    case 0 -> addFrame(builder, -155, -260, -65, 40);
                    case 1 -> addFrame(builder, -155, -260, -50, 55);
                    case 2, 3 -> addFrame(builder, -155, -260, -45, 60);
                    case 4 -> addFrame(builder, -155, -260, -60, 45);
                }
            }
            case TURNING -> {
                switch (spriteIndex) {
                    case 0 -> addFrame(builder, 65, 170, -55, 50);
                    case 1 -> addFrame(builder, -85, -190, -55, 50);
                }
            }
            case JUMP, LAND -> {
                switch (spriteIndex) {
                    case 0 -> addFrame(builder, -145, -250, -40, 65);
                    case 1 -> addFrame(builder, -125, -230, -30, 75);
                    case 2 -> addFrame(builder, -110, -215, -40, 65);
                    case 3 -> {
                        if (state == State.JUMP) addFrame(builder, -185, -290, -80, 25);
                    }
                    case 4 -> {
                        if (state == State.JUMP) addFrame(builder, -185, -290, -75, 30);
                    }
                    case 5 -> {
                        if (state == State.JUMP) addFrame(builder, -195, -300, -60, 45);
                    }
                    case 6 -> {
                        if (state == State.JUMP) addFrame(builder, -190, -295, -70, 35);
                    }
                }
            }
            case ANTICIPATION -> {
                switch (spriteIndex) {
                    case 0 -> addFrame(builder, -190, -295, -110, -5);
                    case 1, 4 -> addFrame(builder, -210, -315, -145, -40);
                    case 2 -> addFrame(builder, -200, -305, -145, -40);
                    case 3 -> addFrame(builder, -185, -290, -150, -45);
                    case 5 -> addFrame(builder, -195, -300, -145, -40);
                }
            }
            case ATTACK -> {
                switch (spriteIndex) {
                    case 0 -> addFrame(builder, 0, -105, 155, 260);
                    case 1 -> addFrame(builder, 310, 205, -35, 70);
                    case 2 -> addFrame(builder, 285, 180, -150, 150);
                    case 3 -> addFrame(builder, 15, -90, 165, 270);
                    case 4 -> addFrame(builder, -105, -210, 150, 255);
                    case 5 -> addFrame(builder, -145, -250, 100, 205);
                    case 6 -> addFrame(builder, -75, -180, 25, 130);
                    case 7 -> addFrame(builder, -125, -230, -65, 40);
                }
            }
            case JUMP_ATTACK -> {
                switch (spriteIndex) {
                    case 0 -> addFrame(builder, -180, -285, -65, 40);
                    case 1 -> addFrame(builder, -200, -305, -65, 40);
                    case 2 -> addFrame(builder, -215, -320, -120, -15);
                    case 3 -> addFrame(builder, -240, -345, -100, 5);
                    case 4 -> addFrame(builder, -75, -180, 150, 255);
                    case 5 -> addFrame(builder, 265, 160, -140, 150);
                    case 6 -> addFrame(builder, -55, -160, 155, 260);
                    case 7 -> addFrame(builder, -105, -210, 105, 210);
                    case 8 -> addFrame(builder, -100, -205, 40, 145);
                    case 9 -> addFrame(builder, -135, -240, -70, 35);
                }
            }
        }
        return builder.build();
    }

    /**
     * Adiciona um retângulo à HurtBox preservando a ordem dos pontos.
     * <p>
     * Calcula os vértices de um retângulo com base nas coordenadas fornecidas e
     * na direção atual do chefe (invertendo o eixo X se necessário), e adiciona-os
     * ao construtor da HurtBox.
     * </p>
     *
     * @param b  o construtor da HurtBox.
     * @param x1 a coordenada X inicial.
     * @param x2 a coordenada X final.
     * @param y1 a coordenada Y inicial.
     * @param y2 a coordenada Y final.
     */
    private void addFrame(HurtBox.Builder b, int x1, int x2, int y1, int y2) {
        b.addPoint(x1 * multValue, y1).addPoint(x2 * multValue, y1).addPoint(x2 * multValue, y2).addPoint(x1 * multValue, y2);
    }

    /**
     * Renderiza o chefe no ecrã.
     * <p>
     * Desenha a hitbox, atualiza a visão, gere mudanças de direção, processa
     * ataques e a máquina de estados. Aplica as transformações gráficas
     * (posição, escala e inversão) para desenhar o sprite correto.
     * </p>
     *
     * @param p       o contexto gráfico do Processing.
     * @param painter o objeto auxiliar para desenho de linhas de depuração.
     * @param plt     o objeto auxiliar para conversão de coordenadas.
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(this.getPosition().x, this.getPosition().y);

        if (isDying()) {
            if (deathTime == 0) deathTime = now;
            if (grounded) state = State.LAND_DEATH;
            else state = State.FALL_DEATH;
        }

        this.getEye().look();
        directionChange();
        handleAttack(p, painter, plt);
        stateMachine();

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(multValue * 0.7f, 0.7f);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f - PIXEL_CORRECTION);

        p.popMatrix();
    }
}