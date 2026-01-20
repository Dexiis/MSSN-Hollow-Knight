package game.scenery.components.hitbox;

/**
 * Representa um ponto geométrico no espaço 2D.
 * <p>
 * Esta classe serve como uma estrutura fundamental para coordenadas no sistema.
 */
public class Point {

    public float x;
    public float y;

    /**
     * Construtor que inicializa o ponto com coordenadas específicas.
     *
     * @param x A coordenada horizontal.
     * @param y A coordenada vertical.
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

}