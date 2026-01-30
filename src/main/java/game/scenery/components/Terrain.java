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
 * Representa um elemento físico estático do ambiente.
 * <p>
 * Estende Hitbox para incluir lógica de resolução de colisões físicas.
 * </p>
 */
public class Terrain extends Hitbox implements IVisualizable {

    protected PApplet p;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center a posição central do terreno
     * @param width  a largura total
     * @param height a altura total
     * @param p      o contexto gráfico do Processing
     */
    public Terrain(PVector center, float width, float height, PApplet p) {
        super(new Point(center.x, center.y), width, height);
        this.p = p;
    }

    /**
     * Desenha o terreno no ecrã.
     * <p>
     * Delega a renderização para a classe pai Hitbox.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        //TODO SPRITES???
        draw(painter, plt);
    }

    /**
     * Calcula e resolve a resposta física à colisão com uma entidade.
     * <p>
     * Reposiciona a entidade fora do terreno e anula a velocidade no eixo da colisão.
     * Deteta se o cavaleiro ou chefe aterrou.
     * </p>
     *
     * @param entity a entidade que será verificada e ajustada fisicamente
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
                if (dx > 0) newX = this.getPosition().x + this.width / 2 + otherHitbox.getWidth() / 2 - PIXEL_CORRECTION;
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