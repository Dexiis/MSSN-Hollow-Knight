package game.scenery.characters.types;

import game.core.SubPlot;
import game.scenery.World;
import game.scenery.characters.Entity;
import game.scenery.characters.IVisualizable;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa a personagem principal controlada pelo jogador ("The Knight").
 * <p>
 * Esta classe gere a lógica específica do herói, incluindo a carga e animação de sprites,
 * a máquina de estados de movimento, o combate, a física específica e a gestão de entradas.
 * </p>
 */
public class TheKnight extends Entity implements IVisualizable {
    public static final float JUMP_STRENGTH = 1000f;
    public static final float SPEED = 300f;

    private HurtBox attack = null;
    private final Map<KnightMovement, Boolean> directions;
    private KnightMovement facingDirection;
    private boolean grounded = false;
    private KnightMovement lastFacingDirection;
    private State latestState;
    private State state;

    protected boolean stunned = true;
    protected float stunnedTime = 0f;

    private boolean moving = false;

    private static PImage attackSprite;
    private static float attackSpriteAngle;
    private static float attackOffset;
    private static final int ATTACK_WIDTH = 150;
    private static final int ATTACK_HEIGHT = 130;
    private static final int ATTACK_PIXEL_CORRECTION = 70;

    /**
     * Constrói uma nova instância do Cavaleiro na posição especificada.
     * <p>
     * Inicializa as propriedades físicas (hitbox, massa, vida), configura o mapa de direções
     * e carrega a folha de sprites, dividindo-a em quadros individuais para animação.
     * </p>
     *
     * @param position a posição inicial do jogador no mundo
     * @param p        o contexto gráfico do Processing necessário para carregar imagens
     */
    public TheKnight(PVector position, PApplet p) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 35, 70);
        this.mass = 1f;
        this.health = 10;

        this.directions = new HashMap<>();
        this.directions.put(KnightMovement.UP, false);
        this.directions.put(KnightMovement.DOWN, false);
        this.directions.put(KnightMovement.RIGHT, false);
        this.directions.put(KnightMovement.LEFT, false);
        this.directions.put(KnightMovement.UPRELEASED, false);

        I_FRAMES = 1500;
        ATTACK_DURATION = 100f;
        ATTACK_COOLDOWN = 800f;

        PIXEL_CORRECTION = 2;
        SPRITE_SIZE = 80;
        SPRITE_COUNT = 12;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        attackSprite = p.loadImage("images/TheKnightAttack.png");

        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("images/TheKnightSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
        state = State.JUMPING;
        latestState = state;
    }

    /**
     * Obtém a caixa de dano (HurtBox) ativa no momento.
     *
     * @return a {@code HurtBox} atual ou {@code null} se não houver ataque ativo
     */
    public HurtBox getAttack() {
        return attack;
    }

    /**
     * Obtém o momento em que o último ataque foi iniciado.
     *
     * @return o tempo em milissegundos
     */
    public float getAttackTime() {
        return attackTime;
    }

    /**
     * Obtém o mapa de entradas de direção.
     *
     * @return um mapa que associa cada direção a um valor booleano (ativo/inativo)
     */
    public Map<KnightMovement, Boolean> getDirections() {
        return directions;
    }

    /**
     * Obtém a direção para a qual o jogador está virado atualmente.
     *
     * @return a direção {@code KnightMovement} atual
     */
    public KnightMovement getFacingDirection() {
        return facingDirection;
    }

    /**
     * Obtém a última direção horizontal para a qual o jogador olhou.
     * <p>
     * Retorna a direção LEFT ou RIGHT.
     * </p>
     *
     * @return a direção {@code LEFT} ou {@code RIGHT}
     */
    public KnightMovement getLastFacingDirection() {
        return lastFacingDirection;
    }

    /**
     * Obtém o estado de movimento registado no quadro anterior.
     *
     * @return o estado {@code State} anterior
     */
    public State getLatestState() {
        return this.latestState;
    }

    /**
     * Obtém o estado atual de movimento da personagem.
     *
     * @return o estado {@code State} atual (ex: IDLE, RUNNING)
     */
    public State getState() {
        return this.state;
    }

    /**
     * Verifica se o jogador está apoiado numa superfície física.
     *
     * @return {@code true} se estiver no chão, {@code false} caso contrário
     */
    public boolean isGrounded() {
        return grounded;
    }

    /**
     * Verifica se o jogador está atordoado.
     *
     * @return {@code true} se estiver atordoado, {@code false} caso contrário
     */
    public boolean isStunned() {
        return stunned;
    }

    /**
     * Define a caixa de dano (HurtBox) atual.
     *
     * @param attack a nova {@code HurtBox} ou {@code null} para cancelar o ataque
     */
    public void setAttack(HurtBox attack) {
        this.attack = attack;
    }

    /**
     * Regista o momento do último ataque.
     *
     * @param attackTime o tempo em milissegundos
     */
    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }

    /**
     * Define a direção para a qual o jogador está virado.
     *
     * @param facingDirection a nova direção
     */
    public void setFacingDirection(KnightMovement facingDirection) {
        this.facingDirection = facingDirection;
    }

    /**
     * Define o estado de contacto com o chão.
     *
     * @param grounded o novo estado de contacto
     */
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    /**
     * Define a última direção horizontal do jogador.
     * <p>
     * Esta informação é utilizada para orientar o sprite corretamente (inversão horizontal).
     * </p>
     *
     * @param lastFacingDirection a nova direção horizontal
     */
    public void setLastFacingDirection(KnightMovement lastFacingDirection) {
        this.lastFacingDirection = lastFacingDirection;
    }

    /**
     * Define manualmente o registo do estado anterior.
     *
     * @param latestState o estado a registar como anterior
     */
    public void setLatestState(State latestState) {
        this.latestState = latestState;
    }

    /**
     * Define o estado atual de movimento da personagem.
     *
     * @param state o novo estado a definir
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Atualiza o estado de uma direção de movimento específica.
     *
     * @param direction a direção a atualizar no mapa
     * @param aux       {@code true} se a tecla foi premida, {@code false} se foi solta
     */
    public void setMovingDirection(KnightMovement direction, boolean aux) {
        directions.put(direction, aux);
    }

    /**
     * Define o estado de atordoamento do jogador.
     * <p>
     * Se o estado for verdadeiro, inicia também o temporizador de atordoamento.
     * </p>
     *
     * @param stunned o novo estado de atordoamento
     */
    public void setStunned(boolean stunned) {
        this.stunned = stunned;
        if (stunned) this.stunnedTime = now;
    }

    /**
     * Aplica dano à entidade e gere a reação física.
     * <p>
     * Se o período de invencibilidade tiver passado, reduz a vida, aplica uma força de
     * repulsão (knockback) na direção oposta ao dano e coloca a entidade em estado de atordoamento.
     * </p>
     *
     * @param other o vetor de posição da origem do dano
     */
    public void damage(PVector other) {
        if (now - hitTime > I_FRAMES) {
            int direction;
            if (other.x > this.position.x) direction = -1;
            else direction = 1;

            this.setVelocity(new PVector(500 * direction, 300));

            this.stunned = true;
            this.stunnedTime = now;

            health--;
            hitTime = now;
        }
    }

    /**
     * Executa a ação de saltar.
     * <p>
     * Aplica uma velocidade vertical instantânea definida pela constante de força de salto.
     * </p>
     */
    public void jump() {
        this.setVelocity(new PVector(this.getVelocity().x, JUMP_STRENGTH));
    }

    /**
     * Move a personagem para a esquerda.
     * <p>
     * Decrementa a velocidade horizontal até atingir o limite máximo definido.
     * </p>
     */
    public void moveLeft() {
        setVelocity(new PVector(Math.max(-SPEED, getVelocity().x - SPEED), getVelocity().y));
    }

    /**
     * Move a personagem para a direita.
     * <p>
     * Incrementa a velocidade horizontal até atingir o limite máximo definido.
     * </p>
     */
    public void moveRight() {
        setVelocity(new PVector(Math.min(SPEED, getVelocity().x + SPEED), getVelocity().y));
    }

    /**
     * Inicia a lógica de ataque do jogador.
     * <p>
     * Verifica se o tempo de recarga já expirou antes de gerar uma nova caixa de ataque.
     * </p>
     */
    public void playerAttack() {
        if (now - attackTime > this.ATTACK_COOLDOWN && !stunned) {
            this.attack = attack();
            state = State.ATTACK;
            resetAnimation();
            attackTime = now;
        }
    }


    /**
     * Interrompe o movimento horizontal da personagem.
     * <p>
     * Aplica uma força de atrito reduzindo a velocidade pela metade a cada chamada,
     * até que a personagem pare.
     * </p>
     */
    public void stopMovement() {
        setVelocity(new PVector(0.5f * getVelocity().x, getVelocity().y));
    }

    /**
     * Constrói a geometria da caixa de ataque (HurtBox).
     * <p>
     * Determina a direção do ataque com base nas teclas premidas e define os vértices
     * do polígono de colisão correspondente.
     * </p>
     *
     * @return uma nova instância de {@code HurtBox} configurada
     */
    private HurtBox attack() {
        HurtBox.Builder builder = new HurtBox.Builder();
        if (directions.get(KnightMovement.DOWN)) facingDirection = KnightMovement.DOWN;
        else if ((directions.get(KnightMovement.RIGHT) && directions.get(KnightMovement.LEFT)))
            facingDirection = KnightMovement.UP;
        else if (directions.get(KnightMovement.LEFT)) facingDirection = KnightMovement.LEFT;
        else if (directions.get(KnightMovement.RIGHT)) facingDirection = KnightMovement.RIGHT;
        else facingDirection = KnightMovement.UP;

        attackOffset = 0;

        switch (facingDirection) {
            case DOWN:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, -100).addPoint(25, -150).addPoint(-25, -150).addPoint(-55, -100);
                attackSpriteAngle = 270;
                attackOffset = (ATTACK_HEIGHT + ATTACK_PIXEL_CORRECTION) * 2;
                break;
            case LEFT:
                builder.addPoint(0, -65).addPoint(0, 65).addPoint(-100, 55).addPoint(-150, 25).addPoint(-150, -25).addPoint(-100, -55);
                attackSpriteAngle = 0;
                attackOffset = (ATTACK_WIDTH + ATTACK_PIXEL_CORRECTION) * 2;
                break;
            case RIGHT:
                builder.addPoint(0, 65).addPoint(0, -65).addPoint(100, -55).addPoint(150, -25).addPoint(150, 25).addPoint(100, 55);
                attackSpriteAngle = 180;
                attackOffset = (ATTACK_WIDTH + ATTACK_PIXEL_CORRECTION) * 2;
                break;
            default:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, 100).addPoint(25, 150).addPoint(-25, 150).addPoint(-55, 100);
                attackSpriteAngle = 90;
                attackOffset = (ATTACK_HEIGHT + ATTACK_PIXEL_CORRECTION) * 2;
                break;
        }

        return builder.build();
    }

    /**
     * Gere a lógica de movimento do jogador baseada nas entradas (inputs).
     * <p>
     * Controla a máquina de estados de movimento (IDLE, RUNNING, JUMPING, FALLING)
     * e aplica as ações correspondentes como mover para os lados ou saltar.
     */
    private void handleInputMovements() {
        if ((!(directions.get(KnightMovement.RIGHT)) && !(directions.get(KnightMovement.LEFT))) || (directions.get(KnightMovement.RIGHT)) && (directions.get(KnightMovement.LEFT))) {
            stopMovement();
            moving = false;
        } else {
            if (directions.get(KnightMovement.RIGHT)) {
                lastFacingDirection = KnightMovement.RIGHT;
                moveRight();
            }

            if (directions.get(KnightMovement.LEFT)) {
                lastFacingDirection = KnightMovement.LEFT;
                moveLeft();
            }

            if (grounded) moving = true;
        }

        if (grounded && directions.get(KnightMovement.UP)) jump();

        if (directions.get(KnightMovement.UPRELEASED) && directions.get(KnightMovement.UP)) {
            directions.put(KnightMovement.UPRELEASED, false);
            directions.put(KnightMovement.UP, false);
            if (this.velocity.y > 0) this.velocity = new PVector(this.velocity.x, 0);
        }

    }

    /**
     * Gere a lógica de combate do jogador.
     * <p>
     * Atualiza a posição da área de ataque (hitbox), verifica interseções com inimigos,
     * aplica dano e remove inimigos derrotados.
     */
    private void handleAttack(PApplet p, LinePainter painter, SubPlot plt) {
        if (attack != null) {
            attack.setPosition(this.position);

            if (World.getInstance().getEnemies() != null)
                for (int i = World.getInstance().getEnemies().size() - 1; i >= 0; i--) {
                    Enemy enemy = World.getInstance().getEnemies().get(i);
                    if (attack.intersected(enemy.getHitbox())) {
                        enemy.damage(this.position);

                        // Pequeno salto ao bater para baixo no ar
                        if (!grounded && facingDirection == KnightMovement.DOWN)
                            this.velocity = new PVector(this.velocity.x, 400f);
                    }
                }

            if (now - attackTime > ATTACK_DURATION) setAttack(null);
        }
    }

    private void stateMachine() {
        switch (state) {
            case State.IDLE:
                idling();
                break;
            case State.RUNNING:
                running();
                break;
            case State.ATTACK:
                attacking();
                break;
            case State.JUMPING:
                jumping();
                break;
            case State.FALLING:
                falling();
                break;
        }
    }

    private void idling() {
        if (moving) state = State.RUNNING;
        if (!grounded) state = State.JUMPING;
        this.sprite = spriteArray[0][0];
    }

    private void running() {
        if (!moving) state = State.IDLE;
        if (!grounded) state = State.JUMPING;
        if (now - spriteTime > 40) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) spriteIndex = 0;
        }
    }

    private void jumping() {
        if (this.velocity.y < 0) state = State.FALLING;
    }

    private void falling() {
        if (grounded) state = State.IDLE;
        if (now - spriteTime > 40) {
            this.sprite = spriteArray[spriteIndex][9];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) spriteIndex = 5;
        }
    }

    private void attacking() {
        if (now - spriteTime > ATTACK_DURATION / 6) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 5) {
                state = State.IDLE;
                resetAnimation();
            }
        }
    }

    private void displayAttack(PApplet p, LinePainter painter, SubPlot plt) {
        if(attack == null) return;

        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(0.6f);
        p.rotate(PApplet.radians(attackSpriteAngle));

        p.scale(0.55f, 1.5f);
        p.image(attackSprite, -attack.getWidth() / 2f - attackOffset, -attack.getHeight() / 2f);

        p.popMatrix();
    }

    /**
     * Renderiza o jogador no ecrã.
     * <p>
     * Esta função gere a seleção de sprites baseada no estado e no tempo, converte
     * coordenadas do mundo para pixéis e desenha a personagem com as transformações
     * adequadas (escala e direção).
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto auxiliar para desenho de linhas
     * @param plt     o objeto de gestão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        if (stunned && now - stunnedTime >= 500 && grounded) this.stunned = false;

        if (getState() != getLatestState()) {
            resetAnimation();
            setLatestState(getState());
        }

        if (!stunned) handleInputMovements();
        handleAttack(p, painter, plt);
        displayAttack(p, painter, plt);

        int multValue = (lastFacingDirection == KnightMovement.LEFT) ? -1 : 1;

        stateMachine();

        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        p.pushMatrix();

        p.translate(pp[0] - multValue * SPRITE_SIZE / 2f, pp[1] - SPRITE_SIZE / 2f + PIXEL_CORRECTION);
        p.scale(multValue, 1);
        p.image(this.sprite, 0, 0);

        p.popMatrix();
    }
}