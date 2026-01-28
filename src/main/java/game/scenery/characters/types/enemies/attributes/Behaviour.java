package game.scenery.characters.types.enemies.attributes;

import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Classe base abstrata para todos os comportamentos de direção (steering behaviors).
 * <p>
 * Implementa a interface {@link IBehaviour} e gere o atributo de "peso", que determina
 * a influência relativa deste comportamento quando combinado com outros num sistema de forças ponderadas.
 */
public abstract class Behaviour implements IBehaviour {

    protected float weight;

    /**
     * Construtor do comportamento.
     *
     * @param weight O peso inicial ou prioridade deste comportamento no cálculo final da direção.
     */
    public Behaviour(float weight) {
        this.weight = weight;
    }

    /**
     * Define o peso (influência) deste comportamento.
     *
     * @param weight O novo valor do peso.
     */
    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Obtém o peso atual deste comportamento.
     *
     * @return O valor do peso.
     */
    @Override
    public float getWeight() {
        return weight;
    }

    public abstract PVector getDesiredVelocity(Flock me);
}