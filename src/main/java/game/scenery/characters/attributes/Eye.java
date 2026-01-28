package game.scenery.characters.attributes;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o sensor visual de uma entidade ("Olho").
 * <p>
 * Esta classe é responsável pela perceção do ambiente, detetando outras entidades
 * que entrem nos campos de visão (cones) definidos pelos atributos genéticos (DNA) da entidade proprietária.
 * Gere listas distintas para visão ao longe e visão de perto (alcance de ataque).
 */
public class Eye {
    private List<Entity> allTrackingBodies = new ArrayList<>();
    private List<Entity> farSight = new ArrayList<>();
    private List<Entity> nearSight = new ArrayList<>();
    private final Enemy me;
    protected Entity target;

    /**
     * Construtor do sensor visual.
     * Inicializa a referência para a entidade proprietária e define um alvo inicial para rastreio.
     *
     * @param me     A entidade a quem este olho pertence.
     * @param target O alvo inicial a ser rastreado.
     */
    public Eye(Enemy me, Entity target) {
        this.me = me;
        this.target = target;
        addTarget(target);
    }

    /**
     * Define o alvo principal que a entidade deve tentar localizar ou perseguir.
     *
     * @param target A entidade alvo.
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Obtém a referência para o alvo atual.
     *
     * @return A entidade alvo.
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Define a lista completa de todas as entidades que este olho deve tentar monitorizar.
     *
     * @param allTrackingBodies Lista de entidades rastreáveis.
     */
    public void setAllTrackingBodies(List<Entity> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    /**
     * Obtém a lista de todas as entidades que estão configuradas para serem rastreadas.
     *
     * @return Lista de entidades.
     */
    public List<Entity> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    /**
     * Adiciona uma nova entidade específica à lista de corpos a rastrear.
     *
     * @param target A entidade a adicionar.
     */
    public void addTarget(Entity target) {
        this.allTrackingBodies.add(target);
    }

    /**
     * Obtém a lista de entidades detetadas dentro do campo de visão distante no último ciclo de atualização.
     *
     * @return Lista de entidades visíveis ao longe.
     */
    public List<Entity> getFarSight() {
        return farSight;
    }

    /**
     * Obtém a lista de entidades detetadas dentro do campo de visão próximo (zona de ataque) no último ciclo.
     *
     * @return Lista de entidades visíveis ao perto.
     */
    public List<Entity> getNearSight() {
        return nearSight;
    }

    /**
     * Executa o processo de perceção visual.
     * <p>
     * Limpa as memórias visuais anteriores e itera sobre todas as entidades rastreáveis.
     * Para cada uma, verifica se se encontra dentro dos limites de visão (longe ou perto) definidos no DNA
     * e atualiza as listas correspondentes.
     */
    public void look() {
        farSight = new ArrayList<Entity>();
        nearSight = new ArrayList<Entity>();
        for (Entity character : allTrackingBodies) {
            if (farSight(character.getPosition())) farSight.add(character);
            if (nearSight(character.getPosition())) nearSight.add(character);
        }
    }

    /**
     * Verifica se uma posição específica está dentro do alcance de visão distante.
     * Utiliza a distância e ângulo de visão definidos no DNA da entidade.
     *
     * @param t O vetor de posição a verificar.
     * @return {@code true} se estiver visível, {@code false} caso contrário.
     */
    private boolean farSight(PVector t) {
        return inSight(t, me.getDNA().visionDistance, me.getDNA().visionAngle);
    }

    /**
     * Verifica se uma posição específica está dentro do alcance de visão curta (zona de ataque).
     * Utiliza a distância e ângulo de ataque definidos no DNA da entidade.
     *
     * @param t O vetor de posição a verificar.
     * @return {@code true} se estiver ao alcance, {@code false} caso contrário.
     */
    private boolean nearSight(PVector t) {
        return inSight(t, me.getDNA().visionAttack, me.getDNA().visionAttackAngle);
    }

    /**
     * Cálculo matemático auxiliar para determinar a visibilidade.
     * <p>
     * Calcula o vetor relativo ao alvo, a magnitude da distância e o ângulo em relação
     * ao vetor de velocidade atual da entidade para validar se o ponto está dentro do cone cónico definido.
     *
     * @param t           A posição do alvo.
     * @param maxDistance A distância máxima de perceção.
     * @param maxAngle    O ângulo máximo de abertura da visão (metade do cone).
     * @return {@code true} se o ponto cumprir os critérios geométricos.
     */
    private boolean inSight(PVector t, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(t, me.getPosition());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVelocity());
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
    }

    /**
     * Renderiza os cones de visão no ecrã para fins de depuração (debug).
     * <p>
     * Desenha o cone de visão distante a vermelho e a zona de ataque a magenta.
     * Realiza transformações matriciais para alinhar o desenho com a posição e rotação da entidade.
     *
     * @param p   O contexto gráfico do Processing.
     * @param plt O objeto SubPlot para conversão de coordenadas.
     */
    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.pushMatrix();

        float[] pp = plt.getPixelCoord(me.getPosition().x, me.getPosition().y);
        p.translate(pp[0], pp[1]);

        p.rotate(-me.getVelocity().heading());
        p.noFill();
        p.stroke(255, 0, 0);
        p.strokeWeight(3);

        float[] dd1 = plt.getDimInPixel(me.getDNA().visionDistance, me.getDNA().visionDistance);
        p.rotate(me.getDNA().visionAngle);
        p.line(0, 0, dd1[0], 0);
        p.rotate(-2 * me.getDNA().visionAngle);
        p.line(0, 0, dd1[0], 0);
        p.rotate(me.getDNA().visionAngle);
        p.arc(0, 0, 2 * dd1[0], 2 * dd1[0], -me.getDNA().visionAngle, me.getDNA().visionAngle);

        float[] dd2 = plt.getDimInPixel(me.getDNA().visionAttack, me.getDNA().visionAttack);
        p.stroke(255, 0, 255);
        if (me.getDNA().visionAttackAngle >= Math.PI) {
            p.circle(0, 0, 2 * dd2[0]);
        } else {
            p.rotate(me.getDNA().visionAttackAngle);
            p.line(0, 0, dd2[0], 0);
            p.rotate(-2 * me.getDNA().visionAttackAngle);
            p.line(0, 0, dd2[0], 0);
            p.rotate(me.getDNA().visionAttackAngle);
            p.arc(0, 0, 2 * dd2[0], 2 * dd2[0], -me.getDNA().visionAttackAngle, me.getDNA().visionAttackAngle);
        }
        p.popMatrix();
        p.popStyle();
    }
}