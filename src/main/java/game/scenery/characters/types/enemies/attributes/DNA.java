package game.scenery.characters.types.enemies.attributes;

import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.mobs.HuskHornhead;
import game.scenery.characters.types.enemies.mobs.Squit;
import game.scenery.components.flock.Flock;

/**
 * Define os atributos genéticos e físicos de uma entidade.
 * <p>
 * Esta classe armazena valores como velocidade máxima, força máxima e parâmetros de visão,
 * que influenciam o comportamento e movimento dos personagens no jogo. Cada tipo de entidade
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
     * Constrói um DNA específico para a entidade Squit.
     * <p>
     * Inicializa os atributos com valores aleatórios dentro de intervalos
     * pré-definidos adequados para este tipo de entidade.
     * </p>
     *
     * @param me a instância de Squit para a qual criar o DNA
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
     * Constrói um DNA específico para a entidade HuskHornhead.
     * <p>
     * Inicializa os atributos com valores aleatórios dentro de intervalos
     * pré-definidos adequados para este tipo de entidade.
     * </p>
     *
     * @param me a instância de HuskHornhead para a qual criar o DNA
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
     * Constrói um DNA específico para a entidade FalseKnight.
     * <p>
     * Inicializa os atributos com valores fixos adequados para este chefe.
     * </p>
     *
     * @param me a instância de FalseKnight para a qual criar o DNA
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
     * Constrói um DNA específico para entidades do tipo Flock (grupo).
     * <p>
     * Inicializa os atributos com valores aleatórios adequados para comportamentos
     * de grupo, tipicamente com valores menores do que entidades individuais.
     * </p>
     *
     * @param me a instância de Flock para a qual criar o DNA
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
     * Obtém a variação máxima do ângulo de direção no movimento de vagueio.
     *
     * @return a variação em radianos
     */
    public float getDeltaPhiWander() {
        return deltaPhiWander;
    }

    /**
     * Obtém o intervalo de tempo para atualização do movimento de vagueio (Wander).
     *
     * @return o valor de delta T
     */
    public float getDeltaTWander() {
        return deltaTWander;
    }

    /**
     * Obtém a força máxima que pode ser aplicada para mover a entidade.
     *
     * @return o valor da força máxima
     */
    public float getMaxForce() {
        return maxForce;
    }

    /**
     * Obtém a velocidade máxima que a entidade pode atingir.
     *
     * @return o valor da velocidade máxima
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Obtém o raio do círculo projetado para o cálculo do movimento de vagueio.
     *
     * @return o raio do círculo de Wander
     */
    public float getRadiusWander() {
        return radiusWander;
    }

    /**
     * Obtém o ângulo do campo de visão (Field of View).
     *
     * @return o ângulo em radianos
     */
    public float getVisionAngle() {
        return visionAngle;
    }

    /**
     * Obtém a distância de visão de ataque.
     *
     * @return a distância de ataque
     */
    public float getVisionAttack() {
        return visionAttack;
    }

    /**
     * Obtém o ângulo de visão de ataque.
     *
     * @return o ângulo de ataque em radianos
     */
    public float getVisionAttackAngle() {
        return visionAttackAngle;
    }

    /**
     * Obtém a distância máxima de visão da entidade.
     *
     * @return o raio de visão
     */
    public float getVisionDistance() {
        return visionDistance;
    }

    /**
     * Obtém o ângulo de visão para a zona de ataque.
     *
     * @return o ângulo em radianos
     */
    public float getVisionNearAngle() {
        return visionAttackAngle;
    }

    /**
     * Obtém a distância de visão curta (zona de ataque ou interação próxima).
     *
     * @return o raio de visão próxima
     */
    public float getVisionNearDistance() {
        return visionAttack;
    }

    /**
     * Define a variação máxima do ângulo de direção para o vagueio.
     *
     * @param deltaPhiWander a nova variação em radianos
     */
    public void setDeltaPhiWander(float deltaPhiWander) {
        this.deltaPhiWander = deltaPhiWander;
    }

    /**
     * Define o intervalo de tempo para o movimento de vagueio.
     *
     * @param deltaTWander o novo valor de delta T
     */
    public void setDeltaTWander(float deltaTWander) {
        this.deltaTWander = deltaTWander;
    }

    /**
     * Define a força máxima da entidade.
     *
     * @param maxForce o novo valor da força máxima
     */
    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    /**
     * Define a velocidade máxima da entidade.
     *
     * @param maxSpeed o novo valor da velocidade máxima
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Define o raio do círculo de vagueio.
     *
     * @param radiusWander o novo raio
     */
    public void setRadiusWander(float radiusWander) {
        this.radiusWander = radiusWander;
    }

    /**
     * Define o ângulo do campo de visão.
     *
     * @param visionAngle o novo ângulo em radianos
     */
    public void setVisionAngle(float visionAngle) {
        this.visionAngle = visionAngle;
    }

    /**
     * Define a distância de visão de ataque.
     *
     * @param visionAttack a nova distância de ataque
     */
    public void setVisionAttack(float visionAttack) {
        this.visionAttack = visionAttack;
    }

    /**
     * Define o ângulo de visão de ataque.
     *
     * @param visionAttackAngle o novo ângulo de ataque em radianos
     */
    public void setVisionAttackAngle(float visionAttackAngle) {
        this.visionAttackAngle = visionAttackAngle;
    }

    /**
     * Define a distância máxima de visão.
     *
     * @param visionDistance o novo raio de visão
     */
    public void setVisionDistance(float visionDistance) {
        this.visionDistance = visionDistance;
    }

    /**
     * Define o ângulo de visão para a zona de ataque.
     *
     * @param visionNearAngle o novo ângulo em radianos
     */
    public void setVisionNearAngle(float visionNearAngle) {
        this.visionAttackAngle = visionNearAngle;
    }

    /**
     * Define a distância de visão curta.
     *
     * @param visionNearDistance o novo raio de visão próxima
     */
    public void setVisionNearDistance(float visionNearDistance) {
        this.visionAttack = visionNearDistance;
    }

    /**
     * Gera um número decimal aleatório dentro de um intervalo especificado.
     *
     * @param min o valor mínimo do intervalo
     * @param max o valor máximo do intervalo
     * @return um valor float aleatório entre min e max
     */
    protected static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }
}