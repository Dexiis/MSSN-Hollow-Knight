package game.scenery.components.hitbox;

/**
 * Representa matematicamente uma reta no plano cartesiano.
 * <p>
 * Esta classe encapsula os cálculos necessários para trabalhar com uma reta
 * definida a partir de um segmento, permitindo obter valores, coeficientes
 * e pontos de interseção, incluindo o tratamento de situações verticais.
 * </p>
 */
public class LineEquation {

    private Point position;
    private final Point start;
    private final Point stop;
    private float a;
    private float k;
    private float verticalX;
    private boolean isVertical;

    /**
     * Cria uma representação linear a partir de um segmento de reta.
     * <p>
     * São guardadas as referências aos pontos inicial e final do segmento,
     * bem como a posição global associada, servindo de base para os cálculos
     * posteriores da equação.
     * </p>
     *
     * @param segment segmento de reta que fornece a informação geométrica
     */
    public LineEquation(LineSegment segment) {
        this.position = segment.getPosition();
        this.start = segment.getStart();
        this.stop = segment.getStop();
    }

    /**
     * Obtém o valor da coordenada vertical para uma coordenada horizontal dada.
     * <p>
     * O cálculo é realizado com base nos coeficientes atualmente definidos,
     * assumindo que a reta não se encontra numa configuração vertical.
     * </p>
     *
     * @param x valor da coordenada horizontal
     * @return valor correspondente da coordenada vertical
     */
    public float calculate(float x) {
        return k * x + a;
    }

    /**
     * Calcula os parâmetros algébricos que definem a reta.
     * <p>
     * A partir das coordenadas dos pontos inicial e final, já ajustadas à posição
     * global, são determinados o declive e a ordenada na origem, sendo também
     * identificada a situação de uma reta vertical.
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
     * Define uma nova posição global para a equação.
     * <p>
     * A atualização deste ponto altera a referência espacial utilizada
     * nos cálculos da reta, influenciando os valores obtidos posteriormente.
     * </p>
     *
     * @param position novo ponto de referência no espaço
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Determina o ponto de cruzamento entre duas representações lineares.
     * <p>
     * São analisadas as equações envolvidas, considerando casos de paralelismo
     * e retas verticais, devolvendo a coordenada exata de interseção quando
     * esta existe.
     * </p>
     *
     * @param other outra representação linear a comparar
     * @return ponto de interseção ou {@code null} caso não exista cruzamento
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
}