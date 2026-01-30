package game.scenery.components.terraintypes;

import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.Mob;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import processing.core.PApplet;
import processing.core.PVector;

public class DeathPlatform extends Terrain {
    private final Map map;

    /**
     * Representa um terreno letal que causa dano ou remove entidades.
     * <p>
     * Ao interagir com entidades, danifica o cavaleiro ou remove inimigos do mapa.
     * </p>
     */
    public DeathPlatform(PVector center, float width, float height, Map map, PApplet p) {
        super(center, width, height, p);
        this.map = map;
    }

    /**
     * Processa a interseção com uma entidade.
     * <p>
     * Chama o método específico e o da superclasse.
     * </p>
     *
     * @param entity a entidade que interseta
     */
    @Override
    public void elaborateIntersects(Entity entity) {
        this.intersected(entity);
        super.elaborateIntersects(entity);
    }


    /**
     * Gere a interseção específica com o terreno letal.
     * <p>
     * Danifica o cavaleiro ou remove inimigos do mapa.
     * </p>
     *
     * @param entity a entidade que interseta
     */
    public void intersected(Entity entity) {
        if (super.intersected(entity.getHitbox())) {
            if (entity instanceof TheKnight) {
                entity.damage(p);
                entity.setPosition(new PVector(50, 800));
                entity.setVelocity(new PVector(0, 0));
                entity.setAcceleration(new PVector(0, 0));
            } else {
                map.removeEnemy((Mob) entity);
            }
        }
    }
}
