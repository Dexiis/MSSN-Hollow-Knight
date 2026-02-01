package game.scenery.characters.types.enemies.attributes;

import game.scenery.characters.types.Enemy;
import game.scenery.components.flock.Flock;
import processing.core.PVector;

/**
 * Define a base abstrata para ações de movimento ou decisão.
 * <p>
 * Estabelece a estrutura fundamental para implementar diferentes padrões de conduta
 * nas personagens do jogo, tais como vaguear, perseguir ou atacar.
 * Cada ação possui um peso associado que determina a sua influência no cálculo
 * final da direção a tomar.
 * </p>
 */
public abstract class Behaviour {

    protected float weight;

    /**
     * Inicializa uma nova instância de comportamento com a ponderação indicada.
     * <p>
     * Atribui o valor inicial à variável de peso, definindo a prioridade desta
     * ação no cálculo da resultante final.
     * </p>
     *
     * @param weight o peso inicial ou prioridade desta ação.
     */
    public Behaviour(float weight) {
        this.weight = weight;
    }

    /**
     * Atualiza o valor da influência deste comportamento.
     * <p>
     * Permite ajustar dinamicamente a importância desta ação em relação às outras
     * durante a execução do programa.
     * </p>
     *
     * @param weight o novo valor a atribuir ao peso.
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Devolve o nível de influência atual.
     * <p>
     * Acede ao valor armazenado que representa a prioridade desta conduta.
     * </p>
     *
     * @return o valor numérico do peso.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Calcula a velocidade pretendida para um inimigo específico.
     * <p>
     * Este é um dos principais pontos de extensão da classe. As subclasses devem
     * sobrepor esta função para implementar a lógica concreta da ação a realizar.
     * </p>
     *
     * @param me o agente inimigo que executa a ação.
     * @return o vetor de velocidade desejada, ou {@code null} se a lógica não for aplicável.
     */
    public PVector getDesiredVelocity(Enemy me) {
        return null;
    }

    /**
     * Determina a velocidade pretendida para um enxame (Flock).
     * <p>
     * Funciona como ponto de extensão para lógicas de grupo. As subclasses podem
     * sobrepor esta operação para ditar regras coletivas.
     * </p>
     *
     * @param me o grupo de entidades alvo da ação.
     * @return o vetor de velocidade desejada, ou {@code null} se a lógica não for aplicável.
     */
    public PVector getDesiredVelocity(Flock me) {
        return null;
    }

    /**
     * Computa e aplica uma trajetória balística à entidade.
     * <p>
     * Realiza o cálculo físico necessário para lançar a entidade, baseando-se na gravidade,
     * ângulo e distância. O resultado é aplicado diretamente na velocidade da entidade fornecida.
     * </p>
     *
     * @param me a entidade que sofrerá a alteração de velocidade.
     * @param g o valor da aceleração gravitacional a considerar.
     * @param theta o ângulo de lançamento em radianos.
     * @param xDistance a distância horizontal até ao alvo.
     * @param direction a direção do movimento (1 ou -1).
     */
    protected void calculateTrajectory(Enemy me, float g, float theta, float xDistance, float direction) {
        float velocity = (float) Math.sqrt((Math.abs(xDistance) * g) / Math.sin(2 * theta));

        float xVelocity = velocity * (float) Math.cos(theta) * direction;
        float yVelocity = velocity * (float) Math.sin(theta);

        me.setVelocity(new PVector(xVelocity, Math.abs(yVelocity)));
    }

}