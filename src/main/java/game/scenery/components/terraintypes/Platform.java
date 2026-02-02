package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa uma plataforma sólida no ambiente de jogo.
 * <p>
 * Esta classe define um elemento de terreno que permite às entidades
 * deslocarem-se e interagirem com uma superfície estável, integrando
 * tanto a componente visual como a de colisão.
 * </p>
 */
public class Platform extends Terrain {

    private static final int PIXEL_CORRECTION_X = 2;
    private static final int PIXEL_CORRECTION_Y = 10;
    private static final int SPRITE_WIDTH = 270;
    private static PImage sprite;

    /**
     * Cria uma nova plataforma com posição e dimensões definidas.
     * <p>
     * A posição central, largura e altura são encaminhadas para a classe base,
     * sendo também carregado o recurso gráfico utilizado para a sua
     * representação visual.
     * </p>
     *
     * @param center posição central da plataforma no mundo
     * @param width  largura total da plataforma
     * @param height altura total da plataforma
     * @param p      contexto gráfico do Processing
     */
    public Platform(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/plat02.png");
    }

    /**
     * Apresenta graficamente a plataforma no ecrã.
     * <p>
     * O sprite é desenhado na posição correta, aplicando correções de alinhamento,
     * sendo repetido quando necessário para acomodar plataformas com maior largura.
     * </p>
     *
     * @param p       contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho de linhas auxiliares
     * @param plt     sistema de conversão entre coordenadas do mundo e píxeis
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.image(sprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION_X, pp[1] - getHeight() / 2f - PIXEL_CORRECTION_Y);

        if (getWidth() > SPRITE_WIDTH)
            p.image(sprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION_X * 3 + SPRITE_WIDTH, pp[1] - getHeight() / 2f - PIXEL_CORRECTION_Y);
    }
}