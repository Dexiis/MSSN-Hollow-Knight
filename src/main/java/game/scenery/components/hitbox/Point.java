package game.scenery.components.hitbox;

import processing.core.PVector;

/**
 * Representa uma coordenada bidimensional no espaço.
 * <p>
 * Esta classe serve como base para a definição de posições, vértices e pontos
 * utilizados nos cálculos geométricos e nos sistemas de colisão.
 * </p>
 */
public class Point {

    public float x;
    public float y;

    /**
     * Cria uma nova coordenada com valores específicos.
     * <p>
     * Os valores fornecidos são atribuídos diretamente às componentes horizontal
     * e vertical, passando a definir a localização do ponto no plano.
     * </p>
     *
     * @param x valor correspondente ao eixo horizontal
     * @param y valor correspondente ao eixo vertical
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converte a coordenada para um vetor compatível com a biblioteca gráfica.
     * <p>
     * É criada uma nova instância de {@link PVector} contendo as componentes
     * horizontal e vertical atualmente associadas a esta coordenada.
     * </p>
     *
     * @return vetor bidimensional equivalente a este ponto
     */
    public PVector toPVector() {
        return new PVector(x, y);
    }

}