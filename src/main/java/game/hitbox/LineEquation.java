package game.hitbox;

/**
 * Representa a equação matemática de uma reta na forma algébrica (y = kx + a).
 * <p>
 * Esta classe é utilizada para realizar cálculos geométricos analíticos,
 * essenciais para determinar pontos de interseção exatos entre as linhas das Hitboxes.
 */
public class LineEquation {
    private Point position;
    private Point start;
    private Point stop;
    private Point result = new Point(0.0f, 0.0f);
    private float k;
    private float a;

    /**
     * Construtor da LineEquation.
     * Inicializa os dados da equação com base num segmento de reta existente.
     *
     * @param segment O segmento de reta (LineSegment) que servirá de base para a equação.
     */
    public LineEquation(LineSegment segment) {
        this.position = segment.getPosition();
        this.start = segment.getStart();
        this.stop = segment.getStop();
    }

    /**
     * Calcula os coeficientes da equação da reta ($k$ e $a$).
     * <p>
     * O cálculo baseia-se nas coordenadas dos pontos de início e fim, ajustados pela posição global.
     * <br>
     * <b>Nota:</b> Se a reta for perfeitamente vertical (delta X == 0), o código define o declive ($k$) como 0
     * para evitar divisões por zero, embora matematicamente o declive fosse indefinido.
     */
    public void formEquation() {
        if (stop.pos.x == start.pos.x) {
            k = 0.0f;
        } else {
            k = (stop.pos.y - start.pos.y) / (stop.pos.x - start.pos.x);
        }
        a = start.pos.y + position.pos.y - k * (start.pos.x + position.pos.x);
    }

    /**
     * Obtém o declive (coeficiente angular) da reta.
     *
     * @return O valor de $k$.
     */
    public float getK() {
        return k;
    }

    /**
     * Obtém a ordenada na origem (constante da equação).
     * Determina onde a reta cruza o eixo Y (quando x = 0).
     *
     * @return O valor de $a$.
     */
    public float getA() {
        return a;
    }

    /**
     * Calcula o ponto de interseção entre esta equação de reta e outra.
     * <p>
     * O método resolve o sistema de equações para encontrar o valor de X onde
     * as duas retas se encontram e, em seguida, calcula o Y correspondente.
     *
     * @param other A outra equação de reta com a qual se procura a interseção.
     * @return Um objeto {@link Point} contendo as coordenadas (x, y) da interseção.
     */
    public Point solveIntersectionPoint(LineEquation other) {
        this.formEquation();
        other.formEquation();

        float resultK = this.k - other.getK();
        float resultA = other.getA() - this.a;

        if (resultK != 0.0f) {
            resultA /= resultK;
        } else {
            resultA = 0.0f;
        }

        float x = resultA;

        result.pos.x = x;
        result.pos.y = calculate(x);

        return result;
    }

    /**
     * Calcula o valor de Y para uma dada coordenada X, usando a equação da reta atual.
     * <p>
     * Fórmula: $y = kx + a$
     *
     * @param x A coordenada X de entrada.
     * @return O valor calculado de Y.
     */
    public float calculate(float x) {
        return k * x + a;
    }

    /**
     * Atualiza a posição de deslocamento (offset) usada nos cálculos da equação.
     * Útil quando o objeto se move no mundo e a equação precisa de ser recalculada.
     *
     * @param position O novo ponto de posição.
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}