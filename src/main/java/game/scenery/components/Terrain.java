package game.scenery.components;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.TheKnight;
import game.scenery.hitbox.Hitbox;
import game.scenery.hitbox.LinePainter;
import game.scenery.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um elemento físico estático do ambiente de jogo (como chão, paredes ou plataformas).
 * <p>
 * Estende a funcionalidade da classe {@link Hitbox} para incluir lógica específica de resolução
 * de colisões físicas, impedindo que entidades atravessem o terreno.
 */
public class Terrain extends Hitbox implements IVisualizable {
    private final static int PIXEL_CORRECTION = 1;

    /**
     * Constrói um objeto de terreno baseado numa forma poligonal arbitrária.
     *
     * @param points Lista de pontos que definem os vértices do polígono.
     */
    public Terrain(List<Point> points) {
        super(points);
    }

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center A posição central do terreno.
     * @param width  A largura total.
     * @param height A altura total.
     */
    public Terrain(PVector center, float width, float height) {
        super(new Point(center.x, center.y), width, height);
    }

    /**
     * Calcula e resolve a resposta física à colisão entre este terreno e uma entidade.
     * <p>
     * Utiliza a lógica AABB (Axis-Aligned Bounding Box) para determinar a profundidade da sobreposição
     * entre as caixas de colisão. Baseado na sobreposição (overlap), reposiciona a entidade
     * imediatamente fora do terreno e anula a velocidade no eixo da colisão.
     * <p>
     * Também é responsável por detetar se uma entidade do tipo {@link TheKnight} aterrou no chão.
     *
     * @param entity A entidade que será verificada e ajustada fisicamente.
     */
    public void elaborateIntersects(Entity entity) {
        Hitbox otherHitbox = entity.getHitbox();

        if (!this.roughHitbox.isIntersecting(otherHitbox.getRoughHitbox())) {
            return;
        }

        float dx = otherHitbox.getPosition().x - this.getPosition().x;
        float dy = otherHitbox.getPosition().y - this.getPosition().y;

        float combinedHalfW = (otherHitbox.getWidth() / 2) + (this.width / 2);
        float combinedHalfH = (otherHitbox.getHeight() / 2) + (this.height / 2);

        float overlapX = combinedHalfW - Math.abs(dx);
        float overlapY = combinedHalfH - Math.abs(dy);

        if (overlapX > 0 && overlapY > 0) {

            if (overlapX < overlapY) {

                if (dx > 0) {
                    entity.setVelocity(new PVector(0, entity.getVelocity().y));
                    float newX = this.getPosition().x + this.width / 2 + otherHitbox.getWidth() / 2 - PIXEL_CORRECTION;
                    entity.setPosition(new PVector(newX, entity.getPosition().y));
                } else {
                    entity.setVelocity(new PVector(0, entity.getVelocity().y));
                    float newX = this.getPosition().x - this.width / 2 - otherHitbox.getWidth() / 2 + PIXEL_CORRECTION;
                    entity.setPosition(new PVector(newX, entity.getPosition().y));
                }

            } else {

                if (dy > 0) {
                    entity.setVelocity(new PVector(entity.getVelocity().x, 0));
                    float newY = this.getPosition().y + this.height / 2 + otherHitbox.getHeight() / 2 - PIXEL_CORRECTION;
                    entity.setPosition(new PVector(entity.getPosition().x, newY));

                    if (entity instanceof TheKnight) ((TheKnight) entity).setIsGrounded(true);
                } else {
                    entity.setVelocity(new PVector(entity.getVelocity().x, 0));
                    float newY = this.getPosition().y - this.height / 2 - otherHitbox.getHeight() / 2 - PIXEL_CORRECTION;
                    entity.setPosition(new PVector(entity.getPosition().x, newY));
                }
            }
        }
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
        public Terrain build() {
            return new Terrain(points);
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
    }
}