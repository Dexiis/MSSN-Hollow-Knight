package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Platform extends Terrain {

    private static PImage sprite;
    private static final int SPRITE_WIDTH = 270;

    private static final int PIXEL_CORRECTION_X = 2;
    private static final int PIXEL_CORRECTION_Y = 10;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center A posição central do terreno.
     * @param width  A largura total.
     * @param height A altura total.
     */
    public Platform(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/plat02.png");
    }

    /**
     * Desenha o terreno no ecrã.
     * Delega a renderização para a classe pai {@link Hitbox}.
     *
     * @param p       O contexto gráfico do Processing.
     * @param painter O objeto responsável pelo desenho das linhas.
     * @param plt     O objeto SubPlot para conversão de coordenadas.
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);
        draw(painter, plt);

        p.image(sprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION_X, pp[1] - getHeight() / 2f - PIXEL_CORRECTION_Y);

        if(getWidth() > SPRITE_WIDTH)
            p.image(sprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION_X * 3 + SPRITE_WIDTH, pp[1] - getHeight() / 2f - PIXEL_CORRECTION_Y);
    }
}
