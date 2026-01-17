package game.hitbox;

import processing.core.PVector;

/**
 * Representa um ponto geométrico no espaço 2D.
 * <p>
 * Esta classe serve como uma estrutura fundamental para coordenadas no sistema.
 */
public class Point {

    public float x = 0.0f;
    public float y = 0.0f;

    /**
     * Construtor padrão.
     * Inicializa o ponto na origem das coordenadas (0.0, 0.0).
     */
    public Point() {
        this(0.0f, 0.0f);
    }

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

    /**
     * Converte este ponto para um objeto {@link PVector} da biblioteca Processing.
     * Útil para interagir com funções nativas do Processing que exigem vetores.
     *
     * @return Uma nova instância de PVector com as mesmas coordenadas (x, y).
     */
    public PVector toPVector() {
        return new PVector(x, y);
    }
}