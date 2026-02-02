package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa um comportamento de ataque por salto para bosses.
 * <p>
 * Esta classe implementa um comportamento específico onde o inimigo calcula uma trajetória de salto
 * para atacar o alvo, utilizando física básica para determinar a velocidade desejada baseada na posição
 * do alvo e nas condições de visão.
 * </p>
 */
public class JumpAttack extends Behaviour {

    /**
     * Constrói um novo comportamento de ataque por salto com o peso especificado.
     * <p>
     * Este construtor inicializa o comportamento com um peso que influencia a probabilidade
     * de seleção deste comportamento em relação a outros comportamentos disponíveis.
     * </p>
     *
     * @param weight o peso associado a este comportamento
     */
    public JumpAttack(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para o ataque por salto.
     * <p>
     * Esta operação verifica se as condições para o comportamento são satisfeitas e, se sim,
     * calcula a trajetória de salto baseada na distância ao alvo, utilizando equações de física
     * com gravidade e ângulo fixo para determinar a velocidade inicial necessária. Caso contrário,
     * retorna uma velocidade zero.
     * </p>
     *
     * @param me o inimigo que executa este comportamento
     * @return a velocidade desejada como um vetor PVector
     */
    public PVector getDesiredVelocity(Enemy me) {
        if (checkBehaviour(me)) {
            float xDistance = me.getEye().getTarget().getPosition().x - me.getPosition().x;
            float direction = xDistance / Math.abs(xDistance);
            xDistance = Math.abs(xDistance) - me.getHitbox().getWidth() / 2;
            float g = 1280f;
            float theta = (float) Math.toRadians(30);

            super.calculateTrajectory(me, g, theta, xDistance, direction);
        }
        return new PVector(0, 0);
    }

    /**
     * Verifica se o comportamento de ataque por salto deve ser ativado.
     * <p>
     * Esta operação avalia as condições ambientais: o alvo deve estar dentro da visão distante
     * do inimigo, mas não dentro da visão próxima, indicando que está ao alcance ideal para
     * um ataque por salto sem estar demasiado perto.
     * </p>
     *
     * @param me o inimigo que executa este comportamento
     * @return true se o comportamento deve ser ativado, false caso contrário
     */
    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget()) && me.getEye().getNearSight().isEmpty();
    }

}