package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

public class BlackTerrain {

    private final PVector position;
    private final float width;
    private final float height;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center a posição central do terreno
     * @param width  a largura total
     * @param height a altura total
     * @param p      o contexto gráfico do Processing
     */
    public BlackTerrain(PVector center, float width, float height, PApplet p) {
        this.position = center;
        this.width = width;
        this.height = height;
    }

    /**
     * Desenha a plataforma no ecrã.
     * <p>
     * Renderiza o sprite e delega o desenho da hitbox.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(position.x, position.y);
        // draw(painter, plt);

        p.fill(0);
        p.noStroke();
        p.rect(pp[0] - width / 2f, pp[1] - height / 2f, width, height);
    }
}
