package game.scenery;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.hitbox.Hitbox;
import game.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Map {
    private Terrain ground;
    private TheKnight player;

    public Map() { //TODO ATUALMENTE RAWCODED - TESTE
        // Criação do chão (Ground)
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1280, 0));
        points.add(new Point(1280, 50));
        points.add(new Point(0, 50));

        this.ground = new Terrain(points);
        this.ground.setPosition(new Point(0, 600));

        this.player = new TheKnight(new PVector(100, 100));
    }

    public void display(PApplet p, SubPlot plt) {
        ground.display(p, plt);

        player.display(p, plt);
    }

    public Terrain getGround() {
        return ground;
    }

    public TheKnight getPlayer() {
        return player;
    }
}