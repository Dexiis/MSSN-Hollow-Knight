package game.scenery.characters.types.enemies.attributes.behaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Implementa o comportamento de "Vaguear" (Wander).
 * <p>
 * Este comportamento gera um movimento aleatório suave e natural, projetando um círculo
 * à frente da entidade e selecionando um alvo aleatório na sua circunferência.
 * Isso evita a vibração excessiva (jitter) que ocorreria com aleatoriedade pura.
 */
public class Wander extends Behaviour {

    /**
     * Construtor do comportamento Wander.
     *
     * @param weight O peso ou prioridade deste comportamento.
     */
    public Wander(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para o movimento de vagueio.
     * <p>
     * O algoritmo atualiza um ângulo aleatório (phiWander) com um pequeno deslocamento,
     * projeta um ponto à frente da entidade e calcula o vetor de direção para um ponto
     * na circunferência desse círculo projetado.
     *
     * @param me A entidade que está a executar o comportamento.
     * @return O vetor de velocidade desejada ou (0,0) se o comportamento não estiver ativo.
     */
    @Override
    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            float newPhiWander = me.getPhiWander();
            newPhiWander += (float) (2 * (Math.random() - 0.5) * me.getDna().getDeltaPhiWander());
            me.setPhiWander(newPhiWander);

            PVector center = me.getVelocity().copy();
            center.normalize().mult(me.getDna().getDeltaTWander());
            center.add(me.getPosition());

            PVector targetDisplacement = new PVector(me.getDna().getRadiusWander() * (float) Math.cos(newPhiWander), me.getDna().getRadiusWander() * (float) Math.sin(newPhiWander));
            PVector targetPosition = PVector.add(center, targetDisplacement);

            PVector desiredVelocity = PVector.sub(targetPosition, me.getPosition());

            desiredVelocity.setMag(me.getDna().getMaxSpeed());

            return desiredVelocity;
        }
        return new PVector(0, 0);
    }

    public PVector getDesiredVelocity(Flock me) {
        return new PVector(0, 0);
    }

    /**
     * Verifica as condições para ativar o vagueio.
     * <p>
     * O comportamento é ativado apenas quando a entidade <b>não</b> tem nenhuma entidade
     * no seu campo de visão distante (FarSight). Ou seja, vagueia quando não vê nada.
     *
     * @param me A entidade atual.
     * @return {@code true} se não houver alvos à vista, {@code false} caso contrário.
     */
    private boolean checkBehaviour(Enemy me) {
        return (me.getEye().getFarSight().isEmpty());
    }
}