package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Floor extends Terrain {

    private static PImage sprite;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center a posição central do terreno
     * @param width  a largura total
     * @param height a altura total
     * @param p      o contexto gráfico do Processing
     */
    public Floor(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/Floor01.png");
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
        // draw(painter, plt);

        p.fill(0);
        p.rect(pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f, getWidth(), getHeight());

        p.image(sprite, pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f);

    }
}
