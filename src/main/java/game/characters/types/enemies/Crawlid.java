package game.characters.types.enemies;

import game.characters.IVisualizable;
import game.characters.types.Enemy;
import game.core.SubPlot;
import game.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Crawlid extends Enemy implements IVisualizable {

    public Crawlid(PVector position, PVector velocity, float mass, float radius, int color) {
        super(position);

        ArrayList<PVector> points = new ArrayList<>();
        points.add(new PVector(0, 0));
        points.add(new PVector(20, 0));
        points.add(new PVector(20, 20));
        points.add(new PVector(0, 20)); //TODO Escolher o tamanho

        this.hitbox = new Hitbox(points);
        this.health = 1; //Todo Escolher a vida
    }

    @Override
    public void display(PApplet p, SubPlot plt) {
        //TODO Associar Sprites
    }
}