package game.hitbox;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Representa um ponto geométrico no espaço 2D.
 * <p>
 * Esta classe atua como um invólucro (wrapper) em torno da classe {@link PVector} do Processing,
 * fornecendo funcionalidades adicionais específicas para o sistema de Hitboxes,
 * como métodos de visualização de debug.
 */
public class Point {

    /**
     * O vetor de posição subjacente da biblioteca Processing.
     * Armazena as coordenadas x e y reais.
     */
    public PVector pos;

    /**
     * Construtor da classe Point.
     * Inicializa um novo ponto com as coordenadas especificadas.
     *
     * @param x A coordenada horizontal.
     * @param y A coordenada vertical.
     */
    public Point(float x, float y) {
        this.pos = new PVector(x, y);
    }

    /**
     * Desenha o ponto no ecrã para fins de visualização ou debug.
     * O ponto é desenhado como um ponto vermelho com espessura de 5 pixels.
     *
     * @param p A instância do PApplet usada para desenhar.
     */
    public void display(PApplet p) {
        p.pushStyle();
        p.stroke(255, 0, 0);
        p.strokeWeight(5);
        p.point(pos.x, pos.y);
        p.popStyle();
    }

    /**
     * Retorna uma representação em String das coordenadas do ponto.
     * Útil para imprimir valores na consola durante o processo de debug.
     *
     * @return Uma string no formato "(x, y)".
     */
    @Override
    public String toString() {
        return "(" + pos.x + ", " + pos.y + ")";
    }

    /**
     * Obtém a coordenada X do ponto.
     * Fornece um acesso direto à componente X do vetor de posição.
     *
     * @return O valor de x.
     */
    public float getX() {
        return pos.x;
    }

    /**
     * Obtém a coordenada Y do ponto.
     * Fornece um acesso direto à componente Y do vetor de posição.
     *
     * @return O valor de y.
     */
    public float getY() {
        return pos.y;
    }
}