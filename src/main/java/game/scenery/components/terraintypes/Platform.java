package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa uma plataforma sólida no ambiente.
 * <p>
 * Permite que entidades se apoiem ou interajam com ela, fornecendo uma superfície
 * para movimento e colisão no jogo.
 * </p>
 */
public class Platform extends Terrain {

    private static final int PIXEL_CORRECTION_X = 2;
    private static final int PIXEL_CORRECTION_Y = 10;
    private static final int SPRITE_WIDTH = 270;
    private static PImage sprite;

    /**
     * Constrói uma nova plataforma com as dimensões especificadas.
     * <p>
     * Inicializa a posição central, largura, altura e carrega o sprite da plataforma
     * a partir do arquivo de imagem "images/plat02.png".
     * </p>
     *
     * @param center o centro da plataforma no mundo
     * @param width a largura da plataforma
     * @param height a altura da plataforma
     * @param p o contexto gráfico do Processing
     */
    public Platform(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/plat02.png");
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
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);
        draw(painter, plt);

        p.image(sprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION_X, pp[1] - getHeight() / 2f - PIXEL_CORRECTION_Y);

        if(getWidth() > SPRITE_WIDTH)
            p.image(sprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION_X * 3 + SPRITE_WIDTH, pp[1] - getHeight() / 2f - PIXEL_CORRECTION_Y);
    }
}
