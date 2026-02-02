package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa um elemento decorativo de vegetação no cenário.
 * <p>
 * Esta classe gere a posição e a representação gráfica de um arbusto,
 * recorrendo a um sprite para a sua visualização no mundo do jogo.
 * </p>
 */
public class Bush {

    private static PImage sprite;

    private final PVector position;

    /**
     * Cria uma nova instância de arbusto numa posição específica.
     * <p>
     * A posição central é armazenada e o recurso gráfico necessário
     * é carregado através do contexto fornecido.
     * </p>
     *
     * @param center posição central do arbusto no espaço
     * @param p      contexto gráfico do Processing
     */
    public Bush(PVector center, PApplet p) {
        this.position = center;

        sprite = p.loadImage("images/bushes.png");
    }

    /**
     * Apresenta graficamente o arbusto no ecrã.
     * <p>
     * A posição é convertida para coordenadas de píxeis e o sprite
     * é desenhado com a transformação adequada para o tamanho pretendido.
     * </p>
     *
     * @param p       contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho de linhas auxiliares
     * @param plt     sistema de conversão entre coordenadas do mundo e píxeis
     */
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(position.x, position.y);

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(0.5f);
        p.image(sprite, 0, 0);

        p.popMatrix();
    }
}