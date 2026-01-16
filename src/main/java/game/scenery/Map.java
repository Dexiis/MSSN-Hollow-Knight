package game.scenery;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.core.Terrain;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Map {
    private TheKnight player;

    private ArrayList<Terrain> terrains = new ArrayList<>();

    public Map() { //TODO ATUALMENTE RAWCODED - TESTE
        PVector center = new PVector(0, 100);
        terrains.add(new Terrain(center, 1280, 200));
        center = new PVector(0, -200);
        terrains.add(new Terrain(center, 1280, 200));

        this.player = new TheKnight(new PVector(0, 0));
    }

    public void display(PApplet p, SubPlot plt) {
        for (Terrain terrain : terrains) terrain.display(p, plt);

        player.display(p, plt);
    }

    public ArrayList<Terrain> getTerrain() {
        return terrains;
    }

    public TheKnight getPlayer() {
        return player;
    }
}