package game.scenery.characters.types.enemies.attributes.bossBehaviours;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import processing.core.PVector;

/**
 * Representa o comportamento de fuga com salto do chefe.
 * <p>
 * Esta classe define o comportamento específico do chefe quando foge, utilizando saltos para se movimentar.
 * </p>
 */
public class JumpFlee extends Behaviour {

    /**
     * Inicializa o comportamento de fuga com salto.
     * <p>
     * Cria uma instância do comportamento JumpFlee com o peso especificado, herdando do comportamento base.
     * </p>
     *
     * @param weight o peso do comportamento
     */
    public JumpFlee(float weight) {
        super(weight);
    }

    /**
     * Calcula a velocidade desejada para o comportamento de fuga.
     * <p>
     * Esta operação determina a velocidade que o inimigo deve ter para executar o salto de fuga, considerando se está encurralado ou não.
     * </p>
     *
     * @param me o inimigo que executa o comportamento
     */
    public PVector getDesiredVelocity(Enemy me) {
        float g = 1280f;
        float theta = (float) Math.toRadians(50);
        float xDistance;
        float direction;
        if (checkBehaviour(me)) {
            if (((FalseKnight) me).isWalled()) {
                PVector center = new PVector(6000, me.getPosition().y);

                xDistance = center.x - me.getPosition().x;
                direction = xDistance / Math.abs(xDistance);
                xDistance = Math.abs(xDistance) - me.getHitbox().getWidth() / 2;

                calculateTrajectory(me, g, theta, xDistance, direction);
            } else {
                xDistance = me.getEye().getTarget().getPosition().x - me.getPosition().x;
                direction = -(xDistance / Math.abs(xDistance));

                if (xDistance != 0) {
                    float totalForce = (300.0f / Math.abs(xDistance)) * 1000.0f;

                    float xVelocity = totalForce * (float) Math.cos(theta) * direction;
                    float yVelocity = totalForce * (float) Math.sin(theta);

                    me.setVelocity(new PVector(xVelocity, yVelocity));
                }
            }
        }
        return new PVector(0, 0);
    }

    /**
     * Verifica se o comportamento deve ser ativado.
     * <p>
     * Avalia se o alvo está dentro do campo de visão distante do inimigo, determinando se o comportamento de fuga deve ser executado.
     * </p>
     *
     * @param me o inimigo que verifica o comportamento
     * @return verdadeiro se o comportamento deve ser ativado, falso caso contrário
     */
    public boolean checkBehaviour(Enemy me) {
        return me.getEye().getFarSight().contains(me.getEye().getTarget());
    }

}
