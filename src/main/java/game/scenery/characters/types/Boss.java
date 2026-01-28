package game.scenery.characters.types;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Boss extends Enemy {

    protected Boss(PVector position, PApplet p) {
        super(position, p);
    }

}