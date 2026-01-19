package game.scenery;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.Aspids;
import game.scenery.characters.types.enemies.Crawlid;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Map {
    private final PApplet p;
    private final LinePainter painter;

    private final TheKnight player;
    private final ArrayList<Terrain> terrains = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>();

    public Map(PApplet p, LinePainter painter) {//TODO ATUALMENTE RAWCODED - TESTE
        this.p = p;
        this.painter = painter;

        terrains.add(new Terrain(new PVector(0, -100), 1000, 200));
        terrains.add(new Terrain(new PVector(0, 1100), 1000, 200));
        terrains.add(new Terrain(new PVector(600, 500), 200, 1200));
        terrains.add(new Terrain(new PVector(-600, 500), 200, 1200));
        terrains.add(new Terrain(new PVector(0, 300), 500, 100));

        this.player = new TheKnight(new PVector(0, 40), p);
        entities.add(player);

        enemies.add(new Aspids(new PVector(-100, 100)));
        entities.add(enemies.getLast());

        enemies.add(new Crawlid(new PVector(200, 200)));
        entities.add(enemies.getLast());
    }

    public void display(SubPlot plt) {
        for (Terrain terrain : terrains) terrain.display(p, painter, plt);
        for (Entity entity : entities) entity.display(p, painter, plt);
    }

    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy(Enemy enemy) {
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