package game.scenery;

import game.hitbox.Hitbox;
import game.hitbox.Point;

import java.util.List;

public class Terrain extends Hitbox {
    public Terrain(List<Point> points) {
        super(points);
    }

    @Override
    public boolean intersects(Hitbox h) {
        //TODO FAZER O MÉTODO COM FORMULAS MATEMATICAS SIMPLES
        return false;
    }
}
