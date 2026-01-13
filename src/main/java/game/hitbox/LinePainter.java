package game.hitbox;

/**
 * Interface que define o contrato para desenhar linhas no sistema de colisão.
 * <p>
 * Esta interface permite desacoplar a lógica das Hitboxes da biblioteca gráfica específica
 * (como o Processing). Qualquer classe que implemente esta interface pode ser usada
 * pela Hitbox para se desenhar a si mesma.
 */
public interface LinePainter {

    /**
     * Desenha um segmento de reta entre dois pontos.
     *
     * @param x1 Coordenada X do ponto inicial.
     * @param y1 Coordenada Y do ponto inicial.
     * @param x2 Coordenada X do ponto final.
     * @param y2 Coordenada Y do ponto final.
     */
    void paintLine(float x1, float y1, float x2, float y2);

    /**
     * Define a cor do traço para as próximas linhas a serem desenhadas.
     *
     * @param color O valor inteiro que representa a cor.
     */
    void setStroke(int color);

    /**
     * Define a espessura (peso) do traço da linha.
     *
     * @param weight A espessura da linha em pixels.
     */
    void setStrokeWeight(float weight);
}