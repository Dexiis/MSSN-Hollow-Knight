package game.characters.types;

import game.characters.Entity;
import processing.core.PVector;

public class Boss extends Entity {

    protected Boss(PVector posisiton) {
        super(posisiton);
        this.mass = 1; //Todo Escolher a massa
    }

}