package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa um piso sólido com representação gráfica.
 * <p>
 * Esta classe define um terreno retangular que serve de suporte às entidades,
 * utilizando um sprite para a sua visualização no cenário.
 * </p>
 */
public class Floor extends Terrain {

    private static PImage sprite;

    /**
     * Cria uma nova instância de piso com dimensões definidas.
     * <p>
     * A posição central e as dimensões são encaminhadas para a classe base,
     * sendo também carregado o recurso gráfico associado ao piso.
     * </p>
     *
     * @param center posição central do terreno
     * @param width  largura total do piso
     * @param height altura total do piso
     * @param p      contexto gráfico do Processing
     */
    public Floor(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/Floor01.png");
    }

    /**
     * Apresenta graficamente o piso no ecrã.
     * <p>
     * A área do terreno é desenhada no espaço e o sprite é renderizado
     * de forma ajustada às dimensões definidas, mantendo coerência visual
     * com o sistema de colisões.
     * </p>
     *
     * @param p       contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho de linhas auxiliares
     * @param plt     sistema de conversão entre coordenadas do mundo e píxeis
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.fill(0);
        p.noStroke();
        p.rect(pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f, getWidth(), getHeight());

        p.image(sprite, pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f, getWidth(), sprite.pixelHeight);
    }
}
