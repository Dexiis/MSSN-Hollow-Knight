package game.scenery.components.hitbox;

import game.core.SubPlot;

/**
 * Representa um segmento de reta limitado por dois pontos no espaço.
 * <p>
 * Esta classe gere a informação geométrica associada a um segmento finito,
 * incluindo a sua posição global, e recorre a uma representação algébrica
 * auxiliar para realizar cálculos de interseção.
 * </p>
 */
public class LineSegment {
    private final LineEquation equation;
    private Point position;
    private final Point start;
    private final Point stop;

    /**
     * Cria um novo segmento de reta com base numa posição e dois pontos locais.
     * <p>
     * São definidos os pontos inicial e final relativos à posição global,
     * sendo também criada a representação algébrica necessária para cálculos
     * geométricos associados ao segmento.
     * </p>
     *
     * @param position ponto de referência no espaço global
     * @param start    ponto inicial relativo à posição
     * @param stop     ponto final relativo à posição
     */
    public LineSegment(Point position, Point start, Point stop) {
        this.position = position;
        this.start = start;
        this.stop = stop;
        this.equation = new LineEquation(this);
    }

    /**
     * Fornece a posição global atualmente associada ao segmento.
     * <p>
     * Este valor é utilizado como referência para converter coordenadas
     * locais dos pontos para o espaço global.
     * </p>
     *
     * @return ponto de referência no mundo
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Disponibiliza o ponto local onde o segmento tem início.
     * <p>
     * O ponto devolvido encontra-se definido relativamente à posição global
     * do segmento.
     * </p>
     *
     * @return ponto inicial do segmento
     */
    public Point getStart() {
        return start;
    }

    /**
     * Disponibiliza o ponto local onde o segmento termina.
     * <p>
     * O ponto devolvido representa o limite final do segmento relativamente
     * à posição global.
     * </p>
     *
     * @return ponto final do segmento
     */
    public Point getStop() {
        return stop;
    }

    /**
     * Atualiza a posição global associada ao segmento.
     * <p>
     * A nova referência espacial é propagada para a representação algébrica,
     * garantindo coerência nos cálculos subsequentes.
     * </p>
     *
     * @param position novo ponto de referência no espaço
     */
    public void setPosition(Point position) {
        this.position = position;
        this.equation.setPosition(position);
    }

    /**
     * Indica se existe cruzamento entre este segmento e outro fornecido.
     * <p>
     * É determinado o ponto de interseção das representações lineares e,
     * caso exista, é verificado se esse ponto se encontra dentro dos limites
     * de ambos os segmentos.
     * </p>
     *
     * @param other outro segmento de reta a comparar
     * @return {@code true} se existir cruzamento, {@code false} caso contrário
     */
    public boolean intersects(LineSegment other) {
        Point intersection = equation.solveIntersectionPoint(other.equation);
        if (intersection == null) return false;

        return this.isPointOnSegment(intersection) && other.isPointOnSegment(intersection);
    }

    /**
     * Avalia se um ponto pertence à extensão finita do segmento.
     * <p>
     * As coordenadas locais são convertidas para o espaço global e é aplicada
     * uma verificação de limites com margem de tolerância para compensar
     * imprecisões numéricas.
     * </p>
     *
     * @param p ponto a avaliar
     * @return {@code true} se o ponto estiver contido no segmento, {@code false} caso contrário
     */
    private boolean isPointOnSegment(Point p) {
        float epsilon = 0.001f;

        float x1 = start.x + position.x;
        float y1 = start.y + position.y;
        float x2 = stop.x + position.x;
        float y2 = stop.y + position.y;

        float minX = Math.min(x1, x2) - epsilon;
        float maxX = Math.max(x1, x2) + epsilon;
        float minY = Math.min(y1, y2) - epsilon;
        float maxY = Math.max(y1, y2) + epsilon;

        return p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY;
    }

    /**
     * Desenha o segmento de reta no ecrã.
     * <p>
     * As coordenadas globais dos pontos inicial e final são calculadas
     * e fornecidas ao componente responsável pela renderização gráfica.
     * </p>
     *
     * @param painter componente responsável pelo desenho da linha
     * @param plt     sistema de conversão entre coordenadas do mundo e pixéis
     */
    public void draw(LinePainter painter, SubPlot plt) {
        painter.paintLine(start.x + position.x, start.y + position.y, stop.x + position.x, stop.y + position.y, plt);
    }
}