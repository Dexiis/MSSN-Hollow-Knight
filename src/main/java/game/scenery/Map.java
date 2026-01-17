package game.scenery;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.core.Terrain;
import game.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Map {
    private final PApplet p;

    private TheKnight player;
    private ArrayList<Terrain> terrains = new ArrayList<>();

    public Map(PApplet p) {//TODO ATUALMENTE RAWCODED - TESTE
        this.p = p;

        PVector center = new PVector(0, 100);
        terrains.add(new Terrain(center, 1280, 200, true));

//        center = new PVector(0, -200);
//        terrains.add(new Terrain(center, 1280, 200));

        this.player = new TheKnight(new PVector(0, 0));
    }

    public void display(SubPlot plt) {
        for (Terrain terrain : terrains) terrain.display(p, painter, plt);

        player.display(p, painter, plt);
    }

    LinePainter painter = new LinePainter() {
        @Override
        public void paintLine(float x1, float y1, float x2, float y2, SubPlot plt) {
            float[] p1 = plt.getPixelCoord(x1, y1);
            float[] p2 = plt.getPixelCoord(x2, y2);
            p.line(p1[0], p1[1], p2[0], p2[1]);
        }
    };

    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    public TheKnight getPlayer() {
        return player;
    }
}