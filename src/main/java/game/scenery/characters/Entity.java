package game.scenery.characters;

import game.scenery.components.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Classe abstrata que representa uma entidade viva ou interativa no jogo.
 * <p>
 * Estende a classe {@link Movement} para herdar capacidades físicas e implementa {@link IVisualizable}
 * para permitir renderização. Serve como base para o Jogador e para os Inimigos, gerindo propriedades
 * comuns como vida, colisão (Hitbox), visão (Eye) e atributos físicos (DNA).
 */
public abstract class Entity extends Movement implements IVisualizable {

    protected PImage[][] spriteArray;
    protected int PIXEL_CORRECTION;
    protected int spriteIndex = 0;
    protected int spriteTime = 0;
    protected int SPRITE_COUNT;
    protected int SPRITE_SIZE;
    protected PImage sprite;

    protected int I_FRAMES;
    protected float attackTime = 0f;
    protected float lastTimeHit;
    protected int health;

    private boolean attacking = false;
    private boolean colliding = false;
    private boolean dead = false;

    protected boolean stunned = false;
    protected float stunnedTimer = 0f;

    protected Hitbox hitbox;

    /**
     * Construtor da entidade.
     * Inicializa a posição base através da superclasse {@link Movement}.
     *
     * @param position O vetor de posição inicial da entidade.
     */
    protected Entity(PVector position) {
        super(position);
    }

    /**
     * Obtém a vida atual da entidade.
     *
     * @return O valor da saúde.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Obtém a área de colisão (Hitbox) da entidade.
     *
     * @return O objeto Hitbox.
     */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /**
     * Verifica se a entidade está atualmente a executar um ataque.
     *
     * @return {@code true} se estiver a atacar.
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Verifica se a entidade está num estado de colisão.
     *
     * @return {@code true} se estiver a colidir.
     */
    public boolean isColliding() {
        return colliding;
    }

    /**
     * Verifica se a entidade está marcada como morta (estado final).
     *
     * @return {@code true} se a entidade estiver morta, {@code false} caso contrário.
     */
    public boolean isDead() {
        return this.dead;
    }

    /**
     * Verifica se a entidade está a morrer (vida esgotada).
     * <p>
     * Difere de {@code isDead()} pois indica apenas que a vida chegou a zero,
     * enquanto {@code isDead()} pode indicar que a animação de morte já terminou.
     *
     * @return {@code true} se a saúde for menor ou igual a zero.
     */
    public boolean isDying() {
        return health <= 0;
    }

    /**
     * Define o estado de ataque da entidade.
     *
     * @param attacking {@code true} para iniciar o estado de ataque, {@code false} para terminar.
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Define o estado de colisão da entidade.
     *
     * @param colliding {@code true} se houver colisão.
     */
    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    /**
     * Define o estado de morte da entidade.
     *
     * @param dead {@code true} para marcar a entidade como morta.
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Define manualmente a posição da entidade.
     * <p>
     * Sobrescreve o mét.odo da superclasse para sincronizar imediatamente a {@link Hitbox}
     * para o mesmo local.
     *
     * @param position O novo vetor de posição.
     */
    @Override
    public void setPosition(PVector position) {
        this.position = position;
        this.hitbox.setPosition(position);
    }

    /**
     * Aplica dano à entidade.
     * <p>
     * Implementa um sistema de "invencibilidade temporária" (I-Frames). A entidade só perde vida
     * se tiver passado tempo suficiente (definido por {@code I_FRAMES}) desde o último golpe recebido.
     *
     * @param p O contexto da PApplet, usado para verificar o tempo atual (millis).
     */
    public void damage(PApplet p) {
        if (p.millis() - lastTimeHit > I_FRAMES) {
            health--;
            lastTimeHit = p.millis();
        }
    }

    public abstract void damage(PApplet p, PVector other);

    /**
     * Atualiza a posição física da entidade baseada no tempo delta.
     * <p>
     * Sobrescreve o mét.odo da superclasse para garantir que a {@link Hitbox} acompanha
     * sempre a nova posição da entidade.
     *
     * @param dt O intervalo de tempo decorrido.
     */
    @Override
    public void move(float dt) {
        super.move(dt);
        if (hitbox != null) hitbox.setPosition(position);
    }
}