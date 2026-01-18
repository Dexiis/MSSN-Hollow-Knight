package game.scenery.components.hitbox;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class HurtBox extends Hitbox implements IVisualizable {

    public HurtBox(List<Point> points) {
        super(points);
    }

    /**
     * Classe utilitária (Builder Pattern) para facilitar a construção progressiva de terrenos.
     */
    public static class Builder {
        private final List<Point> points = new ArrayList<>();

        /**
         * Adiciona um novo vértice ao polígono do terreno.
         *
         * @param x Coordenada X do ponto.
         * @param y Coordenada Y do ponto.
         * @return O próprio Builder para encadeamento de chamadas.
         */
        public Builder addPoint(float x, float y) {
            points.add(new Point(x, y));
            return this;
        }

        /**
         * Finaliza a construção e retorna uma nova instância de Terrain.
         *
         * @return O objeto Terrain configurado.
         */
        public HurtBox build() {
            return new HurtBox(points);
        }
    }

    /**
     * Desenha o terreno no ecrã.
     * Delega a renderização para a classe pai {@link Hitbox}.
     *
     * @param p       O contexto gráfico do Processing.
     * @param painter O objeto responsável pelo desenho das linhas.
     * @param plt     O objeto SubPlot para conversão de coordenadas.
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        //TODO SPRITES???
        draw(painter, plt);
        roughHitbox.draw(p, plt);
    }
}
