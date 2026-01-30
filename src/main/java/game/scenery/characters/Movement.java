package game.scenery.characters;

import processing.core.PVector;

/**
 * Classe abstrata que define a base para a física de movimento das entidades.
 * <p>
 * Gere as grandezas cinemáticas fundamentais (posição, velocidade e aceleração)
 * e implementa a lógica de integração para simular o movimento e a resposta a forças.
 * </p>
 */
public abstract class Movement {

    protected PVector position;
    protected PVector velocity = new PVector(0, 0);
    protected PVector acceleration = new PVector();
    protected float mass = 1;

    /**
     * Constrói uma nova instância de movimento na posição especificada.
     * <p>
     * Cria uma cópia do vetor de posição para garantir a independência da referência.
     * </p>
     *
     * @param position o vetor de posição inicial
     */
    protected Movement(PVector position) {
        this.position = position.copy();
    }

    /**
     * Obtém a posição atual da entidade.
     *
     * @return o vetor de posição
     */
    public PVector getPosition() {
        return position;
    }

    /**
     * Define manualmente a posição da entidade no mundo.
     *
     * @param position o novo vetor de posição
     */
    public void setPosition(PVector position) {
        this.position = position;
    }

    /**
     * Obtém a velocidade atual da entidade.
     *
     * @return o vetor de velocidade
     */
    public PVector getVelocity() {
        return velocity;
    }

    /**
     * Define manualmente a velocidade da entidade.
     *
     * @param velocity o novo vetor de velocidade
     */
    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    /**
     * Obtém a aceleração atual da entidade.
     *
     * @return o vetor de aceleração
     */
    public PVector getAcceleration() {
        return acceleration;
    }

    /**
     * Define manualmente a aceleração da entidade.
     *
     * @param acceleration o novo vetor de aceleração
     */
    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Obtém a massa da entidade.
     *
     * @return o valor da massa
     */
    public float getMass() {
        return mass;
    }

    /**
     * Aplica uma força física à entidade.
     * <p>
     * Divide a força aplicada pela massa da entidade (segunda lei de Newton) e adiciona
     * o resultado ao vetor de aceleração acumulada.
     * </p>
     *
     * @param force o vetor da força a aplicar
     */
    public void applyForce(PVector force) {
        acceleration.add(PVector.div(force, mass));
    }

    /**
     * Atualiza o estado físico da entidade com base no intervalo de tempo fornecido.
     * <p>
     * Atualiza a velocidade com base na aceleração acumulada e atualiza a posição
     * com base na nova velocidade. No final, reinicia a aceleração a zero para o próximo ciclo.
     * </p>
     *
     * @param dt o intervalo de tempo decorrido
     */
    public void move(float dt) {
        velocity.add(PVector.mult(acceleration, dt));
        position.add(PVector.mult(velocity, dt));
        acceleration.mult(0);
    }
}