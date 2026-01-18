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
    private Point position;
    private final Point start;
    private final Point stop;
    private LineEquation equation;

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
        return intersection != null;
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
    }

    /**
     * Verifica se um ponto específico (x, y) está contido dentro dos limites físicos do segmento (Bounding Box).
     * <p>
     * Este método é útil para validar se uma interseção calculada pela equação da reta infinita
     * ocorre realmente dentro do pedaço finito de reta que este segmento representa.
     *
     * @param x A coordenada X global do ponto.
     * @param y A coordenada Y global do ponto.
     * @return {@code true} se o ponto estiver sobre o segmento (com uma pequena margem de erro), {@code false} caso contrário.
     */
    public boolean isPointOnLine(float x, float y) {
        float x1 = start.x + position.x;
        float x2 = stop.x + position.x;
        float y1 = start.y + position.y;
        float y2 = stop.y + position.y;

        boolean betweenX = (x >= Math.min(x1, x2) - 0.01f) && (x <= Math.max(x1, x2) + 0.01f);
        boolean betweenY = (y >= Math.min(y1, y2) - 0.01f) && (y <= Math.max(y1, y2) + 0.01f);

        return betweenX && betweenY;
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