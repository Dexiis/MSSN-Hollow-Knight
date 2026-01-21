package game.scenery.characters;

import game.scenery.characters.attributes.Behaviour;
import game.scenery.characters.attributes.DNA;
import game.scenery.characters.attributes.Eye;
import game.scenery.components.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

/**
 * Classe abstrata que representa uma entidade viva ou interativa no jogo.
 * <p>
 * Estende a classe {@link Movement} para herdar capacidades físicas e implementa {@link IVisualizable}
 * para permitir renderização. Serve como base para o Jogador e para os Inimigos, gerindo propriedades
 * comuns como vida, colisão (Hitbox), visão (Eye) e atributos físicos (DNA).
 */
public abstract class Entity extends Movement implements IVisualizable {

    private static final int I_FRAMES = 700;

    protected Eye eye;
    protected DNA dna;
    protected Hitbox hitbox;

    protected int health;
    protected float lastTimeHit;
    protected float phiWander;

    protected float[] positions;
    private double[] window;

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
     * Verifica se a entidade está morta.
     *
     * @return {@code true} se a vida for menor ou igual a zero, {@code false} caso contrário.
     */
    public boolean isDead() {
        return health <= 0;
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
     * Obtém o sensor visual (Olho) da entidade.
     *
     * @return O objeto Eye.
     */
    public Eye getEye() {
        return this.eye;
    }

    /**
     * Define o sensor visual da entidade.
     *
     * @param eye O novo objeto Eye.
     */
    public void setEye(Eye eye) {
        this.eye = eye;
    }

    /**
     * Obtém o ângulo atual de "vaguear" (wander angle).
     * Usado por comportamentos de movimento aleatório suave.
     *
     * @return O valor do ângulo em radianos.
     */
    public float getPhiWander() {
        return phiWander;
    }

    /**
     * Define o ângulo de "vaguear".
     *
     * @param newPhiWander O novo ângulo em radianos.
     */
    public void setPhiWander(float newPhiWander) {
        this.phiWander = newPhiWander;
    }

    /**
     * Obtém os atributos genéticos (DNA) da entidade.
     * O DNA contém limites físicos como velocidade máxima e força máxima.
     *
     * @return O objeto DNA.
     */
    public DNA getDNA() {
        return dna;
    }

    /**
     * Define manualmente a posição da entidade.
     * <p>
     * Sobrescreve o método da superclasse para sincronizar imediatamente a {@link Hitbox}
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

    /**
     * Aplica um comportamento de direção (steering behaviour) único à entidade.
     * <p>
     * O método ativa o sensor visual (se existir), calcula a velocidade desejada pelo comportamento
     * e aplica a força de movimento correspondente.
     *
     * @param behaviour O comportamento a aplicar.
     * @param dt        O intervalo de tempo para a atualização física.
     */
    public void applyBehaviour(Behaviour behaviour, float dt) {
        if (eye != null) eye.look();
        PVector vd = behaviour.getDesiredVelocity(this);
        move(dt, vd);
    }

    /**
     * Aplica uma lista de comportamentos combinados.
     * <p>
     * Calcula a soma das velocidades desejadas de todos os comportamentos fornecidos.
     *
     * @param behaviours A lista de comportamentos a processar.
     * @param dt         O intervalo de tempo para a atualização física.
     */
    public void applyBehaviours(List<Behaviour> behaviours, float dt) {
        if (eye != null) eye.look();
        PVector vd = new PVector();
//      float sumWeights = 0;

//      for (Behaviour behaviour : behaviours)                        // ISTO NÃO DEVE DE SER NECESSÁRIO MAS DEIXAR POR ENQUANTO
//          sumWeights += behaviour.getWeight();

        for (Behaviour behaviour : behaviours) {
            PVector vdd = behaviour.getDesiredVelocity(this);
//          vdd.mult(behaviour.getWeight() / sumWeights);             O MESMO PARA ISTO
//          vdd.mult(behaviour.getWeight());
            vd.add(vdd);
        }

        move(dt, vd);
    }

    /**
     * Atualiza a posição física da entidade baseada no tempo delta.
     * <p>
     * Sobrescreve o método da superclasse para garantir que a {@link Hitbox} acompanha
     * sempre a nova posição da entidade.
     *
     * @param dt O intervalo de tempo decorrido.
     */
    @Override
    public void move(float dt) {
        super.move(dt);
        if (hitbox != null) hitbox.setPosition(position);
    }

    /**
     * Executa o movimento baseado numa velocidade desejada (Steering Force).
     * <p>
     * Implementa a fórmula de Reynolds: Força = Velocidade Desejada - Velocidade Atual.
     * A força resultante é limitada pela força máxima definida no DNA da entidade.
     *
     * @param dt O intervalo de tempo.
     * @param vd O vetor de velocidade desejada (Desired Velocity).
     */
    public void move(float dt, PVector vd) {
        vd.normalize().mult(dna.getMaxSpeed());
        PVector fs = PVector.sub(vd, velocity);
        applyForce(fs.limit(dna.getMaxForce()));
        move(dt);
    }
}