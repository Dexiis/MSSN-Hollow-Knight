package game.scenery.components.hitbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma área onde uma entidade pode sofrer dano.
 * <p>
 * Esta classe especializa a Hitbox para definir zonas vulneráveis de uma entidade,
 * permitindo identificar regiões específicas que podem ser afetadas por interações
 * externas, como ataques ou colisões.
 * </p>
 */
public class HurtBox extends Hitbox {

    /**
     * Cria uma nova instância de HurtBox com base numa lista de vértices.
     * <p>
     * A geometria definida pelos pontos fornecidos é encaminhada para a classe base,
     * ficando responsável pela configuração da forma e limites da área vulnerável.
     * </p>
     *
     * @param points lista de objetos Point que definem o contorno poligonal da área
     */
    public HurtBox(List<Point> points) {
        super(points);
    }

    /**
     * Fornece uma abordagem incremental para a criação de instâncias HurtBox.
     * <p>
     * Esta classe interna permite adicionar vértices de forma progressiva,
     * facilitando a definição da geometria antes da criação final do objeto.
     * </p>
     */
    public static class Builder {
        private final List<Point> points = new ArrayList<>();

        /**
         * Acrescenta um novo vértice à geometria em definição.
         * <p>
         * Cada chamada adiciona um ponto com as coordenadas indicadas,
         * permitindo construir gradualmente a forma pretendida.
         * </p>
         *
         * @param x coordenada horizontal do vértice
         * @param y coordenada vertical do vértice
         * @return a própria instância do Builder para permitir encadeamento de chamadas
         */
        public Builder addPoint(float x, float y) {
            points.add(new Point(x, y));
            return this;
        }

        /**
         * Conclui o processo de construção da área vulnerável.
         * <p>
         * Utiliza os vértices acumulados para criar uma nova instância de HurtBox,
         * ficando esta totalmente configurada com a geometria definida.
         * </p>
         *
         * @return uma nova instância de HurtBox com os pontos especificados
         */
        public HurtBox build() {
            return new HurtBox(points);
        }
    }
}