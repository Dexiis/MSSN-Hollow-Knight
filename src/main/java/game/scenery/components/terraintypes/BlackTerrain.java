package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Representa uma área de terreno retangular de cor preta.
 * <p>
 * Esta classe define uma plataforma estática posicionada no espaço,
 * caracterizada pelas suas dimensões e utilizada como elemento visual
 * e físico no cenário do jogo.
 * </p>
 */
public class BlackTerrain {

    private final PVector position;
    private final float width;
    private final float height;

    /**
     * Cria uma nova instância de terreno com forma retangular.
     * <p>
     * A posição central e as dimensões fornecidas são armazenadas
     * para permitir o cálculo correto da área ocupada e a sua
     * representação gráfica no cenário.
     * </p>
     *
     * @param center posição central do terreno no espaço
     * @param width  largura total do terreno
     * @param height altura total do terreno
     * @param p      contexto gráfico do Processing
     */
    public BlackTerrain(PVector center, float width, float height, PApplet p) {
        this.position = center;
        this.width = width;
        this.height = height;
    }

    /**
     * Apresenta graficamente o terreno no ecrã.
     * <p>
     * As coordenadas do terreno são convertidas para o sistema de pixéis
     * e é desenhado um retângulo preenchido, representando visualmente
     * a área ocupada pela plataforma.
     * </p>
     *
     * @param p       contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho de linhas auxiliares
     * @param plt     sistema de conversão entre coordenadas do mundo e pixéis
     */
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(position.x, position.y);

        p.fill(0);
        p.noStroke();
        p.rect(pp[0] - width / 2f, pp[1] - height / 2f, width, height);
    }
}
