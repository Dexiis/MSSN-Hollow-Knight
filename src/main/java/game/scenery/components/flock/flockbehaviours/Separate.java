package game.scenery.components.flock.flockbehaviours;

import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Define o comportamento de separação em sistemas de bando.
 * <p>
 * Este comportamento introduz uma força de afastamento entre entidades
 * próximas, prevenindo colisões e reduzindo a concentração excessiva
 * de elementos numa mesma zona.
 * </p>
 */
public class Separate extends Behaviour {

    /**
     * Cria uma nova instância do comportamento de separação.
     * <p>
     * O peso recebido controla a intensidade com que a força de repulsão
     * influencia a deslocação final da entidade.
     * </p>
     *
     * @param weight peso associado à influência da separação
     */
    public Separate(float weight) {
        super(weight);
    }

    /**
     * Determina o vetor de afastamento relativamente aos vizinhos imediatos.
     * <p>
     * Para cada entidade visível no alcance próximo, é calculado um vetor
     * oposto à sua posição, cuja intensidade aumenta à medida que a distância
     * diminui, promovendo a dispersão local.
     * </p>
     *
     * @param me entidade do bando que está a ser avaliada
     * @return vetor representativo da força de repulsão aplicada
     */
    public PVector getDesiredVelocity(Flock me) {
        PVector desiredVelocity = new PVector();
        for (Flock f : me.getEye().getNearSight()) {
            PVector direction = me.getToroidalDistanceVector(f.getPosition()).mult(-1);
            float dir = direction.mag();
            direction.div(dir * dir);
            desiredVelocity.add(direction);
        }

        return desiredVelocity;
    }
}