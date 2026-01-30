package game.scenery.components.hitbox;

/**
 * Define a representação matemática de uma reta no formato y = kx + a.
 * <p>
 * Gere a lógica algébrica para cálculos de declives e interseções, tratando retas verticais.
 * </p>
 */
public class LineEquation {

    private final Point start;
    private final Point stop;

    private Point position;

    private float k;
    private float a;

    private boolean isVertical;
    private float verticalX;

    /**
     * Inicializa a estrutura da equação linear com base num segmento de reta.
     * <p>
     * Armazena as referências para os pontos de início e fim, e a posição global.
     * </p>
     *
     * @param segment o segmento de reta que fornece os dados geométricos
     */
    public LineEquation(LineSegment segment) {
        this.position = segment.getPosition();
        this.start = segment.getStart();
        this.stop = segment.getStop();
    }

    /**
     * Atualiza o ponto de referência global da equação.
     *
     * @param position o novo ponto de posição no mundo
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Computa os coeficientes da equação da reta.
     * <p>
     * Analisa as coordenadas atuais para determinar a inclinação e deteta retas verticais.
     * </p>
     */
    public void formEquation() {
        float x1 = start.x + position.x;
        float y1 = start.y + position.y;
        float x2 = stop.x + position.x;
        float y2 = stop.y + position.y;

        if (Math.abs(x2 - x1) < 0.001f) {
            isVertical = true;
            verticalX = x1;
            k = 0;
            a = 0;
        } else {
            isVertical = false;
            k = (y2 - y1) / (x2 - x1);
            a = y1 - (k * x1);
        }
    }

    /**
     * Calcula as coordenadas de interseção entre esta reta e uma outra equação.
     * <p>
     * Resolve o sistema de equações lineares considerando cenários geométricos.
     * </p>
     *
     * @param other a outra equação linear para verificar a convergência
     * @return o ponto exato da interseção, ou {@code null} se as retas forem paralelas
     */
    public Point solveIntersectionPoint(LineEquation other) {
        this.formEquation();
        other.formEquation();

        float intersectX, intersectY;

        if (this.isVertical && other.isVertical) return null;

        else if (this.isVertical) {
            intersectX = this.verticalX;
            intersectY = other.calculate(intersectX);
        } else if (other.isVertical) {
            intersectX = other.verticalX;
            intersectY = this.calculate(intersectX);
        } else {
            if (Math.abs(this.k - other.k) < 0.001f) return null;

            intersectX = (other.a - this.a) / (this.k - other.k);
            intersectY = calculate(intersectX);
        }

        return new Point(intersectX, intersectY);
    }

    /**
     * Determina o valor da coordenada Y para um dado X.
     *
     * @param x a coordenada horizontal de entrada
     * @return o valor resultante de Y
     */
    public float calculate(float x) {
        return k * x + a;
    }
}