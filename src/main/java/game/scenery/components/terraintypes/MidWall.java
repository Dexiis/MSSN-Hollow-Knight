package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa uma parede intermédia composta por vários elementos gráficos.
 * <p>
 * Esta classe define um terreno vertical que utiliza múltiplos sprites
 * empilhados para criar uma estrutura contínua no cenário do jogo.
 * </p>
 */
public class MidWall extends Terrain {
    private static final float PIXEL_CORRECTION = 25f;
    private static PImage pillarSprite;
    private static PImage bottomWallSprite;

    /**
     * Cria uma nova instância de parede intermédia com dimensões definidas.
     * <p>
     * A posição central e as dimensões são encaminhadas para a classe base,
     * sendo também carregados os recursos gráficos necessários à sua
     * representação visual.
     * </p>
     *
     * @param center posição central do terreno
     * @param width  largura total da parede
     * @param height altura total da parede
     * @param p      contexto gráfico do Processing
     */
    public MidWall(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        pillarSprite = p.loadImage("images/pillar.png");
        bottomWallSprite = p.loadImage("images/wall_bottom.png");
    }

    /**
     * Apresenta graficamente a parede no ecrã.
     * <p>
     * Os elementos verticais são desenhados de forma sequencial para formar
     * a estrutura da parede, sendo aplicado um ajuste adicional para o
     * alinhamento correto da base.
     * </p>
     *
     * @param p       contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho de linhas auxiliares
     * @param plt     sistema de conversão entre coordenadas do mundo e píxeis
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        for (int i = 0; i < 2; i++)
            p.image(pillarSprite, pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f + i * pillarSprite.pixelHeight);

        p.image(bottomWallSprite, pp[0] - getWidth() / 2f - PIXEL_CORRECTION, pp[1] - getHeight() / 2f + 2 * pillarSprite.pixelHeight);
    }
}