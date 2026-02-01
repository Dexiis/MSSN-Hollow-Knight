package game.scenery.components.terraintypes;

import game.scenery.World;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import processing.core.PApplet;
import processing.core.PVector;

public class SpawnPlatform extends Terrain {

    /**
     * Representa uma plataforma que ativa o spawn do chefe.
     * <p>
     * Ao ser tocada pelo cavaleiro, remove-se e inicia o combate com o chefe.
     * </p>
     */
    public SpawnPlatform(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);
    }

    /**
     * Processa a interseção com uma entidade.
     * <p>
     * Se for o cavaleiro, remove a plataforma e spawna o chefe.
     * </p>
     *
     * @param entity a entidade que interseta
     */
    @Override
    public void elaborateIntersects(Entity entity) {
        if (entity instanceof TheKnight) {
            if (super.intersected(entity.getHitbox())) {
                World.getInstance().removeTerrain(this);
                World.getInstance().spawnBoss();
            }
        }
    }
}
