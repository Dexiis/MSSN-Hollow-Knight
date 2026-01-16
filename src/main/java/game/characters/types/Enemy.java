package game.characters.types;

import game.characters.Entity;
import processing.core.PVector;

public class Enemy extends Entity {

    protected Enemy(PVector position) {
        super(position);
        this.mass = 1; //Todo Escolher a massa
    }

}
