package game.scenery.components.terraintypes;

import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import processing.core.PApplet;
import processing.core.PVector;

public class TrapDoor extends Terrain {
    private final Map map;

    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center A posição central do terreno.
     * @param width  A largura total.
     * @param height A altura total.
     */
    public TrapDoor(PVector center, float width, float height, Map map, PApplet p) {
        super(center, width, height, p);
        this.map = map;
    }

    @Override
    public void elaborateIntersects(Entity entity) {
        if (entity instanceof TheKnight) {
            if (super.intersected(entity.getHitbox())) map.removeTerrain(this);
        } else super.elaborateIntersects(entity);
    }
}
