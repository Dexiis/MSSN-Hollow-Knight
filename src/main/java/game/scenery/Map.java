package game.scenery;

import game.characters.hitbox.Hitbox;
import game.characters.hitbox.LinePainter;
import game.characters.hitbox.Point;

import java.util.ArrayList;

public class Map {
    Hitbox ground;

    public Map() { // TODO Fazer mapa + entidades (Isto é um teste hardcoded por enquanto)
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1280, 0));
        points.add(new Point(1280, 50));
        points.add(new Point(0, 50));
        this.ground = new Hitbox(points);
        this.ground.setPosition(new Point(0, 600));
    }

    public void draw(LinePainter painter) {
        ground.draw(painter);
    }

    public Hitbox getGround() {
        return ground;
    }
}