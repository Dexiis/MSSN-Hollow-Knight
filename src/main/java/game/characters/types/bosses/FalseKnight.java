package game.characters.types.bosses;

import game.characters.types.Boss;
import game.core.SubPlot;
import game.hitbox.Hitbox;
import game.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class FalseKnight extends Boss {

    public FalseKnight(PVector position) {
        super(position);

        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(20, 0));
        points.add(new Point(20, 20));
        points.add(new Point(0, 20)); //TODO Escolher o tamanho

        this.hitbox = new Hitbox(points);
        this.health = 1; //Todo Escolher a vida
    }

    @Override
    public void display(PApplet p, SubPlot plt) {
        //TODO Associar Sprites
    }

}