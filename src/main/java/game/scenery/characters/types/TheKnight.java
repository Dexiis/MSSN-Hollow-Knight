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
 * Esta classe gere toda a lógica específica do herói, incluindo:
 * <ul>
 * <li>Carregamento e animação de sprites (SpriteSheet).</li>
 * <li>Máquina de estados de movimento (Correr, Saltar, Cair, Parado).</li>
 * <li>Lógica de combate e criação de caixas de ataque (HurtBox).</li>
 * <li>Física de movimento específica (velocidade, salto, inércia).</li>
 * </ul>
 */
public class TheKnight extends Entity implements IVisualizable {
    public static final float JUMP_STRENGTH = 1000f;
    public static final float SPEED = 275f;

    public static final float ATTACK_DURATION = 100f;
    public static final float ATTACK_COOLDOWN = 700f;

    private MovementState movement;
    private MovementState lastMovement;

    private final Map<Direction, Boolean> directions;
    private Direction lastFacingDirection; // LEFT or RIGHT
    Direction facingDirection;

    private HurtBox attack = null;

    private boolean grounded = false;

    private PImage auraSprite;
    private static final int AURA_SIZE = 1600;

    /**
     * Construtor do Cavaleiro.
     * <p>
     * Inicializa a física (Hitbox, massa, vida), prepara o mapa de inputs e carrega
     * a folha de sprites (SpriteSheet) para a memória, cortando-a em frames individuais.
     *
     * @param position A posição inicial do jogador no mundo.
     * @param p        O contexto PApplet necessário para carregar imagens.
     */
    public TheKnight(PVector position, PApplet p) {
        super(position);
        this.hitbox = new Hitbox(new Point(position.x, position.y), 35, 70);
        this.mass = 1f;
        this.health = 10;

        this.directions = new HashMap<>();
        this.directions.put(Direction.UP, false);
        this.directions.put(Direction.DOWN, false);
        this.directions.put(Direction.RIGHT, false);
        this.directions.put(Direction.LEFT, false);
        this.directions.put(Direction.UPRELEASED, false);

        I_FRAMES = 2000;

        PIXEL_CORRECTION = 2;
        SPRITE_SIZE = 80;
        SPRITE_COUNT = 12;
        super.spriteArray = new PImage[SPRITE_COUNT][SPRITE_COUNT];

        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage("images/TheKnightSprites.png");
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        auraSprite = p.loadImage("images/aura.png");
        this.sprite = spriteArray[0][0];
        movement = MovementState.IDLE;
        lastMovement = movement;
    }

    /**
     * Obtém a última direção horizontal para a qual o jogador estava a olhar.
     *
     * @return {@link Direction#LEFT} ou {@link Direction#RIGHT}.
     */
    public Direction getLastFacingDirection() {
        return lastFacingDirection;
    }

    /**
     * Define a última direção horizontal para a qual o jogador olhou.
     * Útil para orientar o sprite corretamente (flip horizontal).
     *
     * @param lastFacingDirection A nova direção.
     */
    public void setLastFacingDirection(Direction lastFacingDirection) {
        this.lastFacingDirection = lastFacingDirection;
    }

    /**
     * Verifica se o jogador está apoiado numa superfície física (chão).
     *
     * @return {@code true} se estiver no chão, {@code false} se estiver no ar.
     */
    public boolean isGrounded() {
        return grounded;
    }

    /**
     * Define o estado de contacto com o chão.
     * Geralmente atualizado pelo sistema de colisão do terreno.
     *
     * @param grounded O novo estado.
     */
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    /**
     * Verifica se o jogador está apoiado numa superfície física (chão).
     *
     * @return {@code true} se estiver no chão, {@code false} se estiver no ar.
     */
    public boolean isStunned() {
        return stunned;
    }

    /**
     * Define o estado de contacto com o chão.
     * Geralmente atualizado pelo sistema de colisão do terreno.
     *
     * @param stunned O novo estado.
     */
    public void setStunned(boolean stunned, PApplet p) {
        this.stunned = stunned;
        if (stunned) this.stunnedTimer = p.millis();
    }

    /**
     * Obtém o mapa de inputs de direção.
     * Indica quais as teclas que estão a ser premidas no momento.
     *
     * @return Mapa vinculando {@link Direction} a um booleano (ativo/inativo).
     */
    public Map<Direction, Boolean> getDirections() {
        return directions;
    }

    /**
     * Atualiza o estado de uma direção de movimento específica.
     *
     * @param direction A direção a atualizar.
     * @param aux       {@code true} se a tecla foi premida, {@code false} se foi solta.
     */
    public void setMovingDirection(Direction direction, boolean aux) {
        directions.put(direction, aux);
    }

    /**
     * Obtém a Hitbox de ataque ativa no momento.
     *
     * @return O objeto {@link HurtBox} se estiver a atacar, ou {@code null} caso contrário.
     */
    public HurtBox getAttack() {
        return attack;
    }

    /**
     * Define a Hitbox de ataque atual.
     *
     * @param attack A nova HurtBox ou {@code null} para cancelar o ataque.
     */
    public void setAttack(HurtBox attack) {
        this.attack = attack;
    }

    /**
     * Obtém o momento (millis) em que o último ataque foi iniciado.
     * Usado para calcular a duração e o cooldown.
     *
     * @return O tempo em milissegundos.
     */
    public float getAttackTime() {
        return attackTime;
    }

    /**
     * Define o momento do último ataque.
     *
     * @param attackTime O tempo em milissegundos.
     */
    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }

    /**
     * Obtém o estado atual de movimento da personagem (ex: IDLE, RUNNING).
     *
     * @return O enum {@link MovementState}.
     */
    public MovementState getMovement() {
        return this.movement;
    }

    /**
     * Define manualmente o último estado de movimento registado.
     * Usado para detetar transições de estado.
     *
     * @param lastMovement O estado anterior.
     */
    public void setLastMovement(MovementState lastMovement) {
        this.lastMovement = lastMovement;
    }

    /**
     * Obtém o estado de movimento registado no frame anterior.
     *
     * @return O enum {@link MovementState}.
     */
    public MovementState getLastMovement() {
        return this.lastMovement;
    }

    /**
     * Define o estado atual de movimento da personagem.
     *
     * @param movement O novo estado.
     */
    public void setMovement(MovementState movement) {
        this.movement = movement;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    /**
     * Aplica dano à entidade.
     * <p>
     * Implementa um sistema de "invencibilidade temporária" (I-Frames). A entidade só perde vida
     * se tiver passado tempo suficiente (definido por {@code I_FRAMES}) desde o último golpe recebido.
     *
     * @param p O contexto da PApplet, usado para verificar o tempo atual (millis).
     */
    @Override
    public void damage(PApplet p, PVector other) {
        if (p.millis() - lastTimeHit > I_FRAMES) {
            int direction;
            if (other.x > this.position.x) direction = -1;
            else direction = 1;

            this.setVelocity(new PVector(500 * direction, 300));

            this.stunned = true;
            this.stunnedTimer = p.millis();

            health--;
            lastTimeHit = p.millis();
        }
    }

    /**
     * Reinicia os contadores de animação.
     * Chamado sempre que o estado de movimento muda (ex: de correr para saltar),
     * para garantir que a nova animação começa do primeiro frame.
     *
     * @param now O tempo atual em milissegundos.
     */
    public void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }

    /**
     * Realiza a ação de saltar.
     * Aplica uma velocidade vertical instantânea definida por {@code JUMP_STRENGTH}.
     */
    public void jump() {
        this.setVelocity(new PVector(this.getVelocity().x, JUMP_STRENGTH));
    }

    /**
     * Move a personagem para a direita.
     * Incrementa a velocidade horizontal até ao limite definido por {@code SPEED}.
     */
    public void moveRight() {
        setVelocity(new PVector(Math.min(SPEED, getVelocity().x + SPEED), getVelocity().y));
    }

    /**
     * Move a personagem para a esquerda.
     * Decrementa a velocidade horizontal até ao limite definido por {@code -SPEED}.
     */
    public void moveLeft() {
        setVelocity(new PVector(Math.max(-SPEED, getVelocity().x - SPEED), getVelocity().y));
    }

    /**
     * Para o movimento horizontal da personagem.
     * Aplica uma força de atrito/fricção (reduzindo a velocidade em 50% por frame)
     * até que a personagem pare.
     */
    public void stopMovement() {
        setVelocity(new PVector(0.5f * getVelocity().x, getVelocity().y));
    }

    /**
     * Inicia a lógica de ataque do jogador.
     * Verifica se o tempo de recarga (cooldown) já passou antes de criar uma nova HurtBox de ataque.
     *
     * @param now O tempo atual em milissegundos.
     */
    public void playerAttack(int now) {
        if (now - attackTime > TheKnight.ATTACK_COOLDOWN) {
            this.attack = attack();
            attackTime = now;
        }
    }

    /**
     * Cria a geometria do ataque (HurtBox) baseada na direção do input.
     * <p>
     * Determina a direção do ataque (Cima, Baixo, Esquerda, Direita) baseada nas teclas premidas
     * e desenha o polígono da HurtBox correspondente à espada do cavaleiro.
     *
     * @return Uma nova instância de {@link HurtBox} configurada.
     */
    private HurtBox attack() {
        HurtBox.Builder builder = new HurtBox.Builder();
        if (directions.get(Direction.DOWN)) facingDirection = Direction.DOWN;
        else if ((directions.get(Direction.RIGHT) && directions.get(Direction.LEFT))) facingDirection = Direction.UP;
        else if (directions.get(Direction.LEFT)) facingDirection = Direction.LEFT;
        else if (directions.get(Direction.RIGHT)) facingDirection = Direction.RIGHT;
        else facingDirection = Direction.UP;

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
     * Este mét.odo gere:
     * 1. A seleção do sprite correto baseado no estado (IDLE, RUNNING, etc.) e no tempo (animação).
     * 2. A conversão de coordenadas do mundo para pixels.
     * 3. A inversão horizontal do sprite (scale -1, 1) se estiver a olhar para a esquerda.
     * 4. O desenho da Hitbox para fins de debug.
     *
     * @param p       O contexto gráfico do Processing.
     * @param painter O objeto auxiliar de desenho de linhas.
     * @param plt     O objeto SubPlot para conversão de coordenadas.
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        // Máquina de estados para aplicar sprites baseado no movimento com animação
        switch (movement) {
            case MovementState.IDLE:
                this.sprite = spriteArray[0][0];
                break;
            case MovementState.RUNNING:
                if (p.millis() - spriteTime > 40) {
                    this.sprite = spriteArray[spriteIndex][0];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 7) spriteIndex = 0;
                }
                break;
            case MovementState.JUMPING:
                break;
            case MovementState.FALLING:
                if (p.millis() - spriteTime > 40) {
                    this.sprite = spriteArray[spriteIndex][9];
                    spriteTime = p.millis();
                    spriteIndex++;
                    if (spriteIndex > 7) spriteIndex = 5;
                }
                break;
        }

        if (stunned && p.millis() - stunnedTimer >= 500) this.stunned = false;

        int multValue = 1;
        if (lastFacingDirection == Direction.LEFT) multValue = -1;

        float[] pp = plt.getPixelCoord(this.hitbox.getPosition().x, this.hitbox.getPosition().y);

        p.pushStyle();

        p.tint(255, 190);
        p.image(auraSprite, pp[0] - AURA_SIZE / 2f, pp[1] - AURA_SIZE / 2f);

        p.popStyle();

        p.pushMatrix();

        p.translate(pp[0] - multValue * SPRITE_SIZE / 2f, pp[1] - SPRITE_SIZE / 2f + PIXEL_CORRECTION);
        p.scale(multValue, 1);
        p.image(this.sprite, 0, 0);

        p.popMatrix();

        this.hitbox.draw(painter, plt);
    }
}