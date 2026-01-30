package game.scenery.characters;

import game.scenery.components.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Classe abstrata que representa uma entidade viva ou interativa no jogo.
 * <p>
 * Estende a classe de movimento para herdar capacidades físicas e implementa a interface
 * de visualização. Serve como base para o Jogador e para os Inimigos, gerindo propriedades
 * comuns como vida, colisão (Hitbox), visão e atributos físicos.
 * </p>
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
     * Constrói uma nova entidade na posição especificada.
     * <p>
     * Inicializa a posição base invocando o construtor da superclasse.
     * </p>
     *
     * @param position o vetor de posição inicial da entidade
     */
    protected Entity(PVector position) {
        super(position);
    }

    /**
     * Obtém o valor atual da vida da entidade.
     *
     * @return a quantidade de vida restante
     */
    public int getHealth() {
        return health;
    }

    /**
     * Obtém a área de colisão (Hitbox) associada a esta entidade.
     *
     * @return o objeto Hitbox atual
     */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /**
     * Verifica se a entidade está atualmente a executar um ataque.
     *
     * @return {@code true} se estiver num estado ofensivo, {@code false} caso contrário
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * Verifica se a entidade está num estado de colisão com outro objeto.
     *
     * @return {@code true} se estiver a colidir, {@code false} caso contrário
     */
    public boolean isColliding() {
        return colliding;
    }

    /**
     * Verifica se a entidade está marcada como morta.
     * <p>
     * Este estado indica tipicamente que a animação de morte já terminou e a entidade
     * pode ser removida ou desativada.
     * </p>
     *
     * @return {@code true} se a entidade estiver morta, {@code false} caso contrário
     */
    public boolean isDead() {
        return this.dead;
    }

    /**
     * Verifica se a entidade perdeu toda a sua vida.
     * <p>
     * Difere da verificação de morte final, pois indica apenas que a saúde chegou a zero,
     * o que normalmente despoleta a animação de morte.
     * </p>
     *
     * @return {@code true} se a saúde for menor ou igual a zero
     */
    public boolean isDying() {
        return health <= 0;
    }

    /**
     * Define o estado ofensivo da entidade.
     *
     * @param attacking {@code true} para iniciar o ataque, {@code false} para terminar
     */
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    /**
     * Define o estado de colisão da entidade.
     *
     * @param colliding o novo estado de colisão
     */
    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }

    /**
     * Marca a entidade como morta.
     *
     * @param dead o novo estado de morte
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Define manualmente a posição da entidade no mundo.
     * <p>
     * Sobrescreve a rotina da superclasse para garantir que a caixa de colisão (Hitbox)
     * é sincronizada imediatamente para a nova localização.
     * </p>
     *
     * @param position o novo vetor de posição
     */
    @Override
    public void setPosition(PVector position) {
        this.position = position;
        this.hitbox.setPosition(position);
    }

    /**
     * Aplica dano à entidade de forma genérica.
     * <p>
     * Implementa um sistema de "invencibilidade temporária" (I-Frames). A vida só é reduzida
     * se tiver passado tempo suficiente desde o último golpe recebido.
     * </p>
     *
     * @param p o contexto gráfico para verificar o tempo atual
     */
    public void damage(PApplet p) {
        if (p.millis() - lastTimeHit > I_FRAMES) {
            health--;
            lastTimeHit = p.millis();
        }
    }

    /**
     * Aplica dano à entidade considerando a origem do ataque.
     * <p>
     * Deve ser implementado pelas subclasses para gerir reações específicas,
     * como a direção do recuo (knockback).
     * </p>
     *
     * @param p     o contexto gráfico para verificar o tempo atual
     * @param other o vetor de posição da origem do dano
     */
    public abstract void damage(PApplet p, PVector other);

    /**
     * Atualiza a posição física da entidade baseada no intervalo de tempo.
     * <p>
     * Sobrescreve a rotina da superclasse para garantir que a caixa de colisão (Hitbox)
     * acompanha sempre a nova posição da entidade após o cálculo do movimento.
     * </p>
     *
     * @param dt o intervalo de tempo decorrido
     */
    @Override
    public void move(float dt) {
        super.move(dt);
        if (hitbox != null) hitbox.setPosition(position);
    }
}