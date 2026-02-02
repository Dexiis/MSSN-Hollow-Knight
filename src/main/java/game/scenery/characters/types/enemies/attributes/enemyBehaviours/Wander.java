package game.scenery.characters.types.enemies.attributes.enemyBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.Mob;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Implementa a conduta de deambulação (Wander).
 * <p>
 * Esta ação gera um movimento aleatório suave e natural, projetando um círculo
 * à frente da entidade e selecionando um alvo aleatório na sua circunferência.
 * Isto evita a vibração excessiva (jitter) que ocorreria com aleatoriedade pura.
 * </p>
 */
public class Wander extends Behaviour {

    /**
     * Instancia uma nova conduta de vagueio com o peso especificado.
     * <p>
     * Atribui a prioridade fornecida à superclasse para influenciar a decisão da entidade
     * ao escolher entre diferentes ações.
     * </p>
     *
     * @param weight o peso ou prioridade desta ação.
     */
    public Wander(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para o movimento de vagueio.
     * <p>
     * O algoritmo atualiza um ângulo aleatório (phiWander) com um pequeno deslocamento,
     * projeta um ponto à frente da entidade e calcula o vetor de direção para um ponto
     * na circunferência desse círculo projetado, criando um trajeto fluido.
     * </p>
     *
     * @param me a entidade que está a executar a ação.
     * @return o vetor de velocidade desejada ou (0,0) se a conduta não estiver ativa.
     */
    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            Mob mob = ((Mob) me);
            float newPhiWander = mob.getPhiWander();
            newPhiWander += (float) (2 * (Math.random() - 0.5) * mob.getDna().getDeltaPhiWander());
            mob.setPhiWander(newPhiWander);

            PVector center = mob.getVelocity().copy();
            center.normalize().mult(mob.getDna().getDeltaTWander());
            center.add(mob.getPosition());

            PVector targetDisplacement = new PVector(mob.getDna().getRadiusWander() * (float) Math.cos(newPhiWander), mob.getDna().getRadiusWander() * (float) Math.sin(newPhiWander));
            PVector targetPosition = PVector.add(center, targetDisplacement);

            PVector desiredVelocity = PVector.sub(targetPosition, mob.getPosition());

            desiredVelocity.setMag(mob.getDna().getMaxSpeed());

            return desiredVelocity;
        }
        return new PVector(0, 0);
    }

    /**
     * Avalia as condições para ativar o vagueio.
     * <p>
     * Esta lógica é ativada apenas quando a entidade <b>não</b> deteta nenhuma outra entidade
     * no seu campo de visão distante (FarSight). Ou seja, vagueia quando não vê nada.
     * </p>
     *
     * @param me a entidade atual.
     * @return {@code true} se não houver alvos à vista, {@code false} caso contrário.
     */
    private boolean checkBehaviour(Enemy me) {
        return (me.getEye().getFarSight().isEmpty());
    }

}