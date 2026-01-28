package game.scenery.characters.attributes;

import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.Squit;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.HuskHornhead;

/**
 * Define os atributos genéticos e físicos de uma entidade.
 * <p>
 * Esta classe armazena valores como velocidade máxima, força máxima e parâmetros de visão,
 * que influenciam o comportamento e movimento dos personagens no jogo.
 */
public class DNA {

    protected float maxSpeed;
    protected float maxForce;
    protected float visionDistance;
    protected float visionAttack;
    protected float visionAngle;
    protected float deltaTWander;
    protected float radiusWander;
    protected float deltaPhiWander;
    protected float visionAttackAngle;

    /**
     * Construtor da classe DNA.
     * <p>
     * Inicializa os atributos com valores aleatórios dentro de intervalos específicos,
     * dependendo do tipo concreto da entidade fornecida (ex: Squit, HuskHornhead).
     *
     * @param me A entidade à qual este DNA pertence.
     */
    public DNA(Entity me) { //TODO FAZER O DNA PARA CADA UM

        if (me instanceof Squit) {
            maxSpeed = random(100f, 200f);
            maxForce = random(150f, 250f);

            visionDistance = random(500f, 800f);
            visionAngle = (float) Math.PI * 2f;

            visionAttack = 0.60f * visionDistance;
            visionAttackAngle = (float) Math.PI * 2f;

            deltaTWander = 2f;
            radiusWander = random(100f, 150f);
            deltaPhiWander = (float) Math.PI / 8;

        } else if (me instanceof HuskHornhead) {

            maxSpeed = random(100f, 200f);
            maxForce = random(150f, 250f);

            visionDistance = random(500f, 800f);
            visionAngle = (float) Math.PI * 2f;

            visionAttack = 0.30f * visionDistance;
            visionAttackAngle = (float) Math.PI * 2f;

            deltaTWander = 2f;
            radiusWander = random(100f, 150f);
            deltaPhiWander = (float) Math.PI / 8;

        } else if (me instanceof FalseKnight) {

            maxSpeed = random(100f, 200f);
            maxForce = random(150f, 250f);

            visionDistance = random(500f, 800f);
            visionAngle = (float) Math.PI * 2f;

            visionAttack = 0.60f * visionDistance;
            visionAttackAngle = (float) Math.PI * 2f;

            deltaTWander = 2f;
            radiusWander = random(100f, 150f);
            deltaPhiWander = (float) Math.PI / 8;

        } else {

            maxSpeed = random(100f, 200f);
            maxForce = random(150f, 250f);

            visionDistance = random(500f, 800f);
            visionAngle = (float) Math.PI * 2f;

            visionAttack = 0.60f * visionDistance;
            visionAttackAngle = (float) Math.PI * 2f;

            deltaTWander = 2f;
            radiusWander = random(100f, 150f);
            deltaPhiWander = (float) Math.PI / 8;

        }
    }

    /**
     * Obtém a velocidade máxima que a entidade pode atingir.
     *
     * @return O valor da velocidade máxima.
     */
    public float getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Define a velocidade máxima da entidade.
     *
     * @param maxSpeed O novo valor da velocidade máxima.
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Obtém a força máxima que pode ser aplicada para mover a entidade.
     *
     * @return O valor da força máxima.
     */
    public float getMaxForce() {
        return maxForce;
    }

    /**
     * Define a força máxima da entidade.
     *
     * @param maxForce O novo valor da força máxima.
     */
    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    /**
     * Obtém a distância máxima de visão da entidade.
     *
     * @return O raio de visão.
     */
    public float getVisionDistance() {
        return visionDistance;
    }

    /**
     * Define a distância máxima de visão.
     *
     * @param visionDistance O novo raio de visão.
     */
    public void setVisionDistance(float visionDistance) {
        this.visionDistance = visionDistance;
    }

    /**
     * Obtém a distância de visão curta (zona de ataque ou interação próxima).
     *
     * @return O raio de visão próxima.
     */
    public float getVisionNearDistance() {
        return visionAttack;
    }

    /**
     * Define a distância de visão curta.
     *
     * @param visionNearDistance O novo raio de visão próxima.
     */
    public void setVisionNearDistance(float visionNearDistance) {
        this.visionAttack = visionNearDistance;
    }

    /**
     * Obtém o ângulo do campo de visão (Field of View).
     *
     * @return O ângulo em radianos.
     */
    public float getVisionAngle() {
        return visionAngle;
    }

    /**
     * Define o ângulo do campo de visão.
     *
     * @param visionAngle O novo ângulo em radianos.
     */
    public void setVisionAngle(float visionAngle) {
        this.visionAngle = visionAngle;
    }

    /**
     * Obtém o intervalo de tempo para atualização do movimento de vagueio (Wander).
     *
     * @return O valor de delta T.
     */
    public float getDeltaTWander() {
        return deltaTWander;
    }

    /**
     * Define o intervalo de tempo para o movimento de vagueio.
     *
     * @param deltaTWander O novo valor.
     */
    public void setDeltaTWander(float deltaTWander) {
        this.deltaTWander = deltaTWander;
    }

    /**
     * Obtém o raio do círculo projetado para o cálculo do movimento de vagueio.
     *
     * @return O raio do círculo de Wander.
     */
    public float getRadiusWander() {
        return radiusWander;
    }

    /**
     * Define o raio do círculo de vagueio.
     *
     * @param radiusWander O novo raio.
     */
    public void setRadiusWander(float radiusWander) {
        this.radiusWander = radiusWander;
    }

    /**
     * Obtém a variação máxima do ângulo de direção no movimento de vagueio.
     *
     * @return A variação em radianos.
     */
    public float getDeltaPhiWander() {
        return deltaPhiWander;
    }

    /**
     * Define a variação máxima do ângulo de direção para o vagueio.
     *
     * @param deltaPhiWander A nova variação em radianos.
     */
    public void setDeltaPhiWander(float deltaPhiWander) {
        this.deltaPhiWander = deltaPhiWander;
    }

    /**
     * Obtém o ângulo de visão para a zona de ataque.
     *
     * @return O ângulo em radianos.
     */
    public float getVisionNearAngle() {
        return visionAttackAngle;
    }

    /**
     * Define o ângulo de visão para a zona de ataque.
     *
     * @param visionNearAngle O novo ângulo em radianos.
     */
    public void setVisionNearAngle(float visionNearAngle) {
        this.visionAttackAngle = visionNearAngle;
    }

    /**
     * Gera um número decimal aleatório dentro de um intervalo especificado.
     *
     * @param min O valor mínimo do intervalo.
     * @param max O valor máximo do intervalo.
     * @return Um valor float aleatório entre min e max.
     */
    protected static float random(float min, float max) {
        return (float) (min + (max - min) * Math.random());
    }
}