package game.scenery.characters.types.enemies.attributes.enemyBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa o comportamento de ataque de um inimigo.
 * <p>
 * Este comportamento é ativado quando o alvo entra na zona de visão próxima. O inimigo move-se diretamente para a última posição conhecida do alvo com uma velocidade de ataque específica.
 * </p>
 */
public class Attack extends Behaviour {
    private PVector targetPosition;
    private PVector chargeDirection;

    /**
     * Inicializa um novo comportamento de ataque com o peso especificado.
     * <p>
     * Cria uma instância do comportamento Attack com o peso fornecido, que determina a prioridade no sistema de decisão de movimento.
     * </p>
     *
     * @param weight o peso ou prioridade deste comportamento
     */
    public Attack(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para executar o ataque.
     * <p>
     * Se existir uma direção de carga definida, retorna essa direção; caso contrário, retorna um vetor vazio.
     * </p>
     *
     * @param me o inimigo que está a executar o comportamento
     * @return o vetor de velocidade de ataque ou um vetor vazio
     */
    @Override
    public PVector getDesiredVelocity(Enemy me) {
        if (chargeDirection != null) return chargeDirection;
        return new PVector();
    }

    /**
     * Verifica se as condições para atacar estão reunidas.
     * <p>
     * O ataque é considerado possível se o alvo estiver contido na lista de entidades da visão próxima.
     * </p>
     *
     * @param me o inimigo atual
     * @return verdadeiro se o alvo estiver ao alcance, falso caso contrário
     */
    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getNearSight().contains(me.getEye().getTarget());
    }

    /**
     * Guarda a posição do alvo.
     * <p>
     * Armazena a posição atual do alvo e calcula a velocidade de carga correspondente.
     * </p>
     *
     * @param me o inimigo que guarda a posição
     */
    public void saveTargetPosition(Enemy me) {
        this.targetPosition = me.getEye().getTarget().getPosition();
        saveVelocity(me);
    }

    /**
     * Calcula e guarda a direção de carga.
     * <p>
     * Determina a direção normalizada da carga multiplicada pela velocidade máxima do inimigo.
     * </p>
     *
     * @param me o inimigo para o qual a velocidade é calculada
     */
    private void saveVelocity(Enemy me) {
        this.chargeDirection = PVector.sub(targetPosition, me.getPosition()).normalize().mult(me.getDna().getMaxSpeed());
    }

}