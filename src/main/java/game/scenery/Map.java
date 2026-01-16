package game.scenery;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.core.Terrain;
import processing.core.PApplet;
import processing.core.PVector;

public class Map {
    private Terrain ground;
    private TheKnight player;

    public Map() { //TODO ATUALMENTE RAWCODED - TESTE
        PVector center = new PVector(0, 400);
        this.ground = new Terrain(center, 1280, 200);

        this.player = new TheKnight(new PVector(0, 0));
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