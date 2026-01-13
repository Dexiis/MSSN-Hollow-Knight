package game.hitbox;

import processing.core.PApplet;

/**
 * Representa um segmento de reta finito definido por dois pontos (início e fim)
 * relativos a uma posição global.
 * <p>
 * Enquanto a {@link LineEquation} lida com a matemática da reta infinita,
 * esta classe limita essa reta a um segmento específico e gere a lógica de interseção,
 * incluindo casos especiais como linhas verticais.
 */
public class LineSegment {
    private Point position;
    private final Point start;
    private final Point stop;
    private LineEquation equation;

    /**
     * Construtor do LineSegment.
     * Inicializa o segmento e cria automaticamente a equação da reta associada.
     *
     * @param position A posição global (offset) do objeto a que este segmento pertence.
     * @param start    O ponto inicial do segmento (coordenada local).
     * @param stop     O ponto final do segmento (coordenada local).
     */
    public LineSegment(Point position, Point start, Point stop) {
        this.position = position;
        this.start = start;
        this.stop = stop;
        this.equation = new LineEquation(this);
    }

    /**
     * Obtém a equação matemática (y = kx + a) que representa a reta deste segmento.
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
     * Verifica se o segmento é vertical.
     * <p>
     * É considerado vertical se a diferença horizontal entre o início e o fim for
     * menor que uma tolerância (0.001), para evitar erros de precisão de ponto flutuante.
     * Linhas verticais requerem tratamento matemático especial (declive infinito).
     *
     * @return {@code true} se for vertical, {@code false} caso contrário.
     */
    public boolean isVertical() {
        return Math.abs(start.pos.x - stop.pos.x) < 0.001f;
    }

    /**
     * Verifica se este segmento interseta outro segmento.
     * <p>
     * O método lida com três cenários:
     * 1. Ambos os segmentos são verticais (verifica sobreposição no eixo Y).
     * 2. Apenas um é vertical (calcula a interseção pontual).
     * 3. Nenhum é vertical (usa álgebra linear padrão através da {@link LineEquation}).
     *
     * @param other O outro segmento para verificar a colisão.
     * @return {@code true} se os segmentos se tocarem ou cruzarem.
     */
    public boolean intersects(LineSegment other) {
        if (this.isVertical() && other.isVertical()) {
            if (Math.abs((this.start.pos.x + position.pos.x) - (other.start.pos.x + other.position.pos.x)) > 0.001f)
                return false;

            return rangeOverlap(this.start.pos.y + position.pos.y, this.stop.pos.y + position.pos.y, other.start.pos.y + other.position.pos.y, other.stop.pos.y + other.position.pos.y);
        }

        if (this.isVertical() || other.isVertical()) {
            LineSegment vert = this.isVertical() ? this : other;
            LineSegment nonVert = this.isVertical() ? other : this;

            nonVert.getEquation().formEquation();

            float vertX = vert.start.pos.x + vert.position.pos.x;
            float intersectionY = nonVert.getEquation().calculate(vertX);

            return vert.isPointOnLine(vertX, intersectionY) && nonVert.isPointOnLine(vertX, intersectionY);
        }

        Point p = equation.solveIntersectionPoint(other.getEquation());
        return this.isPointOnLine(p.pos.x, p.pos.y) && other.isPointOnLine(p.pos.x, p.pos.y);
    }

    private boolean rangeOverlap(float a1, float a2, float b1, float b2) {
        return Math.max(a1, a2) >= Math.min(b1, b2) && Math.min(a1, a2) <= Math.max(b1, b2);
    }

    /**
     * Verifica se um ponto (x, y) está contido dentro dos limites deste segmento.
     * <p>
     * Isto é necessário porque a equação da reta considera a linha infinita.
     * Este método garante que a colisão ocorreu entre o ponto inicial e o ponto final.
     *
     * @param x Coordenada X global do ponto.
     * @param y Coordenada Y global do ponto.
     * @return {@code true} se o ponto estiver no segmento (com margem de erro 'epsilon').
     */
    public boolean isPointOnLine(float x, float y) {
        float epsilon = 0.05f;
        float xGlobal = position.pos.x;
        float yGlobal = position.pos.y;

        boolean betweenX = x >= Math.min(start.pos.x, stop.pos.x) + xGlobal - epsilon && x <= Math.max(start.pos.x, stop.pos.x) + xGlobal + epsilon;

        boolean betweenY = y >= Math.min(start.pos.y, stop.pos.y) + yGlobal - epsilon && y <= Math.max(start.pos.y, stop.pos.y) + yGlobal + epsilon;

        return betweenX && betweenY;
    }

    /**
     * Desenha o segmento de reta no ecrã para fins de debug ou visualização.
     *
     * @param p O contexto gráfico do Processing (PApplet).
     */
    public void display(PApplet p) {
        float x1 = start.pos.x + position.pos.x;
        float y1 = start.pos.y + position.pos.y;
        float x2 = stop.pos.x + position.pos.x;
        float y2 = stop.pos.y + position.pos.y;

        p.pushStyle();
        p.stroke(0);
        p.strokeWeight(2);
        p.line(x1, y1, x2, y2);
        p.popStyle();
    }

    /**
     * Atualiza a posição global do segmento.
     * Propaga a atualização também para a equação matemática associada.
     *
     * @param position O novo ponto de posição global.
     */
    public void setPosition(Point position) {
        this.position = position;
        this.equation.setPosition(position);
    }
}