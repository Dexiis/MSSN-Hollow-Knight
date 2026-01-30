package game.scenery.characters.types.enemies.attributes;

import game.core.SubPlot;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.Entity;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o sensor visual de uma entidade ("Olho").
 * <p>
 * Esta classe é responsável pela perceção do ambiente, detetando outras entidades
 * que entrem nos campos de visão (cones) definidos pelos atributos genéticos (DNA)
 * da entidade proprietária. Gere listas distintas para visão ao longe e visão de
 * perto (alcance de ataque).
 * </p>
 */
public class Eye {
    private List<Entity> allTrackingBodies = new ArrayList<>();
    private List<Entity> farSight = new ArrayList<>();
    private final Enemy me;
    private List<Entity> nearSight = new ArrayList<>();
    protected Entity target;

    /**
     * Constrói um novo sensor visual para uma entidade.
     * <p>
     * Inicializa a referência para a entidade proprietária e define um alvo
     * inicial para rastreio.
     * </p>
     *
     * @param me     a entidade a quem este olho pertence
     * @param target o alvo inicial a ser rastreado
     */
    public Eye(Enemy me, Entity target) {
        this.me = me;
        this.target = target;
        addTarget(target);
    }

    /**
     * Obtém a lista de todas as entidades que estão configuradas para serem rastreadas.
     *
     * @return lista de entidades rastreáveis
     */
    public List<Entity> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    /**
     * Obtém a lista de entidades detetadas dentro do campo de visão distante.
     * <p>
     * Esta lista é atualizada a cada chamada de {@code look()}.
     * </p>
     *
     * @return lista de entidades visíveis ao longe
     */
    public List<Entity> getFarSight() {
        return farSight;
    }

    /**
     * Obtém a lista de entidades detetadas dentro do campo de visão próximo.
     * <p>
     * Representa a zona de ataque. Esta lista é atualizada a cada chamada de {@code look()}.
     * </p>
     *
     * @return lista de entidades visíveis ao perto
     */
    public List<Entity> getNearSight() {
        return nearSight;
    }

    /**
     * Obtém a referência para o alvo atual.
     *
     * @return a entidade alvo
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Define a lista completa de todas as entidades que este olho deve monitorizar.
     *
     * @param allTrackingBodies lista de entidades rastreáveis
     */
    public void setAllTrackingBodies(List<Entity> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    /**
     * Define o alvo principal que a entidade deve tentar localizar ou perseguir.
     *
     * @param target a entidade alvo
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Adiciona uma nova entidade específica à lista de corpos a rastrear.
     *
     * @param target a entidade a adicionar
     */
    public void addTarget(Entity target) {
        this.allTrackingBodies.add(target);
    }

    /**
     * Executa o processo de perceção visual.
     * <p>
     * Limpa as memórias visuais anteriores e itera sobre todas as entidades rastreáveis.
     * Para cada uma, verifica se se encontra dentro dos limites de visão (longe ou perto)
     * definidos no DNA e atualiza as listas correspondentes.
     * </p>
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
     * <p>
     * Utiliza a distância e ângulo de visão definidos no DNA da entidade.
     * </p>
     *
     * @param t o vetor de posição a verificar
     * @return {@code true} se estiver visível, {@code false} caso contrário
     */
    private boolean farSight(PVector t) {
        return inSight(t, me.getDna().visionDistance, me.getDna().visionAngle);
    }

    /**
     * Cálculo matemático auxiliar para determinar a visibilidade.
     * <p>
     * Calcula o vetor relativo ao alvo, a magnitude da distância e o ângulo em relação
     * ao vetor de velocidade atual da entidade para validar se o ponto está dentro
     * do cone cónico definido.
     * </p>
     *
     * @param t           a posição do alvo
     * @param maxDistance a distância máxima de perceção
     * @param maxAngle    o ângulo máximo de abertura da visão (metade do cone)
     * @return {@code true} se o ponto cumprir os critérios geométricos
     */
    private boolean inSight(PVector t, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(t, me.getPosition());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVelocity());
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
    }

    /**
     * Verifica se uma posição específica está dentro do alcance de visão curta.
     * <p>
     * Representa a zona de ataque. Utiliza a distância e ângulo de ataque
     * definidos no DNA da entidade.
     * </p>
     *
     * @param t o vetor de posição a verificar
     * @return {@code true} se estiver ao alcance, {@code false} caso contrário
     */
    private boolean nearSight(PVector t) {
        return inSight(t, me.getDna().visionAttack, me.getDna().visionAttackAngle);
    }

    /**
     * Renderiza os cones de visão no ecrã para fins de depuração (debug).
     * <p>
     * Desenha o cone de visão distante a vermelho e a zona de ataque a magenta.
     * Realiza transformações matriciais para alinhar o desenho com a posição
     * e rotação da entidade.
     * </p>
     *
     * @param p   o contexto gráfico do Processing
     * @param plt o objeto SubPlot para conversão de coordenadas
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

        float[] dd1 = plt.getDimInPixel(me.getDna().visionDistance, me.getDna().visionDistance);
        p.rotate(me.getDna().visionAngle);
        p.line(0, 0, dd1[0], 0);
        p.rotate(-2 * me.getDna().visionAngle);
        p.line(0, 0, dd1[0], 0);
        p.rotate(me.getDna().visionAngle);
        p.arc(0, 0, 2 * dd1[0], 2 * dd1[0], -me.getDna().visionAngle, me.getDna().visionAngle);

        float[] dd2 = plt.getDimInPixel(me.getDna().visionAttack, me.getDna().visionAttack);
        p.stroke(255, 0, 255);
        if (me.getDna().visionAttackAngle >= Math.PI) {
            p.circle(0, 0, 2 * dd2[0]);
        } else {
            p.rotate(me.getDna().visionAttackAngle);
            p.line(0, 0, dd2[0], 0);
            p.rotate(-2 * me.getDna().visionAttackAngle);
            p.line(0, 0, dd2[0], 0);
            p.rotate(me.getDna().visionAttackAngle);
            p.arc(0, 0, 2 * dd2[0], 2 * dd2[0], -me.getDna().visionAttackAngle, me.getDna().visionAttackAngle);
        }
        p.popMatrix();
        p.popStyle();
    }
}