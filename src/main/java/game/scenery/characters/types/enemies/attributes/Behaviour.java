package game.scenery.characters.types.enemies.attributes;

import game.scenery.characters.types.Enemy;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Classe abstrata que representa um comportamento de movimento ou decisão.
 * <p>
 * Define a estrutura base para implementar diferentes padrões de comportamento
 * de personagens no jogo, tais como vaguear, perseguir, atacar, entre outros.
 * Cada comportamento possui um peso que determina a sua influência no cálculo
 * final da direção de movimento.
 * </p>
 */
public abstract class Behaviour {

    protected float weight;

    /**
     * Constrói um novo comportamento com o peso especificado.
     *
     * @param weight o peso inicial ou prioridade deste comportamento no cálculo final da direção
     */
    public Behaviour(float weight) {
        this.weight = weight;
    }

    /**
     * Define o peso (influência) deste comportamento.
     *
     * @param weight o novo valor do peso
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Obtém o peso atual deste comportamento.
     *
     * @return o valor do peso
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Calcula a velocidade desejada com base no comportamento específico para um inimigo.
     * <p>
     * Este é um dos principais pontos de extensão da classe. As subclasses devem
     * sobrescrever esta função para implementar a lógica específica do comportamento.
     * </p>
     *
     * @param me o inimigo para o qual calcular a velocidade desejada
     * @return o vetor de velocidade desejada, ou {@code null} se não aplicável
     */
    public PVector getDesiredVelocity(Enemy me) {
        return null;
    }

    /**
     * Calcula a velocidade desejada com base no comportamento específico para um grupo (flock).
     * <p>
     * Este é um dos principais pontos de extensão da classe. As subclasses podem
     * sobrescrever esta função para implementar comportamentos de grupo.
     * </p>
     *
     * @param me o grupo para o qual calcular a velocidade desejada
     * @return o vetor de velocidade desejada, ou {@code null} se não aplicável
     */
    public PVector getDesiredVelocity(Flock me) {
        return null;
    }

}