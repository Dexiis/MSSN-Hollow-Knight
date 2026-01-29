package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Wall extends Terrain {

    private static PImage sprite;
    private static final int SPRITE_HEIGHT = 353;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center A posição central do terreno.
     * @param width  A largura total.
     * @param height A altura total.
     * @param p
     */
    public Wall(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/wall.png");
    }

    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);
        draw(painter, plt);

        float pixelCorrectionX = getWidth() - sprite.pixelWidth;
        int slices = PApplet.ceil(getHeight() / SPRITE_HEIGHT);


        for(int i = 0; i < slices; i++)
            p.image(sprite, pp[0] - getWidth() / 2f + pixelCorrectionX, pp[1] - getHeight() / 2f + i * SPRITE_HEIGHT);

    }
}
