package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Roof extends Terrain {
    private static final float PIXEL_CORRECTION = 100f;
    private static PImage sprite;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center a posição central do terreno
     * @param width  a largura total
     * @param height a altura total
     * @param p      o contexto gráfico do Processing
     */
    public Roof(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/roof.png");
    }

    /**
     * Desenha a parede no ecrã.
     * <p>
     * Renderiza o sprite em fatias e delega o desenho da hitbox.
     * </p>
     *
     * @param p o contexto gráfico
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);
        //draw(painter, plt);

        // TODO tentar melhorar performance

        p.pushMatrix();
        p.translate(pp[0], pp[1]);
        p.scale(0.4f);

        p.image(sprite,-getWidth() / 2f, getHeight() - PIXEL_CORRECTION);

        p.popMatrix();
    }
}
