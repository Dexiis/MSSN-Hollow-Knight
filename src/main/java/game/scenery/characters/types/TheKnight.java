package game.scenery.characters.types;

import game.core.SubPlot;
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
    public static final float SPEED = 275f;

    private HurtBox attack = null;
    private final Map<KnightMovement, Boolean> directions;
    private KnightMovement facingDirection;
    private boolean grounded = false;
    private KnightMovement lastFacingDirection;
    private State lastMovement;
    private State movement;

    protected boolean stunned = false;
    protected float stunnedTime = 0f;

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

        I_FRAMES = 2000;
        ATTACK_DURATION = 100f;
        ATTACK_COOLDOWN = 750f;

        PIXEL_CORRECTION = 2;
        SPRITE_SIZE = 80;
        SPRITE_COUNT = 12;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("images/TheKnightSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
        movement = State.JUMPING;
        lastMovement = movement;
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
    public State getLastMovement() {
        return this.lastMovement;
    }

    /**
     * Obtém o estado atual de movimento da personagem.
     *
     * @return o estado {@code State} atual (ex: IDLE, RUNNING)
     */
    public State getMovement() {
        return this.movement;
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
     * @param lastMovement o estado a registar como anterior
     */
    public void setLastMovement(State lastMovement) {
        this.lastMovement = lastMovement;
    }

    /**
     * Define o estado atual de movimento da personagem.
     *
     * @param movement o novo estado a definir
     */
    public void setMovement(State movement) {
        this.movement = movement;
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
     *
     */
    public void playerAttack() {
        if (now - attackTime > this.ATTACK_COOLDOWN) {
            this.attack = attack();
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

        switch (facingDirection) {
            case DOWN:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, -100).addPoint(25, -150).addPoint(-25, -150).addPoint(-55, -100);
                break;
            case LEFT:
                builder.addPoint(0, -65).addPoint(0, 65).addPoint(-100, 55).addPoint(-150, 25).addPoint(-150, -25).addPoint(-100, -55);
                break;
            case RIGHT:
                builder.addPoint(0, 65).addPoint(0, -65).addPoint(100, -55).addPoint(150, -25).addPoint(150, 25).addPoint(100, 55);
                break;
            default:
                builder.addPoint(-65, 0).addPoint(65, 0).addPoint(55, 100).addPoint(25, 150).addPoint(-25, 150).addPoint(-55, 100);
                break;
        }

        return builder.build();
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
        // Máquina de estados para aplicar sprites baseado no movimento com animação
        switch (movement) {
            case State.IDLE:
                this.sprite = spriteArray[0][0];
                break;
            case State.RUNNING:
                if (now - spriteTime > 40) {
                    this.sprite = spriteArray[spriteIndex][0];
                    spriteTime = now;
                    spriteIndex++;
                    if (spriteIndex > 7) spriteIndex = 0;
                }
                break;
            case State.JUMPING:
                break;
            case State.FALLING:
                if (now - spriteTime > 40) {
                    this.sprite = spriteArray[spriteIndex][9];
                    spriteTime = now;
                    spriteIndex++;
                    if (spriteIndex > 7) spriteIndex = 5;
                }
                break;
        }

        if (stunned && now - stunnedTime >= 500 && grounded) this.stunned = false;

        int multValue = 1;
        if (lastFacingDirection == KnightMovement.LEFT) multValue = -1;

        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        p.pushMatrix();

        p.translate(pp[0] - multValue * SPRITE_SIZE / 2f, pp[1] - SPRITE_SIZE / 2f + PIXEL_CORRECTION);
        p.scale(multValue, 1);
        p.image(this.sprite, 0, 0);

        p.popMatrix();

        this.hitbox.draw(painter, plt);
    }
}