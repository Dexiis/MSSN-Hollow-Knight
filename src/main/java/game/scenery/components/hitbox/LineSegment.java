package game.scenery.components.hitbox;

import game.core.SubPlot;

/**
 * Representa um segmento de reta finito definido por um ponto inicial e um final,
 * relativos a uma posição global.
 * <p>
 * Esta classe gere a representação espacial do segmento e delega os cálculos
 * geométricos analíticos (como interseções de retas infinitas) para a classe {@link LineEquation}.
 */
public class LineSegment {
    private final Point start;
    private final Point stop;
    private final LineEquation equation;

    private Point position;

    /**
     * Construtor do segmento de reta.
     * Inicializa os pontos e cria a equação linear associada, passando a própria instância
     * para que a equação possa aceder aos dados atualizados.
     *
     * @param position A posição global (offset) do segmento.
     * @param start    O ponto inicial (coordenada local relativa à posição).
     * @param stop     O ponto final (coordenada local relativa à posição).
     */
    public LineSegment(Point position, Point start, Point stop) {
        this.position = position;
        this.start = start;
        this.stop = stop;
        this.equation = new LineEquation(this);
    }

    /**
     * Obtém o ponto inicial do segmento (coordenadas locais).
     *
     * @return O ponto de início.
     */
    public Point getStart() {
        return start;
    }

    /**
     * Obtém o ponto final do segmento (coordenadas locais).
     *
     * @return O ponto de fim.
     */
    public Point getStop() {
        return stop;
    }

    /**
     * Obtém a equação da reta associada a este segmento.
     *
     * @return O objeto {@link LineEquation}.
     */
    public LineEquation getEquation() {
        return equation;
    }

    /**
     * Obtém a posição global de referência do segmento.
     *
     * @return O ponto de posição.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Define a nova posição global de referência para o segmento.
     * A atualização reflete-se automaticamente nos cálculos da equação linear associada
     * na próxima vez que esta for invocada.
     *
     * @param position O novo ponto de posição global.
     */
    public void setPosition(Point position) {
        this.position = position;
        this.equation.setPosition(position);  // FALTA ESTA LINHA!
    }

    /**
     * Verifica se este segmento de reta interseta outro segmento.
     * <p>
     * Utiliza a equação linear associada para calcular matematicamente se existe
     * um ponto de interseção entre as duas retas.
     *
     * @param other O outro segmento de reta a verificar.
     * @return {@code true} se houver colisão (interseção), {@code false} caso contrário.
     */
    public boolean intersects(LineSegment other) {
        Point intersection = equation.solveIntersectionPoint(other.getEquation());

        if (intersection == null) return false;

        return this.isPointOnSegment(intersection) && other.isPointOnSegment(intersection);
    }

    /**
     * Verifica se um ponto está contido neste segmento de reta finito.
     * <p>
     * Para o ponto estar no segmento, as suas coordenadas devem estar dentro dos
     * limites definidos pelos pontos inicial e final (em coordenadas globais).
     * Utiliza uma pequena tolerância (epsilon) para lidar com erros de arredondamento
     * de ponto flutuante.
     *
     * @param p O ponto a verificar (em coordenadas globais).
     * @return {@code true} se o ponto está no segmento, {@code false} caso contrário.
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
     * Desenha o segmento de reta no ecrã.
     * Converte as coordenadas locais e globais para coordenadas de ecrã utilizando o SubPlot.
     *
     * @param painter O objeto responsável pela pintura da linha.
     * @param plt     O objeto SubPlot usado para a conversão de coordenadas.
     */
    public void draw(LinePainter painter, SubPlot plt) {
        painter.paintLine(start.x + position.x, start.y + position.y, stop.x + position.x, stop.y + position.y, plt);
    }
}