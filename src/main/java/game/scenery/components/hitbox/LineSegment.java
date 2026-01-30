package game.scenery.components.hitbox;

import game.core.SubPlot;

/**
 * Define um segmento de reta finito delimitado por um ponto inicial e um final.
 * <p>
 * Gere a representação espacial do segmento e delega cálculos algébricos para LineEquation.
 * </p>
 */
public class LineSegment {
    private final Point start;
    private final Point stop;
    private final LineEquation equation;

    private Point position;

    /**
     * Inicializa um novo segmento de reta.
     * <p>
     * Configura os pontos locais e a posição global, instanciando a equação linear associada.
     * </p>
     *
     * @param position a coordenada global de referência
     * @param start    o ponto de início relativo à posição
     * @param stop     o ponto de fim relativo à posição
     */
    public LineSegment(Point position, Point start, Point stop) {
        this.position = position;
        this.start = start;
        this.stop = stop;
        this.equation = new LineEquation(this);
    }

    /**
     * Obtém o ponto local onde o segmento começa.
     *
     * @return o objeto Point inicial
     */
    public Point getStart() {
        return start;
    }

    /**
     * Obtém o ponto local onde o segmento termina.
     *
     * @return o objeto Point final
     */
    public Point getStop() {
        return stop;
    }

    /**
     * Recupera a posição global atual do segmento.
     *
     * @return o ponto de referência no mundo
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Atualiza a posição global do segmento.
     * <p>
     * Propaga a alteração para a equação linear interna.
     * </p>
     *
     * @param position o novo ponto de posição
     */
    public void setPosition(Point position) {
        this.position = position;
        this.equation.setPosition(position);
    }

    /**
     * Analisa se existe uma interseção física entre este segmento e outro.
     * <p>
     * Calcula o ponto de cruzamento das retas e valida se está dentro dos limites de ambos os segmentos.
     * </p>
     *
     * @param other o outro segmento de reta a testar
     * @return {@code true} se os segmentos se cruzarem, {@code false} caso contrário
     */
    public boolean intersects(LineSegment other) {
        Point intersection = equation.solveIntersectionPoint(other.equation);
        if (intersection == null) return false;

        return this.isPointOnSegment(intersection) && other.isPointOnSegment(intersection);
    }

    /**
     * Verifica se um ponto está contido no segmento.
     * <p>
     * Converte coordenadas para espaço global e aplica verificação de limites com tolerância.
     * </p>
     *
     * @param p o ponto a verificar
     * @return {@code true} se o ponto estiver no segmento, {@code false} caso contrário
     */
    private boolean isPointOnSegment(Point p) {
        float epsilon = 0.001f;

        // Converter pontos locais para coordenadas globais
        float x1 = start.x + position.x;
        float y1 = start.y + position.y;
        float x2 = stop.x + position.x;
        float y2 = stop.y + position.y;

        // Calcular os limites do segmento
        float minX = Math.min(x1, x2) - epsilon;
        float maxX = Math.max(x1, x2) + epsilon;
        float minY = Math.min(y1, y2) - epsilon;
        float maxY = Math.max(y1, y2) + epsilon;

        // Verificar se o ponto está dentro dos limites
        return p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY;
    }

    /**
     * Renderiza o segmento de reta no ecrã.
     * <p>
     * Utiliza o pintor de linhas para desenhar a conexão entre os pontos.
     * </p>
     *
     * @param painter objeto responsável pelo desenho vetorial
     * @param plt     sistema de conversão de coordenadas do mundo para pixéis
     */
    public void draw(LinePainter painter, SubPlot plt) {
        painter.paintLine(start.x + position.x, start.y + position.y, stop.x + position.x, stop.y + position.y, plt);
    }
}
