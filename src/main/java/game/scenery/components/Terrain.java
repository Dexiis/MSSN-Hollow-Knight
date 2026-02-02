package game.scenery.components;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.mobs.Squit;
import game.scenery.components.hitbox.Hitbox;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Representa um elemento físico estático do cenário.
 * <p>
 * Esta classe estende a funcionalidade de Hitbox para permitir a interação
 * física com entidades do jogo, tratando colisões, ajustes de posição e
 * respostas físicas, além de permitir a sua visualização gráfica.
 * </p>
 */
public class Terrain extends Hitbox implements IVisualizable {

    protected PApplet p;

    /**
     * Cria um novo elemento de terreno retangular.
     * <p>
     * Inicializa a hitbox associada ao terreno a partir da posição central,
     * largura e altura fornecidas, guardando também o contexto gráfico do
     * Processing para efeitos de renderização.
     * </p>
     *
     * @param center a posição central do terreno no mundo do jogo
     * @param width  a largura total do terreno
     * @param height a altura total do terreno
     * @param p      o contexto gráfico do Processing
     */
    public Terrain(PVector center, float width, float height, PApplet p) {
        super(new Point(center.x, center.y), width, height);
        this.p = p;
    }

    /**
     * Desenha o terreno no ecrã.
     * <p>
     * Utiliza a funcionalidade de desenho herdada da classe Hitbox para
     * representar visualmente o terreno, recorrendo ao objeto responsável
     * pelo desenho das linhas e à conversão de coordenadas.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        draw(painter, plt);
    }

    /**
     * Resolve a colisão física entre o terreno e uma entidade.
     * <p>
     * Analisa a interseção entre a hitbox do terreno e a da entidade,
     * ajustando a posição da entidade para fora do terreno, anulando a
     * velocidade no eixo correspondente e atualizando estados específicos
     * consoante o tipo de entidade envolvida na colisão.
     * </p>
     *
     * @param entity a entidade que interage fisicamente com o terreno
     */
    public void elaborateIntersects(Entity entity) {
        Hitbox otherHitbox = entity.getHitbox();

        if (!this.roughHitbox.isIntersecting(otherHitbox.getRoughHitbox())) return;

        float dx = otherHitbox.getPosition().x - this.getPosition().x;
        float dy = otherHitbox.getPosition().y - this.getPosition().y;

        float combinedHalfW = (otherHitbox.getWidth() / 2) + (this.width / 2);
        float combinedHalfH = (otherHitbox.getHeight() / 2) + (this.height / 2);

        float overlapX = combinedHalfW - Math.abs(dx);
        float overlapY = combinedHalfH - Math.abs(dy);

        if (overlapX > 0 && overlapY > 0) {

            if (entity instanceof Squit) ((Squit) entity).setColliding(true);
            if (overlapX < overlapY) {
                entity.setVelocity(new PVector(0, entity.getVelocity().y));
                int PIXEL_CORRECTION = 0;
                if (entity instanceof FalseKnight) {
                    ((FalseKnight) entity).setWalled(true);
                    PIXEL_CORRECTION = 1;
                }
                float newX;
                if (dx > 0)
                    newX = this.getPosition().x + this.width / 2 + otherHitbox.getWidth() / 2 - PIXEL_CORRECTION;
                else newX = this.getPosition().x - this.width / 2 - otherHitbox.getWidth() / 2 + PIXEL_CORRECTION;

                entity.setPosition(new PVector(newX, entity.getPosition().y));

            } else {
                entity.setVelocity(new PVector(entity.getVelocity().x, 0));

                float newY;
                if (dy > 0) {
                    newY = this.getPosition().y + this.height / 2 + otherHitbox.getHeight() / 2;
                    if (entity instanceof TheKnight) ((TheKnight) entity).setGrounded(true);
                    if (entity instanceof FalseKnight) ((FalseKnight) entity).setGrounded(true);
                } else newY = this.getPosition().y - this.height / 2 - otherHitbox.getHeight() / 2;

                entity.setPosition(new PVector(entity.getPosition().x, newY));
            }
        }
    }
}