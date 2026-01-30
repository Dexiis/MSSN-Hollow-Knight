package game.scenery;

import game.core.SubPlot;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.Mob;
import game.scenery.characters.types.enemies.attributes.Eye;
import game.scenery.characters.types.enemies.mobs.HuskHornhead;
import game.scenery.characters.types.enemies.mobs.Squit;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import game.scenery.components.terraintypes.*;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Representa o mapa ou nível do jogo.
 * <p>
 * Atua como contentor para terrenos, jogador e inimigos.
 * </p>
 */
public class Map {
    private final FalseKnight boss;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>();
    private final PApplet p;
    private final LinePainter painter;
    private final TheKnight player;
    private final ArrayList<Terrain> terrains = new ArrayList<>();

    /**
     * Constrói o mapa do jogo.
     * <p>
     * Inicializa terrenos, jogador e inimigos.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto auxiliar para desenho de linhas
     */
    public Map(PApplet p, LinePainter painter) {//TODO ATUALMENTE RAWCODED - TESTE
        this.p = p;
        this.painter = painter;

        // Secção 1
        terrains.add(new Wall(new PVector(-150, 400), 200, 1200, p));
        terrains.add(new Terrain(new PVector(450, -100), 1000, 200, p));
        terrains.add(new Terrain(new PVector(200, 600), 150, 800, p));

        // Plataformas
        terrains.add(new Platform(new PVector(765, 200), 270, 65, p));
        terrains.add(new Platform(new PVector(1365, 350), 270, 65, p));
        terrains.add(new Platform(new PVector(1865, 600), 270, 65, p));

        // Secção 2
        terrains.add(new Terrain(new PVector(2700, 300), 1000, 1000, p));

        // Plataformas
        terrains.add(new Platform(new PVector(3565, 1000), 270, 65, p));
        terrains.add(new Platform(new PVector(4165, 1200), 270, 65, p));
        terrains.add(new Platform(new PVector(4790, 1325), 520, 65, p));
        terrains.add(new TrapDoor(new PVector(5275, 1325), 150, 65, this, p));

        // Boss Room
        terrains.add(new Terrain(new PVector(6000, -800), 2500, 200, p));
        terrains.add(new Terrain(new PVector(4650, -500), 200, 800, p));
        terrains.add(new Terrain(new PVector(7350, -500), 200, 800, p));

        // Death Platform
        terrains.add(new DeathPlatform(new PVector(4000, -6500), 10000, 10000, this, p));

        // Spawn Platform
        terrains.add(new SpawnPlatform(new PVector(5500, -400), 100, 500, this, p));

        //this.player = new TheKnight(new PVector(50, 800), p);
        this.player = new TheKnight(new PVector(5700, -100), p);
        entities.add(player);

        enemies.add(new Squit(new PVector(500, 500), p));
        entities.add(enemies.getLast());

        enemies.add(new HuskHornhead(new PVector(300, 200), p));
        entities.add(enemies.getLast());

        boss = new FalseKnight(new PVector(6100, 300), p);
        boss.setEye(new Eye(boss, player));

        for (Enemy enemy : enemies)
            enemy.setEye(new Eye(enemy, player));
    }

    /**
     * Devolve o chefe.
     *
     * @return o FalseKnight chefe
     */
    public FalseKnight getBoss() {
        return boss;
    }

    /**
     * Obtém a lista de inimigos.
     *
     * @return a lista de objetos Enemy
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Obtém a lista de todas as entidades.
     *
     * @return a lista de objetos Entity
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Obtém a referência para o jogador.
     *
     * @return a instância de TheKnight
     */
    public TheKnight getPlayer() {
        return player;
    }

    /**
     * Obtém a lista de terrenos.
     *
     * @return a lista de objetos Terrain
     */
    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    /**
     * Renderiza todos os elementos do mapa.
     *
     * @param plt o objeto SubPlot para conversão de coordenadas
     */
    public void display(SubPlot plt) {
        for (Entity entity : entities) entity.display(p, painter, plt);
        for (Terrain terrain : terrains) terrain.display(p, painter, plt);
    }

    /**
     * Remove um inimigo do jogo.
     * <p>
     * Elimina o inimigo das listas de inimigos e entidades.
     * </p>
     *
     * @param enemy o inimigo a ser removido
     */
    public void removeEnemy(Mob enemy) {
        enemies.remove(enemy);
        entities.remove(enemy);
    }

    /**
     * Remove um terreno do mapa.
     *
     * @param terrain o terreno a ser removido
     */
    public void removeTerrain(Terrain terrain) {
        terrains.remove(terrain);
    }

    /**
     * Adiciona o chefe às listas de inimigos e entidades.
     */
    public void spawnBoss() {
        enemies.add(boss);
        entities.add(boss);
    }
}