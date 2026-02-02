package game.scenery.components.flock.flockbehaviours;

import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Define o comportamento de coesão em sistemas de bando.
 * <p>
 * Este comportamento conduz a entidade na direção do centro geométrico
 * das entidades vizinhas, promovendo a proximidade entre os elementos
 * do grupo e evitando a sua dispersão.
 * </p>
 */
public class Cohesion extends Behaviour {

    /**
     * Cria uma nova instância do comportamento de coesão.
     * <p>
     * O peso recebido determina o grau de influência desta regra
     * no cálculo final da deslocação da entidade.
     * </p>
     *
     * @param weight peso associado à influência da coesão
     */
    public Cohesion(float weight) {
        super(weight);
    }

    /**
     * Determina a direção em relação ao centro do grupo vizinho.
     * <p>
     * Soma as posições das entidades visíveis no alcance distante,
     * calcula a posição média e devolve o vetor que aponta da posição
     * atual da entidade para esse ponto central.
     * </p>
     *
     * @param me entidade do bando que está a ser avaliada
     * @return vetor orientado para o centro geométrico do grupo vizinho
     */
    public PVector getDesiredVelocity(Flock me) {
        PVector target = me.getPosition().copy();
        for (Flock f : me.getEye().getFarSight())
            target.add(f.getPosition());
        target.div(me.getEye().getFarSight().size() + 1);

        return me.getToroidalDistanceVector(target);
    }
}