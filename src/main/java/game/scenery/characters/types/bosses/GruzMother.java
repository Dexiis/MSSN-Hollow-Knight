package game.scenery.characters.types.bosses;

import game.scenery.characters.IVisualizable;
import game.scenery.characters.types.Boss;
import game.core.SubPlot;
import game.scenery.hitbox.Hitbox;
import game.scenery.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class GruzMother extends Boss implements IVisualizable {

    public GruzMother(PVector position) {
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
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        //TODO Associar Sprites
    }
}