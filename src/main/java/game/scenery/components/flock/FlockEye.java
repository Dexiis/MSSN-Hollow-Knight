package game.scenery.components.flock;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Responsável pela perceção visual de uma entidade pertencente a um bando.
 * <p>
 * Esta classe atua como um sensor, analisando o espaço envolvente da entidade
 * proprietária e identificando outras entidades dentro de diferentes zonas de visão,
 * permitindo suportar comportamentos coletivos como alinhamento, coesão e separação.
 * </p>
 */
public class FlockEye {
    private ArrayList<Flock> allTrackingBodies = new ArrayList<>();
    private ArrayList<Flock> farSight = new ArrayList<>();
    private final Flock me;
    private ArrayList<Flock> nearSight = new ArrayList<>();
    protected Flock target;

    /**
     * Cria um sensor visual associado a uma entidade do bando.
     * <p>
     * Inicializa a ligação à entidade proprietária e regista as restantes entidades
     * que podem ser observadas no ambiente, definindo também um alvo inicial.
     * </p>
     *
     * @param me      a entidade à qual o sensor pertence
     * @param targets a lista inicial de entidades presentes no ambiente
     */
    public FlockEye(Flock me, ArrayList<Flock> targets) {
        this.me = me;
        this.target = targets.getFirst();
        addTarget(targets);
    }

    /**
     * Devolve todas as entidades configuradas para observação.
     * <p>
     * Esta lista representa o conjunto completo de entidades que podem ser
     * analisadas pelo sensor durante o processo de perceção.
     * </p>
     *
     * @return a lista de entidades monitorizadas
     */
    public ArrayList<Flock> getAllTrackingBodies() {
        return allTrackingBodies;
    }

    /**
     * Fornece as entidades detetadas na zona de visão alargada.
     * <p>
     * As entidades presentes nesta lista encontram-se dentro do campo de visão
     * normal e são utilizadas para cálculos de alinhamento e coesão do bando.
     * </p>
     *
     * @return a lista de entidades visíveis à distância
     */
    public ArrayList<Flock> getFarSight() {
        return farSight;
    }

    /**
     * Fornece as entidades detetadas na zona de proximidade imediata.
     * <p>
     * Esta zona representa uma área crítica em redor da entidade, sendo usada
     * para evitar colisões e suportar comportamentos de separação.
     * </p>
     *
     * @return a lista de entidades demasiado próximas
     */
    public ArrayList<Flock> getNearSight() {
        return nearSight;
    }

    /**
     * Devolve a entidade atualmente definida como alvo.
     * <p>
     * O alvo pode ser usado para orientar decisões específicas ou comportamentos
     * prioritários da entidade proprietária.
     * </p>
     *
     * @return a entidade alvo
     */
    public Flock getTarget() {
        return target;
    }

    /**
     * Atualiza o conjunto completo de entidades a observar.
     * <p>
     * Substitui a lista atual de entidades monitorizadas por uma nova lista,
     * redefinindo os potenciais vizinhos a considerar durante a perceção.
     * </p>
     *
     * @param allTrackingBodies a nova lista de entidades observáveis
     */
    public void setAllTrackingBodies(ArrayList<Flock> allTrackingBodies) {
        this.allTrackingBodies = allTrackingBodies;
    }

    /**
     * Define uma nova entidade como alvo principal.
     * <p>
     * Esta referência passa a ser utilizada como foco preferencial para
     * comportamentos que dependam de um alvo específico.
     * </p>
     *
     * @param target a entidade a definir como alvo
     */
    public void setTarget(Flock target) {
        this.target = target;
    }

    /**
     * Adiciona novas entidades ao conjunto de observação.
     * <p>
     * As entidades fornecidas são registadas como possíveis vizinhos,
     * garantindo que a própria entidade proprietária não é incluída.
     * </p>
     *
     * @param targets a lista de entidades a adicionar
     */
    public void addTarget(ArrayList<Flock> targets) {
        for (Flock target : targets) {
            if (target != me) allTrackingBodies.add(target);
        }
    }

    /**
     * Desenha a representação visual do campo de visão.
     * <p>
     * Este desenho tem como objetivo auxiliar a depuração, ilustrando os ângulos
     * e alcances das diferentes zonas de perceção definidas no DNA da entidade.
     * </p>
     *
     * @param p   o contexto gráfico do Processing
     * @param plt o objeto responsável pela conversão de coordenadas
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

    /**
     * Atualiza a perceção do ambiente envolvente.
     * <p>
     * Percorre todas as entidades monitorizadas e classifica-as de acordo com
     * a sua posição relativa, preenchendo as listas de visão distante e próxima.
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
     * Determina se uma posição se encontra na zona de visão alargada.
     * <p>
     * A verificação tem em conta a distância máxima e o ângulo de visão definidos
     * no DNA da entidade proprietária.
     * </p>
     *
     * @param t o vetor de posição a analisar
     * @return {@code true} se a posição estiver visível, {@code false} caso contrário
     */
    private boolean farSight(PVector t) {
        return inSight(t, me.getDna().getVisionDistance(), me.getDna().getVisionAngle());
    }

    /**
     * Avalia se uma posição se encontra dentro de um determinado cone de visão.
     * <p>
     * O cálculo considera a distância entre a entidade e a posição alvo, bem como
     * o ângulo formado com a direção atual do movimento.
     * </p>
     *
     * @param t           o vetor de posição a analisar
     * @param maxDistance a distância máxima considerada visível
     * @param maxAngle    o ângulo máximo permitido
     * @return {@code true} se a posição estiver dentro do cone definido, {@code false} caso contrário
     */
    private boolean inSight(PVector t, float maxDistance, float maxAngle) {
        PVector r = PVector.sub(t, me.getPosition());
        float d = r.mag();
        float angle = PVector.angleBetween(r, me.getVelocity());
        return ((d > 0) && (d < maxDistance) && (angle < maxAngle));
    }

    /**
     * Determina se uma posição se encontra na zona de proximidade crítica.
     * <p>
     * Esta verificação utiliza os parâmetros de alcance e ângulo definidos para
     * a zona de separação, permitindo identificar entidades demasiado próximas.
     * </p>
     *
     * @param t o vetor de posição a analisar
     * @return {@code true} se a posição estiver na zona próxima, {@code false} caso contrário
     */
    private boolean nearSight(PVector t) {
        return inSight(t, me.getDna().getVisionAttack(), me.getDna().getVisionAttackAngle());
    }
}