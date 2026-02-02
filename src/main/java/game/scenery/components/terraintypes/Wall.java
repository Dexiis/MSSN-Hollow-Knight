package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa uma parede sólida que bloqueia movimento.
 * <p>
 * Impede a passagem de entidades através dela, atuando como barreira no ambiente.
 * </p>
 */
public class Wall extends Terrain {

    private static final int SPRITE_HEIGHT = 353;
    private static PImage sprite;
    private final SIDE side;

    public enum SIDE {
        LEFT, RIGHT
    }

    /**
     * Constrói uma nova parede com as dimensões especificadas.
     * <p>
     * Inicializa a posição central, largura, altura e carrega o sprite da parede
     * a partir do arquivo de imagem "images/wall.png".
     * </p>
     *
     * @param center o centro da parede no mundo
     * @param width  a largura da parede
     * @param height a altura da parede
     * @param p      o contexto gráfico do Processing
     */
    public Wall(PVector center, float width, float height, PApplet p, SIDE side) {
        super(center, width, height, p);
        this.side = side;

        sprite = p.loadImage("images/wall.png");
    }

    /**
     * Desenha a parede no ecrã.
     * <p>
     * Renderiza o sprite em fatias e delega o desenho da hitbox.
     * </p>
     *
     * @param p       o contexto gráfico
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        float pixelCorrectionX = getWidth() - sprite.pixelWidth + 12;
        int slices = PApplet.ceil(getHeight() / SPRITE_HEIGHT);

        p.fill(0);
        p.noStroke();
        p.rect(pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f, getWidth(), getHeight());

        if (side == SIDE.LEFT) {
            p.pushMatrix();
            p.translate(pp[0], pp[1]);
            p.scale(-1, 1);
            for (int i = 0; i < slices; i++) {
                if (i == slices - 1) {
                    PImage cropped = sprite.get(0, 0, sprite.pixelWidth, (int) ((pp[1] + getHeight() / 2f) - (pp[1] - getHeight() / 2f + i * SPRITE_HEIGHT)));
                    p.image(cropped, -getWidth() / 2f + pixelCorrectionX, -getHeight() / 2f + i * SPRITE_HEIGHT);
                } else p.image(sprite, -getWidth() / 2f + pixelCorrectionX, -getHeight() / 2f + i * SPRITE_HEIGHT);

            }
            p.popMatrix();
        } else {
            for (int i = 0; i < slices; i++) {
                if (i == slices - 1) {
                    PImage cropped = sprite.get(0, 0, sprite.pixelWidth, (int) ((pp[1] + getHeight() / 2f) - (pp[1] - getHeight() / 2f + i * SPRITE_HEIGHT)));
                    p.image(cropped, pp[0] - getWidth() / 2f + pixelCorrectionX, pp[1] - getHeight() / 2f + i * SPRITE_HEIGHT);
                } else
                    p.image(sprite, pp[0] - getWidth() / 2f + pixelCorrectionX, pp[1] - getHeight() / 2f + i * SPRITE_HEIGHT);
            }
        }
    }
}
