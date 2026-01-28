package game.scenery.components.terrainvariables;

import game.scenery.components.Terrain;
import processing.core.PVector;

public class DeathFloor extends Terrain {


    /**
     * Constrói um objeto de terreno retangular.
     *
     * @param center A posição central do terreno.
     * @param width  A largura total.
     * @param height A altura total.
     */
    public DeathFloor(PVector center, float width, float height) {
        super(center, width, height);
    }

    public void intersects(){

    }
}
