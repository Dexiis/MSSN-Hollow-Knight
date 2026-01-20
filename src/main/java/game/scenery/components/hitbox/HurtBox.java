package game.scenery.components.hitbox;

import game.core.SubPlot;
import game.scenery.characters.IVisualizable;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma área de vulnerabilidade (HurtBox) associada a uma entidade.
 * <p>
 * Uma HurtBox define a região onde a entidade pode receber dano ou interagir negativamente
 * com outros elementos (como Hitboxes de ataque). Implementa {@link IVisualizable} para
 * permitir a sua renderização no sistema de jogo.
 */
public class HurtBox extends Hitbox implements IVisualizable {

    /**
     * Construtor da HurtBox.
     * Inicializa a forma de colisão baseada numa lista de pontos.
     *
     * @param points Lista de pontos que definem o polígono da HurtBox.
     */
    public HurtBox(List<Point> points) {
        super(points);
    }

    /**
     * Exibe a HurtBox no ecrã.
     * <p>
     * Este método desenha a forma poligonal definida pelos segmentos de reta e também
     * a caixa delimitadora aproximada (RoughHitbox) para fins de depuração ou visualização técnica.
     *
     * @param p       O contexto gráfico do Processing.
     * @param painter O objeto responsável pelo desenho das linhas.
     * @param plt     O objeto SubPlot para conversão de coordenadas.
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        draw(painter, plt);
        roughHitbox.draw(p, plt);
    }

    /**
     * Classe utilitária (Builder Pattern) para facilitar a construção progressiva de HurtBoxes.
     */
    public static class Builder {
        private final List<Point> points = new ArrayList<>();

        /**
         * Adiciona um novo vértice ao polígono da HurtBox.
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
         * Finaliza a construção e retorna uma nova instância de HurtBox.
         *
         * @return O objeto HurtBox configurado com os pontos adicionados.
         */
        public HurtBox build() {
            return new HurtBox(points);
        }
    }
}