package game.scenery.components.terraintypes;

import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import processing.core.PApplet;
import processing.core.PVector;

public class DeathFloor extends Terrain {
    private final Map map;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center A posição central do terreno.
     * @param width  A largura total.
     * @param height A altura total.
     */
    public DeathFloor(PVector center, float width, float height, Map map, PApplet p) {
        super(center, width, height, p);
        this.map = map;
    }

    @Override
    public void elaborateIntersects(Entity entity) {
        this.intersected(entity);
    }


    public void intersected(Entity entity) {
        if (super.intersected(entity.getHitbox())) {
            if (entity instanceof TheKnight) {
                entity.damage(p);
                entity.setPosition(new PVector(50, 800));
                entity.setVelocity(new PVector(0, 0));
                entity.setAcceleration(new PVector(0, 0));
            } else {
                map.removeEnemy((Enemy) entity);
            }
        }
    }
}
