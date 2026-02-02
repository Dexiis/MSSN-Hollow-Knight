package game.scenery.characters;

import game.scenery.components.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;

/**
 * Representa uma entidade base com comportamento físico e visual.
 * <p>
 * Serve como fundação para personagens jogáveis e adversários,
 * concentrando propriedades comuns como vida, animações, colisões,
 * temporização e interação com o mundo do jogo.
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
    protected float hitTime;
    protected int health;

    protected boolean dead = false;

    public float ATTACK_DURATION;
    public float ATTACK_COOLDOWN;

    protected int now;
    protected float dt = 1;

    protected Hitbox hitbox;

    protected static SoundFile enemyDamageSound;

    /**
     * Cria uma nova entidade numa posição específica.
     * <p>
     * Inicializa a posição base através da superclasse e prepara
     * os recursos sonoros comuns associados a impactos recebidos.
     * </p>
     *
     * @param position vetor que define a posição inicial
     * @param p        contexto gráfico do Processing
     */
    protected Entity(PVector position, PApplet p) {
        super(position);

        enemyDamageSound = new SoundFile(p, "sounds/TheKnight/hero_fluke_cast.wav");
    }

    /**
     * Devolve a quantidade atual de vida.
     * <p>
     * Este valor representa a resistência restante antes de a entidade
     * entrar em estado de eliminação.
     * </p>
     *
     * @return valor atual da vida
     */
    public int getHealth() {
        return health;
    }

    /**
     * Devolve a caixa de colisão associada.
     * <p>
     * A estrutura retornada é utilizada para deteção de interseções
     * com outras entidades e com o cenário.
     * </p>
     *
     * @return instância da área de colisão
     */
    public Hitbox getHitbox() {
        return hitbox;
    }

    /**
     * Indica se a entidade se encontra marcada como morta.
     * <p>
     * Este estado normalmente significa que o ciclo final de animação
     * já terminou e que a entidade pode ser removida do jogo.
     * </p>
     *
     * @return verdadeiro se estiver marcada como morta
     */
    public boolean isDead() {
        return this.dead;
    }

    /**
     * Indica se a entidade ficou sem vida.
     * <p>
     * Este estado sinaliza que a saúde chegou a zero ou menos,
     * sendo habitualmente utilizado para iniciar animações finais.
     * </p>
     *
     * @return verdadeiro se a vida for menor ou igual a zero
     */
    public boolean isDying() {
        return health <= 0;
    }

    /**
     * Atualiza o estado de morte da entidade.
     * <p>
     * Permite marcar explicitamente quando a entidade deixa de
     * participar nas interações do jogo.
     * </p>
     *
     * @param dead novo valor do estado de morte
     */
    public void setDead(boolean dead) {
        this.dead = dead;
    }

    /**
     * Atualiza a posição da entidade no mundo.
     * <p>
     * Garante que a caixa de colisão acompanha imediatamente
     * a nova localização atribuída.
     * </p>
     *
     * @param position novo vetor de posição
     */
    @Override
    public void setPosition(PVector position) {
        this.position = position;
        this.hitbox.setPosition(position);
    }

    /**
     * Atualiza os valores temporais internos.
     * <p>
     * Estes valores são utilizados para cálculos de movimento,
     * animações e controlo de intervalos entre ações.
     * </p>
     *
     * @param dt  intervalo de tempo decorrido
     * @param now instante temporal atual
     */
    public void updateTime(float dt, int now) {
        this.dt = dt;
        this.now = now;
    }

    /**
     * Aplica dano genérico à entidade.
     * <p>
     * Implementa um período de invulnerabilidade após impacto,
     * evitando perdas consecutivas de vida num curto espaço de tempo.
     * </p>
     */
    public void damage() {
        if (now - hitTime > I_FRAMES) {
            enemyDamageSound.play();
            health--;
            hitTime = now;
        }
    }

    /**
     * Atualiza a posição física com base no tempo decorrido.
     * <p>
     * Após o cálculo do deslocamento, sincroniza a posição
     * da caixa de colisão com a nova localização.
     * </p>
     *
     * @param dt intervalo de tempo utilizado no cálculo
     */
    @Override
    public void move(float dt) {
        super.move(dt);
        if (hitbox != null) hitbox.setPosition(position);
    }

    /**
     * Reinicia a sequência de animação do sprite.
     * <p>
     * Coloca o índice da animação no início e redefine
     * o instante de referência para o ciclo atual.
     * </p>
     */
    public void resetAnimation() {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }
}