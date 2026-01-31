package game.scenery.components.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Define uma zona de vulnerabilidade associada a uma entidade.
 * <p>
 * Estende a classe Hitbox para representar áreas onde a entidade é suscetível a receber dano.
 * </p>
 */
public class HurtBox extends Hitbox {

    /**
     * Instancia uma nova HurtBox a partir de uma lista de vértices.
     * <p>
     * Encaminha a geometria para a classe pai para inicialização.
     * </p>
     *
     * @param points lista de objetos Point que delimitam o polígono da área
     */
    public HurtBox(List<Point> points) {
        super(points);
    }

    /**
     * Disponibiliza um mecanismo fluente para a construção de instâncias HurtBox.
     */
    public static class Builder {
        private final List<Point> points = new ArrayList<>();

        /**
         * Regista um novo vértice na geometria em construção.
         *
         * @param x coordenada horizontal do vértice
         * @param y coordenada vertical do vértice
         * @return a própria instância do Builder para encadeamento
         */
        public Builder addPoint(float x, float y) {
            points.add(new Point(x, y));
            return this;
        }

        /**
         * Finaliza o processo de construção e gera a instância HurtBox.
         *
         * @return um novo objeto HurtBox configurado com a geometria definida
         */
        public HurtBox build() {
            return new HurtBox(points);
        }
    }
}