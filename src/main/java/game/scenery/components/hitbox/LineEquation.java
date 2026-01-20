package game.scenery.components.hitbox;

/**
 * Representa e resolve a equação linear na forma y = kx + a.
 * <p>
 * Esta classe é responsável pela matemática analítica das linhas, lidando
 * especificamente com o caso de linhas verticais (paredes) onde o declive é indefinido.
 */
public class LineEquation {
    private Point position;
    private final Point start;
    private final Point stop;

    private float k;
    private float a;

    private boolean isVertical;
    private float verticalX;

    /**
     * Construtor da equação de reta.
     * Inicializa os pontos de referência com base num segmento de reta existente.
     *
     * @param segment O segmento de reta que servirá de base para a equação.
     */
    public LineEquation(LineSegment segment) {
        this.position = segment.getPosition();
        this.start = segment.getStart();
        this.stop = segment.getStop();
    }

    /**
     * Define a posição global de referência para a equação.
     * Útil quando o objeto se move e a equação precisa de ser recalculada com base no novo offset.
     *
     * @param position O novo ponto de posição global.
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Calcula os coeficientes da equação da reta com base nos pontos atuais.
     * <p>
     * Determina automaticamente se a linha é vertical (diferença em X muito pequena)
     * para evitar erros de divisão por zero e define a flag interna apropriada.
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
     * Calcula o ponto de interseção entre esta reta e outra, considerando-as infinitas.
     * <p>
     * Este método gere quatro cenários de interseção:
     * 1. Ambas verticais (retorna null).
     * 2. Apenas esta reta é vertical.
     * 3. Apenas a outra reta é vertical.
     * 4. Nenhuma é vertical (cálculo algébrico padrão).
     *
     * @param other A outra equação de reta para verificar a interseção.
     * @return Um objeto {@link Point} com as coordenadas da interseção, ou {@code null} se forem paralelas.
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
     * Calcula o valor de Y para uma dada coordenada X utilizando a equação linear.
     *
     * @param x A coordenada X de entrada.
     * @return O valor correspondente de Y (y = kx + a).
     */
    public float calculate(float x) {
        return k * x + a;
    }
}