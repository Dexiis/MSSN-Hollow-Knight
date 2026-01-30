package game.scenery.components.flock;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Gere a perceção visual de um agente do bando.
 * <p>
 * Esta classe funciona como um sensor que analisa o ambiente em redor da entidade,
 * identificando outros agentes dentro do campo de visão e zona de proximidade.
 * </p>
 */
public class FlockEye {
    private ArrayList<Flock> allTrackingBodies = new ArrayList<>();
    private ArrayList<Flock> farSight = new ArrayList<>();
    private ArrayList<Flock> nearSight = new ArrayList<>();
    private final Flock me;
    protected Flock target;

    /**
     * Constrói uma nova instância do sensor visual.
     * <p>
     * Inicializa a referência para a entidade proprietária e a lista de corpos a rastrear.
     * </p>
     *
     * @param me      a entidade a quem este sensor pertence
     * @param targets a lista inicial de outras entidades no ambiente
     */
    public FlockEye(Flock me, ArrayList<Flock> targets) {
        this.me = me;
        this.target = targets.getFirst();
        addTarget(targets);
    }

    /**
     * Define o alvo específico que a entidade deve focar.
     *
     * @param target a entidade alvo
     */
    public void setTarget(Flock target) {
        this.target = target;
    }

    /**
     * Obtém a referência para o alvo atual da entidade.
     *
     * @return a entidade alvo
     */
    public Flock getTarget() {
        return target;
    }

    /**
     * Define a lista completa de todas as entidades que este olho deve monitorizar.
     *
     * @param allTrackingBodies a lista de entidades rastreáveis
     */
    public void setAllTrackingBodies(ArrayList<Flock> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    /**
     * Obtém a lista de todas as entidades que estão configuradas para serem rastreadas.
     *
     * @return a lista de entidades
     */
    public ArrayList<Flock> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    /**
     * Adiciona um conjunto de entidades à lista de corpos rastreáveis pelo sensor.
     * <p>
     * Filtra a lista para garantir que a própria entidade não é incluída.
     * </p>
     *
     * @param targets a lista de novos potenciais vizinhos
     */
    public void addTarget(ArrayList<Flock> targets) {
        for (Flock target : targets) {
            if (target != me) allTrackingBodies.add(target);
        }
    }

    /**
     * Obtém a lista de vizinhos detetados no campo de visão alargado.
     * <p>
     * Estes vizinhos são usados para calcular comportamentos de coesão e alinhamento.
     * </p>
     *
     * @return uma lista de entidades visíveis
     */
    public ArrayList<Flock> getFarSight() {
        return farSight;
    }

    /**
     * Obtém a lista de vizinhos detetados na zona de proximidade imediata.
     * <p>
     * Estes vizinhos são usados para calcular comportamentos de separação.
     * </p>
     *
     * @return uma lista de entidades demasiado próximas
     */
    public ArrayList<Flock> getNearSight() {
        return nearSight;
    }

    /**
     * Executa o processo de perceção sensorial.
     * <p>
     * Percorre todas as entidades rastreáveis e determina se estão visíveis,
     * considerando a distância toroidal e os ângulos de visão.
     * </p>
     */
    public void look() {
        farSight = new ArrayList<>();
        nearSight = new ArrayList<>();
        for (Flock character : allTrackingBodies) {
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
        return inSight(t, me.getDna().getVisionDistance(), me.getDna().getVisionAngle());
    }

    /**
     * Verifica se uma entidade específica está dentro do cone de visão geral.
     * <p>
     * Calcula o vetor de distância toroidal e verifica se a magnitude é inferior
     * à distância de visão definida no DNA. Se estiver perto o suficiente, verifica
     * se o ângulo entre a direção atual e o alvo está dentro do ângulo de visão permitido.
     * </p>
     *
     * @param t a entidade a verificar
     * @return {@code true} se a entidade estiver visível, {@code false} caso contrário
     */
    private boolean nearSight(PVector t) {
        return inSight(t, me.getDna().getVisionAttack(), me.getDna().getVisionAttackAngle());
    }

    /**
     * Verifica se uma entidade específica está na zona crítica de proximidade.
     * <p>
     * Semelhante à verificação de visão distante, mas utiliza o raio de "ataque"
     * (separação) e o ângulo correspondente definidos no DNA.
     * </p>
     *
     * @param t a entidade a verificar
     * @return {@code true} se a entidade estiver na zona próxima, {@code false} caso contrário
     */
    private boolean inSight(PVector t, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(t, me.getPosition());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVelocity());
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
    }

    /**
     * Renderiza a representação gráfica do campo de visão.
     * <p>
     * Desenha os cones de visão para auxiliar na visualização do comportamento.
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

        float[] dd1 = plt.getDimInPixel(me.getDna().getVisionDistance(), me.getDna().getVisionDistance());
        p.rotate(me.getDna().getVisionAngle());
        p.line(0, 0, dd1[0], 0);
        p.rotate(-2 * me.getDna().getVisionAngle());
        p.line(0, 0, dd1[0], 0);
        p.rotate(me.getDna().getVisionAngle());
        p.arc(0, 0, 2 * dd1[0], 2 * dd1[0], -me.getDna().getVisionAngle(), me.getDna().getVisionAngle());

        float[] dd2 = plt.getDimInPixel(me.getDna().getVisionAttack(), me.getDna().getVisionAttack());
        p.stroke(255, 0, 255);
        if (me.getDna().getVisionAttackAngle() >= Math.PI) {
            p.circle(0, 0, 2 * dd2[0]);
        } else {
            p.rotate(me.getDna().getVisionAttackAngle());
            p.line(0, 0, dd2[0], 0);
            p.rotate(-2 * me.getDna().getVisionAttackAngle());
            p.line(0, 0, dd2[0], 0);
            p.rotate(me.getDna().getVisionAttackAngle());
            p.arc(0, 0, 2 * dd2[0], 2 * dd2[0], -me.getDna().getVisionAttackAngle(), me.getDna().getVisionAttackAngle());
        }

        p.popMatrix();
        p.popStyle();
    }
}