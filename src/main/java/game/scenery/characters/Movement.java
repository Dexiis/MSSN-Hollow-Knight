package game.scenery.characters;

import processing.core.PVector;

/**
 * Define a base do comportamento físico de deslocação.
 * <p>
 * Centraliza o controlo das grandezas cinemáticas essenciais, nomeadamente
 * posição, velocidade, aceleração e massa, fornecendo a lógica necessária
 * para simular deslocações e resposta a forças ao longo do tempo.
 * </p>
 */
public abstract class Movement {

    protected PVector position;
    protected PVector velocity = new PVector(0, 0);
    protected PVector acceleration = new PVector();
    protected float mass = 1;

    /**
     * Cria uma nova instância com uma posição inicial definida.
     * <p>
     * É criada uma cópia do vetor recebido de forma a evitar dependências
     * externas sobre a referência original da posição.
     * </p>
     *
     * @param position vetor que representa a posição inicial
     */
    protected Movement(PVector position) {
        this.position = position.copy();
    }

    /**
     * Devolve a posição atual.
     * <p>
     * O vetor retornado representa a localização da entidade no espaço
     * de coordenadas do mundo.
     * </p>
     *
     * @return vetor da posição atual
     */
    public PVector getPosition() {
        return position;
    }

    /**
     * Atualiza diretamente a posição no mundo.
     * <p>
     * Esta atribuição não aplica qualquer cálculo físico intermédio,
     * sendo útil para reposicionamentos imediatos.
     * </p>
     *
     * @param position novo vetor de posição
     */
    public void setPosition(PVector position) {
        this.position = position;
    }

    /**
     * Devolve a velocidade atual.
     * <p>
     * Este vetor indica a variação da posição ao longo do tempo,
     * sendo utilizado nos cálculos de deslocação.
     * </p>
     *
     * @return vetor da velocidade atual
     */
    public PVector getVelocity() {
        return velocity;
    }

    /**
     * Atualiza diretamente a velocidade.
     * <p>
     * A nova velocidade substitui o valor anterior, influenciando
     * imediatamente o deslocamento futuro.
     * </p>
     *
     * @param velocity novo vetor de velocidade
     */
    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    /**
     * Devolve a aceleração atual.
     * <p>
     * Representa a acumulação de forças aplicadas durante o ciclo atual.
     * </p>
     *
     * @return vetor da aceleração atual
     */
    public PVector getAcceleration() {
        return acceleration;
    }

    /**
     * Atualiza diretamente a aceleração.
     * <p>
     * Este valor será utilizado no próximo cálculo de deslocação
     * antes de ser reiniciado.
     * </p>
     *
     * @param acceleration novo vetor de aceleração
     */
    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * Devolve o valor da massa.
     * <p>
     * A massa influencia a forma como forças externas afetam
     * a aceleração resultante.
     * </p>
     *
     * @return valor da massa
     */
    public float getMass() {
        return mass;
    }

    /**
     * Aplica uma força física ao sistema.
     * <p>
     * A força recebida é convertida em aceleração através da divisão
     * pela massa, sendo depois acumulada para o próximo cálculo.
     * </p>
     *
     * @param force vetor que representa a força aplicada
     */
    public void applyForce(PVector force) {
        acceleration.add(PVector.div(force, mass));
    }

    /**
     * Atualiza o estado físico com base no tempo decorrido.
     * <p>
     * A velocidade é atualizada a partir da aceleração acumulada
     * e a posição é ajustada com base na nova velocidade. No final,
     * a aceleração é reiniciada para o ciclo seguinte.
     * </p>
     *
     * @param dt intervalo de tempo considerado no cálculo
     */
    public void move(float dt) {
        velocity.add(PVector.mult(acceleration, dt));
        position.add(PVector.mult(velocity, dt));
        acceleration.mult(0);
    }
}