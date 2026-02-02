package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa um tipo de terreno correspondente a um telhado.
 * <p>
 * Esta classe é responsável por definir a aparência visual e o comportamento
 * gráfico de um telhado no cenário do jogo, utilizando um sprite e as
 * funcionalidades herdadas da classe Terrain.
 * </p>
 */
public class Roof extends Terrain {
    private static final float PIXEL_CORRECTION = 100f;
    private static PImage sprite;

    /**
     * Cria uma nova instância de um telhado com dimensões e posição definidas.
     * <p>
     * Inicializa o terreno com base na posição central, largura e altura
     * fornecidas, bem como no contexto gráfico do Processing, carregando
     * também o sprite associado ao telhado.
     * </p>
     *
     * @param center a posição central do telhado no mundo do jogo
     * @param width  a largura total do telhado
     * @param height a altura total do telhado
     * @param p      o contexto gráfico do Processing utilizado para renderização
     */
    public Roof(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/roof.png");
    }

    /**
     * Desenha o telhado no ecrã.
     * <p>
     * Converte as coordenadas do mundo para coordenadas de píxeis,
     * desenha a forma base do terreno e renderiza o sprite do telhado
     * com as transformações necessárias, delegando ainda o desenho
     * da hitbox ao objeto responsável.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas da hitbox
     * @param plt     o objeto SubPlot utilizado para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.fill(0);
        p.noStroke();
        p.rect(pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f, getWidth(), getHeight());

        p.pushMatrix();

        p.translate(pp[0], pp[1]);
        p.scale(0.4f);
        p.image(sprite, -getWidth() / 2f, getHeight() - PIXEL_CORRECTION);

        p.popMatrix();
    }
}