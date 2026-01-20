package game.scenery.characters;

import processing.core.PVector;

/**
 * Classe abstrata que define a base para a física de movimento das entidades.
 * <p>
 * Gere as grandezas cinemáticas fundamentais (posição, velocidade e aceleração)
 * e implementa a lógica de integração para simular o movimento e a resposta a forças (como a gravidade).
 */
public abstract class Movement {

    protected PVector position;
    protected PVector velocity = new PVector(0, 0);
    protected PVector acceleration = new PVector();
    protected float mass;

    /**
     * Construtor da classe de movimento.
     * Inicializa a componente física da entidade com uma posição inicial.
     *
     * @param position O vetor de posição inicial.
     */
    protected Movement(PVector position) {
        this.position = position.copy();
    }

    /**
     * Obtém a posição atual da entidade.
     *
     * @return O vetor de posição.
     */
    public PVector getPosition() {
        return position;
    }

    /**
     * Define manualmente a posição da entidade.
     *
     * @param position O novo vetor de posição.
     */
    public void setPosition(PVector position) {
        this.position = position;
    }

    /**
     * Obtém a velocidade atual da entidade.
     *
     * @return O vetor de velocidade.
     */
    public PVector getVelocity() {
        return velocity;
    }

    /**
     * Define manualmente a velocidade da entidade.
     *
     * @param velocity O novo vetor de velocidade.
     */
    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    /**
     * Obtém a aceleração atual acumulada neste frame.
     *
     * @return O vetor de aceleração.
     */
    public PVector getAcceleration() {
        return acceleration;
    }

    /**
     * Define manualmente a aceleração da entidade.
     *
     * @param acceleration O novo vetor de aceleração.
     */
    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Obtém a massa da entidade.
     *
     * @return O valor da massa.
     */
    public float getMass() {
        return mass;
    }

    /**
     * Aplica uma força física à entidade.
     * <p>
     * De acordo com a segunda lei de Newton (F = m * a), a força recebida é dividida pela massa
     * para calcular a aceleração resultante, que é acumulada no vetor de aceleração atual.
     *
     * @param force O vetor da força a aplicar.
     */
    public void applyForce(PVector force) {
        acceleration.add(PVector.div(force, mass));
    }

    /**
     * Atualiza o estado físico da entidade com base no tempo decorrido (integração de Euler).
     * <p>
     * Atualiza a velocidade com base na aceleração e a posição com base na velocidade.
     * No final, a aceleração é reiniciada a zero para o próximo frame.
     *
     * @param dt O intervalo de tempo (delta time) decorrido desde a última atualização.
     */
    public void move(float dt) {
        velocity.add(acceleration.mult(dt));
        position.add(PVector.mult(velocity, dt));
        acceleration.mult(0);
    }
}