package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Behaviour;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class Enemy extends Entity {

    protected ArrayList<Behaviour> behaviours = new ArrayList<>();

    protected Enemy(PVector position) {
        super(position);
        this.mass = 1; //Todo Escolher a massa
    }

    public ArrayList<Behaviour> getBehaviours() {
        return this.behaviours;
    }

}
