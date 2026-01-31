package game.scenery.components.hitbox;

import processing.core.PVector;

/**
 * Representa uma coordenada espacial bidimensional.
 * <p>
 * Atua como estrutura fundamental para posições e vértices na geometria de colisão.
 * </p>
 */
public class Point {

    public float x;
    public float y;

    /**
     * Instancia um novo ponto com coordenadas específicas.
     * <p>
     * Atribui os valores às componentes horizontal e vertical.
     * </p>
     *
     * @param x a coordenada no eixo horizontal
     * @param y a coordenada no eixo vertical
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public PVector toPVector() {
        return new PVector(x, y);
    }

}