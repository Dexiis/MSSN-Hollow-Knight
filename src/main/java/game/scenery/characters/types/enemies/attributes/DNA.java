package game.scenery.characters.types.enemies.attributes;

import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.mobs.HuskHornhead;
import game.scenery.characters.types.enemies.mobs.Squit;
import game.scenery.components.flock.Flock;

/**
 * Define os atributos genéticos e físicos de uma entidade.
 * <p>
 * Esta estrutura armazena valores fundamentais como velocidade máxima, força máxima e parâmetros de visão,
 * que influenciam a conduta e o movimento das personagens no jogo. Cada tipo de entidade
 * possui o seu próprio conjunto de valores, definidos através de construtores específicos.
 * </p>
 */
public class DNA {

    protected float deltaPhiWander;
    protected float deltaTWander;
    protected float maxForce;
    protected float maxSpeed;
    protected float radiusWander;
    protected float visionAngle;
    protected float visionAttack;
    protected float visionAttackAngle;
    protected float visionDistance;

    /**
     * Inicializa o código genético para a entidade Squit.
     * <p>
     * Atribui valores aleatórios dentro de intervalos pré-definidos para a velocidade,
     * força e visão, adequados às características ágeis deste inimigo.
     * </p>
     *
     * @param me a instância de Squit a ser configurada.
     */
    public DNA(Squit me) {
        maxSpeed = random(125f, 175f);
        maxForce = random(125f, 175f);

        visionDistance = random(700f, 1000f);
        visionAngle = (float) Math.PI * 2f;

        visionAttack = 0.50f * visionDistance;
        visionAttackAngle = (float) Math.PI * 2f;

        deltaTWander = 2f;
        radiusWander = random(100f, 150f);
        deltaPhiWander = (float) Math.PI / 8;
    }

    /**
     * Inicializa o código genético para a entidade HuskHornhead.
     * <p>
     * Define os atributos físicos e sensoriais com base em intervalos aleatórios
     * específicos para este tipo de monstro, geralmente mais lentos que outros.
     * </p>
     *
     * @param me a instância de HuskHornhead a ser configurada.
     */
    public DNA(HuskHornhead me) {
        maxSpeed = random(70f, 80f);
        maxForce = random(70f, 80f);

        visionDistance = random(500f, 800f);
        visionAngle = (float) Math.PI * 2f;

        visionAttack = 0.40f * visionDistance;
        visionAttackAngle = (float) Math.PI * 2f;

        deltaTWander = 2f;
        radiusWander = random(100f, 150f);
        deltaPhiWander = (float) Math.PI / 8;
    }

    /**
     * Inicializa o código genético para a entidade FalseKnight.
     * <p>
     * Configura os atributos com valores fixos e elevados, refletindo o estatuto
     * de chefe e a potência superior desta personagem.
     * </p>
     *
     * @param me a instância de FalseKnight a ser configurada.
     */
    public DNA(FalseKnight me) {
        maxSpeed = 180f;
        maxForce = 180f;

        visionDistance = 2200f;
        visionAngle = (float) Math.PI * 2f;

        visionAttack = 0.15f * visionDistance;
        visionAttackAngle = (float) Math.PI * 2f;
    }

    /**
     * Inicializa o código genético para um comportamento de grupo (Flock).
     * <p>
     * Estabelece valores reduzidos e aleatórios para simular o movimento coletivo
     * de pequenas entidades, favorecendo a coesão em vez da potência individual.
     * </p>
     *
     * @param me a instância de Flock a ser configurada.
     */
    public DNA(Flock me) {
        maxSpeed = random(1f, 2f);
        maxForce = random(3f, 6f);

        visionDistance = random(0.5f, 2f);
        visionAngle = (float) Math.PI * 2f;

        visionAttack = 0.30f * visionDistance;
        visionAttackAngle = (float) Math.PI * 2f;
    }

    /**
     * Devolve a variação angular para o movimento de vagueio.
     * <p>
     * Indica o quanto o ângulo de direção pode mudar em cada atualização do
     * algoritmo de movimento aleatório, influenciando a "trepidação" da trajetória.
     * </p>
     *
     * @return a variação máxima em radianos.
     */
    public float getDeltaPhiWander() {
        return deltaPhiWander;
    }

    /**
     * Devolve o fator de projeção temporal para o vagueio.
     * <p>
     * Representa a distância à frente da entidade onde o círculo de vagueio é projetado,
     * afetando a suavidade da curva de viragem.
     * </p>
     *
     * @return o valor do deslocamento temporal (delta T).
     */
    public float getDeltaTWander() {
        return deltaTWander;
    }

    /**
     * Devolve a força máxima aplicada.
     * <p>
     * Este valor limita a magnitude do vetor de direção (steering) antes de ser
     * aplicado à aceleração da entidade.
     * </p>
     *
     * @return o valor da força máxima.
     */
    public float getMaxForce() {
        return maxForce;
    }

    /**
     * Devolve a velocidade limite.
     * <p>
     * Define o quão rápido a entidade se pode deslocar no espaço do jogo num determinado instante.
     * </p>
     *
     * @return o valor da velocidade máxima.
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Devolve o raio do círculo de vagueio.
     * <p>
     * Define a amplitude do círculo projetado usado para calcular o vetor de deslocamento aleatório.
     * </p>
     *
     * @return o raio do círculo de Wander.
     */
    public float getRadiusWander() {
        return radiusWander;
    }

    /**
     * Devolve a amplitude do campo de visão.
     * <p>
     * Determina o ângulo total que a entidade consegue observar ao seu redor.
     * </p>
     *
     * @return o ângulo de visão em radianos.
     */
    public float getVisionAngle() {
        return visionAngle;
    }

    /**
     * Devolve o alcance para iniciar um ataque.
     * <p>
     * Define a distância máxima a que um alvo pode estar para desencadear uma ofensiva.
     * </p>
     *
     * @return a distância de ataque.
     */
    public float getVisionAttack() {
        return visionAttack;
    }

    /**
     * Devolve o ângulo do cone de ataque.
     * <p>
     * Especifica a largura da área frontal onde o ataque é considerado eficaz.
     * </p>
     *
     * @return o ângulo de ataque em radianos.
     */
    public float getVisionAttackAngle() {
        return visionAttackAngle;
    }

    /**
     * Devolve o alcance visual máximo.
     * <p>
     * Define a distância radial até onde a entidade consegue detetar outros objetos ou personagens.
     * </p>
     *
     * @return o raio máximo de visão.
     */
    public float getVisionDistance() {
        return visionDistance;
    }

    /**
     * Atualiza a força máxima permitida.
     * <p>
     * Permite alterar dinamicamente a capacidade de manobra da entidade durante a execução.
     * </p>
     *
     * @param maxForce o novo valor limite de força.
     */
    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    /**
     * Atualiza a velocidade limite.
     * <p>
     * Modifica a rapidez máxima de deslocamento da personagem.
     * </p>
     *
     * @param maxSpeed o novo valor limite de velocidade.
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Produz um valor numérico aleatório num intervalo.
     * <p>
     * Gera um número de vírgula flutuante entre os limites mínimo e máximo fornecidos.
     * </p>
     *
     * @param min o limite inferior do intervalo.
     * @param max o limite superior do intervalo.
     * @return um valor aleatório contido no intervalo [min, max].
     */
    protected static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }
}