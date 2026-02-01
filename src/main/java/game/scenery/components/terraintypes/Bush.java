package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Bush {

    private static PImage sprite;

    private final PVector position;

    /**
     * Constrói um objeto de arbusto.
     *
     * @param center a posição central do arbusto
     * @param p      o contexto gráfico do Processing
     */
    public Bush(PVector center, PApplet p) {
        this.position = center;

        sprite = p.loadImage("images/bushes.png");
    }

    /**
     * Desenha o arbusto no ecrã.
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

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(0.5f);
        p.image(sprite, 0, 0);

        p.popMatrix();
    }

}
