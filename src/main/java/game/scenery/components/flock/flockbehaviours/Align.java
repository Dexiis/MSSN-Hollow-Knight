package game.scenery.components.flock.flockbehaviours;

import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Define o comportamento de alinhamento em sistemas de bando.
 * <p>
 * Este comportamento orienta uma entidade a ajustar a sua deslocação
 * de acordo com a direção e velocidade médias das entidades vizinhas,
 * promovendo um movimento coletivo uniforme e sincronizado.
 * </p>
 */
public class Align extends Behaviour {

    /**
     * Cria uma nova instância do comportamento de alinhamento.
     * <p>
     * O valor de peso recebido determina a influência relativa deste
     * comportamento no cálculo final da deslocação da entidade.
     * </p>
     *
     * @param weight peso associado à influência do alinhamento
     */
    public Align(float weight) {
        super(weight);
    }

    /**
     * Determina a velocidade pretendida com base nos vizinhos próximos.
     * <p>
     * Soma as velocidades das entidades visíveis no alcance distante e
     * calcula a média, incluindo a própria entidade, de forma a obter
     * a direção predominante do movimento do grupo.
     * </p>
     *
     * @param me entidade do bando que está a ser avaliada
     * @return vetor representativo da velocidade média dos vizinhos
     */
    public PVector getDesiredVelocity(Flock me) {
        PVector desiredVelocity = me.getVelocity().copy();
        for (Flock character : me.getEye().getFarSight())
            desiredVelocity.add(character.getVelocity());

        return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
    }
}