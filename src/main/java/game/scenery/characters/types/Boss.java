package game.scenery.characters.types;

import game.scenery.characters.Entity;
import processing.core.PVector;

public abstract class Boss extends Entity {

    protected Boss(PVector posisiton) {
        super(posisiton);
        this.mass = 1; //Todo Escolher a massa
    }

}