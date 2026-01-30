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
     * Representa uma armadilha que remove-se ao ser tocada pelo cavaleiro.
     * <p>
     * Permite passagem para inimigos, mas desaparece para o cavaleiro, atuando como
     * um mecanismo de progressão no jogo.
     * </p>
     *
     * @param center a posição central
     * @param width a largura
     * @param height a altura
     * @param map o mapa
     * @param p o contexto gráfico
     */
    public TrapDoor(PVector center, float width, float height, Map map, PApplet p) {
        super(center, width, height, p);
        this.map = map;
    }

    /**
     * Processa a interseção com uma entidade.
     * <p>
     * Remove a armadilha se for o cavaleiro, caso contrário processa normalmente.
     * </p>
     *
     * @param entity a entidade que interseta
     */
    @Override
    public void elaborateIntersects(Entity entity) {
        if (entity instanceof TheKnight) {
            if (super.intersected(entity.getHitbox())) map.removeTerrain(this);
        } else super.elaborateIntersects(entity);
    }
}
