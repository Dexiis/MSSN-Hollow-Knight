package game.scenery.characters.types;

import game.scenery.characters.Entity;
import processing.core.PVector;

public abstract class Enemy extends Entity {

    protected Enemy(PVector position) {
        super(position);
        this.mass = 1; //Todo Escolher a massa
    }

}
