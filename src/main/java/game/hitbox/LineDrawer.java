package game.hitbox;

import processing.core.PApplet;

/**
 * Implementação concreta da interface {@link LinePainter} utilizando a biblioteca Processing.
 * <p>
 * Esta classe atua como um adaptador que redireciona os comandos de desenho de linhas
 * definidos na lógica do jogo diretamente para os métodos gráficos do {@link PApplet}.
 */
public class LineDrawer implements LinePainter {
    private final PApplet pApplet;

    /**
     * Construtor do LineDrawer.
     * Inicializa o desenhador com uma instância do PApplet onde os desenhos serão realizados.
     *
     * @param pApplet O contexto gráfico do Processing (geralmente a classe principal do sketch).
     */
    public LineDrawer(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    /**
     * Desenha uma linha no ecrã entre dois pontos especificados.
     * Utiliza o método {@code line()} do Processing.
     *
     * @param x1 Coordenada X do ponto inicial.
     * @param y1 Coordenada Y do ponto inicial.
     * @param x2 Coordenada X do ponto final.
     * @param y2 Coordenada Y do ponto final.
     */
    @Override
    public void paintLine(float x1, float y1, float x2, float y2) {
        pApplet.line(x1, y1, x2, y2);
    }

    /**
     * Define a cor do traço da linha.
     * Utiliza o método {@code stroke()} do Processing.
     *
     * @param color O valor da cor (pode ser um inteiro gerado por {@code pApplet.color()}).
     */
    @Override
    public void setStroke(int color) {
        pApplet.stroke(color);
    }

    /**
     * Define a espessura do traço da linha.
     * Utiliza o método {@code strokeWeight()} do Processing.
     *
     * @param weight A espessura da linha em pixels.
     */
    @Override
    public void setStrokeWeight(float weight) {
        pApplet.strokeWeight(weight);
    }
}