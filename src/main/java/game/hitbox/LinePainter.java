package game.hitbox;

import game.core.SubPlot;

/**
 * Define o contrato para desenhar linhas.
 * <p>
 * Esta interface é utilizada para desacoplar a lógica de desenho da {@link Hitbox}
 * da implementação gráfica específica, permitindo diferentes estratégias de visualização.
 */
public interface LinePainter {
    /**
     * Método responsável por desenhar uma linha entre dois pontos definidos.
     *
     * @param x1  Coordenada X do ponto inicial.
     * @param y1  Coordenada Y do ponto inicial.
     * @param x2  Coordenada X do ponto final.
     * @param y2  Coordenada Y do ponto final.
     * @param plt O objeto SubPlot utilizado para a conversão de coordenadas (Mundo para Pixel).
     */
    public void paintLine(float x1, float y1, float x2, float y2, SubPlot plt);
}