package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class MidWall extends Terrain {
    private static final float PIXEL_CORRECTION = 25f;
    private static PImage pillarSprite;
    private static PImage bottomWallSprite;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center a posição central do terreno
     * @param width  a largura total
     * @param height a altura total
     * @param p      o contexto gráfico do Processing
     */
    public MidWall(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        pillarSprite = p.loadImage("images/pillar.png");
        bottomWallSprite = p.loadImage("images/wall_bottom.png");
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

        for(int i = 0; i < 2; i++)
            p.image(pillarSprite, pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f + i * pillarSprite.pixelHeight);

        p.image(bottomWallSprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION, pp[1] - getHeight() / 2f + 2 * pillarSprite.pixelHeight);

    }
}
