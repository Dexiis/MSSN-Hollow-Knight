package game.scenery.characters.types.enemies.attributes;

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
     * Inicializa um novo sensor visual para a entidade.
     * <p>
     * Guarda a referência para a entidade proprietária e define um alvo
     * inicial para iniciar o rastreio imediatamente.
     * </p>
     *
     * @param me     a entidade a quem este olho pertence.
     * @param target o alvo inicial a ser rastreado.
     */
    public Eye(Enemy me, Entity target) {
        this.me = me;
        this.target = target;
        addTarget(target);
    }

    /**
     * Devolve a lista de entidades detetadas no campo de visão distante.
     * <p>
     * A coleção retornada contém os elementos identificados durante a última
     * execução da operação de perceção.
     * </p>
     *
     * @return a lista de entidades visíveis ao longe.
     */
    public List<Entity> getFarSight() {
        return farSight;
    }

    /**
     * Devolve a lista de entidades detetadas no campo de visão próximo.
     * <p>
     * Corresponde à zona de ataque. A coleção é atualizada sempre que a
     * perceção visual é recalculada.
     * </p>
     *
     * @return a lista de entidades visíveis ao perto.
     */
    public List<Entity> getNearSight() {
        return nearSight;
    }

    /**
     * Devolve a referência para o alvo principal atual.
     * <p>
     * Permite aceder à entidade que está a ser considerada como foco
     * principal das atenções deste sensor.
     * </p>
     *
     * @return a entidade alvo.
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Adiciona uma nova entidade à lista de corpos a rastrear.
     * <p>
     * Insere o objeto fornecido no conjunto de elementos que serão testados
     * quanto à visibilidade nas próximas atualizações.
     * </p>
     *
     * @param target a entidade a adicionar ao rastreio.
     */
    public void addTarget(Entity target) {
        this.allTrackingBodies.add(target);
    }

    /**
     * Executa o processo de perceção visual.
     * <p>
     * Reinicia as memórias visuais e itera sobre as entidades rastreáveis.
     * Para cada uma, verifica se se encontra dentro dos limites geométricos
     * (longe ou perto) definidos no DNA e atualiza as respetivas listas.
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
     * Verifica se uma posição se encontra no alcance de visão distante.
     * <p>
     * Utiliza a distância máxima e o ângulo de visão definidos no código genético
     * da entidade para validar a visibilidade.
     * </p>
     *
     * @param t o vetor de posição a analisar.
     * @return {@code true} se o ponto estiver visível ao longe, {@code false} caso contrário.
     */
    private boolean farSight(PVector t) {
        return inSight(t, me.getDna().visionDistance, me.getDna().visionAngle);
    }

    /**
     * Realiza o cálculo geométrico para determinar a visibilidade de um ponto.
     * <p>
     * Calcula o vetor relativo ao ponto, a magnitude da distância e o ângulo
     * em relação à direção atual da entidade. Valida se estes valores se
     * encontram dentro dos limites do cone especificado.
     * </p>
     *
     * @param t           a posição do ponto a testar.
     * @param maxDistance a distância máxima permitida.
     * @param maxAngle    a abertura angular máxima (metade do cone).
     * @return {@code true} se o ponto cumprir os critérios geométricos.
     */
    private boolean inSight(PVector t, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(t, me.getPosition());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVelocity());
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
    }

    /**
     * Verifica se uma posição se encontra no alcance de visão curta.
     * <p>
     * Analisa se o ponto está dentro da zona de ataque, usando os parâmetros
     * específicos de distância e ângulo de ataque do DNA.
     * </p>
     *
     * @param t o vetor de posição a analisar.
     * @return {@code true} se o ponto estiver ao alcance de ataque, {@code false} caso contrário.
     */
    private boolean nearSight(PVector t) {
        return inSight(t, me.getDna().visionAttack, me.getDna().visionAttackAngle);
    }

    /**
     * Renderiza os cones de visão para fins de depuração (debug).
     * <p>
     * Desenha graficamente o cone de visão distante e a zona de ataque no ecrã,
     * aplicando as transformações matriciais necessárias para alinhar com a
     * posição e rotação da entidade.
     * </p>
     *
     * @param p   o contexto gráfico do Processing.
     * @param plt o objeto auxiliar para conversão de coordenadas.
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