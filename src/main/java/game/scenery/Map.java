package game.scenery;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.Aspids;
import game.scenery.components.Terrain;
import game.scenery.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Map {
    private final PApplet p;
    private final LinePainter painter;

    private TheKnight player;
    private Aspids monster;
    private ArrayList<Terrain> terrains = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();

    public Map(PApplet p, LinePainter painter) {//TODO ATUALMENTE RAWCODED - TESTE
        this.p = p;
        this.painter = painter;

        PVector center = new PVector(0, 200);
        terrains.add(new Terrain(center, 1280, 200));

        center = new PVector(0, -200);
        terrains.add(new Terrain(center, 1280, 200));

        this.player = new TheKnight(new PVector(0, 0));
        entities.add(player);

        enemies.add(new Aspids(new PVector(0, 0)));
        entities.add(enemies.getLast());
    }

    public void display(SubPlot plt) {
        for (Terrain terrain : terrains) terrain.display(p, painter, plt);
        for (Entity entity: entities) entity.display(p, painter, plt);

    }

    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy (Enemy enemy){
        enemies.remove(enemy);
        entities.remove(enemy);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public TheKnight getPlayer() {
        return player;
    }
}