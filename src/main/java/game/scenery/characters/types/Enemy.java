package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

public abstract class Enemy extends Entity {

    protected Behaviour behaviour;

    protected Enemy(PVector position) {
        super(position);
        this.mass = 1; //Todo Escolher a massa
    }

    public Behaviour getBehaviour() {
        return this.behaviour;
    }

}
