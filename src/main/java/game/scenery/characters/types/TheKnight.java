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
import processing.sound.SoundFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa a personagem principal controlada pelo jogador.
 * <p>
 * Esta classe encapsula o comportamento completo do cavaleiro, incluindo
 * movimentação, combate, animações, interações físicas, gestão de estados
 * e reprodução de efeitos sonoros associados às ações executadas.
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

    private static SoundFile jumpSound;
    private static SoundFile runSound;
    private static SoundFile fallingSound;
    private static SoundFile landSound;
    private static SoundFile attackSound;
    private static SoundFile damageSound;

    private static boolean isJumpSoundPlaying = false;
    private static boolean isRunSoundPlaying = false;
    private static boolean isFallingSoundPlaying = false;
    private static boolean isAttackSoundPlaying = false;

    /**
     * Cria uma nova instância do cavaleiro na posição indicada.
     * <p>
     * Inicializa as propriedades físicas, estados internos, direções de movimento,
     * sprites, animações e efeitos sonoros necessários para o funcionamento
     * completo da personagem no jogo.
     * </p>
     *
     * @param position posição inicial no mundo
     * @param p        contexto gráfico do Processing
     */
    public TheKnight(PVector position, PApplet p) {
        super(position, p);
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

        jumpSound = new SoundFile(p, "sounds/TheKnight/hero_jump.wav");
        runSound = new SoundFile(p, "sounds/TheKnight/hero_run_footsteps_stone.wav");
        fallingSound = new SoundFile(p, "sounds/TheKnight/hero_falling.wav");
        landSound = new SoundFile(p, "sounds/TheKnight/hero_land_soft.wav");
        attackSound = new SoundFile(p, "sounds/TheKnight/hero_butterfly_blade.wav");
        damageSound = new SoundFile(p, "sounds/TheKnight/hero_damage.wav");
    }

    /**
     * Devolve o estado registado anteriormente.
     * <p>
     * Este valor é usado para detetar transições de estado e reiniciar animações
     * sempre que ocorre uma mudança.
     * </p>
     *
     * @return estado anterior da personagem
     */
    public State getLatestState() {
        return this.latestState;
    }

    /**
     * Devolve o estado atual da personagem.
     * <p>
     * O estado representa a ação principal em execução, como parado, a correr,
     * a saltar, a cair ou a atacar.
     * </p>
     *
     * @return estado atual
     */
    public State getState() {
        return this.state;
    }

    /**
     * Define a área de ataque ativa.
     * <p>
     * Quando esta referência é nula, não existe qualquer ataque em curso.
     * </p>
     *
     * @param attack instância da área de dano ou nulo
     */
    public void setAttack(HurtBox attack) {
        this.attack = attack;
    }

    /**
     * Atualiza a informação de contacto com o chão.
     * <p>
     * Este valor influencia o comportamento de saltos, quedas e transições
     * entre estados.
     * </p>
     *
     * @param grounded indica se a personagem está apoiada no chão
     */
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    /**
     * Atualiza manualmente o estado anterior.
     * <p>
     * Utilizado para garantir sincronização correta entre animações
     * e mudanças de comportamento.
     * </p>
     *
     * @param latestState estado a registar
     */
    public void setLatestState(State latestState) {
        this.latestState = latestState;
    }

    /**
     * Regista o estado de uma direção de movimento.
     * <p>
     * Este registo reflete as ações do utilizador através do teclado
     * e influencia a movimentação da personagem.
     * </p>
     *
     * @param direction direção associada
     * @param aux       valor do estado da tecla
     */
    public void setMovingDirection(KnightMovement direction, boolean aux) {
        directions.put(direction, aux);
    }

    /**
     * Aplica dano à personagem.
     * <p>
     * Caso o período de invulnerabilidade tenha terminado, a vida é reduzida,
     * é aplicada uma força de recuo e a personagem entra temporariamente
     * num estado de incapacidade.
     * </p>
     *
     * @param other posição da origem do impacto
     */
    public void damage(PVector other) {
        if (now - hitTime > I_FRAMES) {
            playDamageSound();

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
     * Reproduz o efeito sonoro de dano.
     * <p>
     * Este som é tocado sempre que a personagem sofre um impacto válido.
     * </p>
     */
    public void playDamageSound() {
        damageSound.play(1.0f, 0.3f);
    }

    /**
     * Executa um salto vertical.
     * <p>
     * A velocidade vertical é ajustada instantaneamente de acordo
     * com a força de salto definida.
     * </p>
     */
    public void jump() {
        this.setVelocity(new PVector(this.getVelocity().x, JUMP_STRENGTH));
    }

    /**
     * Move a personagem para a esquerda.
     * <p>
     * A velocidade horizontal é reduzida até ao limite máximo permitido.
     * </p>
     */
    public void moveLeft() {
        setVelocity(new PVector(Math.max(-SPEED, getVelocity().x - SPEED), getVelocity().y));
    }

    /**
     * Move a personagem para a direita.
     * <p>
     * A velocidade horizontal é aumentada até ao limite máximo permitido.
     * </p>
     */
    public void moveRight() {
        setVelocity(new PVector(Math.min(SPEED, getVelocity().x + SPEED), getVelocity().y));
    }

    /**
     * Inicia uma ação ofensiva.
     * <p>
     * Verifica se o tempo de recarga terminou antes de criar
     * uma nova área de ataque.
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
     * Reduz progressivamente o movimento horizontal.
     * <p>
     * Simula atrito, diminuindo a velocidade até à imobilização.
     * </p>
     */
    public void stopMovement() {
        setVelocity(new PVector(0.5f * getVelocity().x, getVelocity().y));
    }

    /**
     * Cria a área geométrica do ataque.
     * <p>
     * A forma e orientação são definidas com base na direção atual
     * da personagem e nas entradas ativas.
     * </p>
     *
     * @return nova área de dano configurada
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
     * Processa as entradas de movimento.
     * <p>
     * Atualiza velocidades, estados e ações associadas às teclas
     * de deslocação e salto.
     * </p>
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
     * Gere a lógica de colisões ofensivas.
     * <p>
     * Atualiza a posição da área de ataque, verifica interseções
     * com inimigos e aplica os efeitos correspondentes.
     * </p>
     *
     * @param p       contexto gráfico
     * @param painter utilitário de desenho
     * @param plt     gestor de coordenadas
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

    /**
     * Atualiza o comportamento com base no estado atual.
     * <p>
     * Encaminha a execução para a lógica correspondente ao estado ativo.
     * </p>
     */
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

    /**
     * Processa o comportamento parado.
     * <p>
     * Atualiza o sprite base e verifica transições para outros estados.
     * </p>
     */
    private void idling() {
        if (moving) state = State.RUNNING;
        if (!grounded) state = State.JUMPING;
        this.sprite = spriteArray[0][0];
    }

    /**
     * Processa o comportamento de corrida.
     * <p>
     * Atualiza animações, sons e transições associadas ao deslocamento horizontal.
     * </p>
     */
    private void running() {
        if (!isRunSoundPlaying) {
            runSound.loop();
            isRunSoundPlaying = true;
        }
        if (!moving) {
            runSound.stop();
            isRunSoundPlaying = false;
            state = State.IDLE;
        }
        if (!grounded) {
            runSound.stop();
            isRunSoundPlaying = false;
            state = State.JUMPING;
        }
        if (now - spriteTime > 40) {
            this.sprite = spriteArray[spriteIndex][0];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) spriteIndex = 0;
        }
    }

    /**
     * Processa o comportamento de salto.
     * <p>
     * Controla a transição entre subida e queda.
     * </p>
     */
    private void jumping() {
        if (!isJumpSoundPlaying) {
            jumpSound.play();
            isJumpSoundPlaying = true;
        }
        if (this.velocity.y < 0) {
            isJumpSoundPlaying = false;
            state = State.FALLING;
        }
    }

    /**
     * Processa o comportamento de queda.
     * <p>
     * Atualiza animações, sons e verifica o contacto com o chão.
     * </p>
     */
    private void falling() {
        if (!isFallingSoundPlaying) {
            fallingSound.loop(1.0f, 0.4f);
            isFallingSoundPlaying = true;
        }
        if (grounded) {
            landSound.play();
            fallingSound.stop();
            isFallingSoundPlaying = false;
            state = State.IDLE;
        }
        if (now - spriteTime > 40) {
            this.sprite = spriteArray[spriteIndex][9];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 7) spriteIndex = 5;
        }
    }

    /**
     * Processa o comportamento ofensivo.
     * <p>
     * Atualiza a animação do ataque e gere a sua conclusão.
     * </p>
     */
    private void attacking() {
        if (!isAttackSoundPlaying) {
            attackSound.play(1.0f, 0.4f);
            isAttackSoundPlaying = true;
        }
        if (now - spriteTime > ATTACK_DURATION / 6) {
            this.sprite = spriteArray[spriteIndex][4];
            spriteTime = now;
            spriteIndex++;
            if (spriteIndex > 5) {
                isAttackSoundPlaying = false;
                state = State.IDLE;
                resetAnimation();
            }
        }
    }

    /**
     * Desenha visualmente a área de ataque.
     * <p>
     * Aplica transformações gráficas necessárias para alinhar
     * o sprite do ataque com a posição e direção corretas.
     * </p>
     *
     * @param p       contexto gráfico
     * @param painter utilitário de desenho
     * @param plt     gestor de coordenadas
     */
    private void displayAttack(PApplet p, LinePainter painter, SubPlot plt) {
        if (attack == null) return;

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
     * Desenha a personagem no ecrã.
     * <p>
     * Atualiza estados, animações, lógica de combate e converte
     * coordenadas do mundo para o sistema gráfico antes do desenho.
     * </p>
     *
     * @param p       contexto gráfico
     * @param painter utilitário de desenho
     * @param plt     gestor de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        if (stunned && now - stunnedTime >= 500 && grounded) this.stunned = false;

        if (isDying()) setDead(true);

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
