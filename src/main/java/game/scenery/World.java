package game.scenery;

import game.core.SubPlot;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.FalseKnight;
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
public class World {
    private static World world = null;

    private final FalseKnight boss;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>();

    private final DeathPlatform deathPlatform;
    private final Roof roof;
    private final PApplet p;
    private final LinePainter painter;
    private final TheKnight player;
    private final ArrayList<Terrain> terrains = new ArrayList<>();
    private final ArrayList<BlackTerrain> blackTerrains = new ArrayList<>();
    private final ArrayList<Bush> bushes = new ArrayList<>();

    /**
     * Constrói o mapa do jogo.
     * <p>
     * Inicializa terrenos, jogador e inimigos.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto auxiliar para desenho de linhas
     */
    private World(PApplet p, LinePainter painter) {
        this.p = p;
        this.painter = painter;

        // Teto
        blackTerrains.add(new BlackTerrain(new PVector(4000, 2700), 10000, 1000, p));
        roof = new Roof(new PVector(4000, 2200), 10000, 1000, p);

        // Secção 1
        bushes.add(new Bush(new PVector(150, 75), p));
        terrains.add(new Wall(new PVector(-550, 1350), 1000, 2700, p, Wall.SIDE.RIGHT));
        terrains.add(new Floor(new PVector(450, -1000), 1000, 2000, p));
        terrains.add(new MidWall(new PVector(287, 940), 124, 1480, p));
        blackTerrains.add(new BlackTerrain(new PVector(-2650, -1350), 5200, 2700, p));

        // Plataformas
        terrains.add(new Platform(new PVector(765, 200), 270, 65, p));
        terrains.add(new Platform(new PVector(1365, 350), 270, 65, p));
        terrains.add(new Platform(new PVector(1865, 600), 270, 65, p));

        // Secção 2
        bushes.add(new Bush(new PVector(2600, 875), p));
        terrains.add(new Floor(new PVector(2700, 300), 1000, 1000, p));

        // Plataformas
        terrains.add(new Platform(new PVector(3565, 1000), 270, 65, p));
        terrains.add(new Platform(new PVector(4165, 1200), 270, 65, p));
        terrains.add(new Platform(new PVector(4790, 1325), 520, 65, p));
        terrains.add(new TrapDoor(new PVector(5275, 1283), 150, 150, p));

        // Death Platform
        deathPlatform = new DeathPlatform(new PVector(4000, -6500), 10000, 10000, p);

        // Spawn Platform
        terrains.add(new SpawnPlatform(new PVector(5500, -400), 100, 500, p));

        this.player = new TheKnight(new PVector(50, 800), p);
        //this.player = new TheKnight(new PVector(5300, -100), p);
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

    public static World getInstance() {
        if (world == null)
            System.out.println(Thread.currentThread().getStackTrace()[2] + ": É necessário inicializar o mapa primeiro");

        return world;
    }

    public synchronized static World init(PApplet p, LinePainter painter) {
        if (world != null)
            System.err.println("Mapa já foi inicializado");

        world = new World(p, painter);
        return world;
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
        // for (Entity entity : entities) entity.display(p, painter, plt);
        // for (Terrain terrain : terrains) terrain.display(p, painter, plt);
        player.display(p, painter, plt);
        deathPlatform.display(p, painter, plt);
        for (BlackTerrain blackTerrain : blackTerrains) blackTerrain.display(p, painter, plt);
        for (Bush bush : bushes) bush.display(p, painter, plt);
        roof.display(p, painter, plt);
    }

    /**
     * Remove um inimigo do jogo.
     * <p>
     * Elimina o inimigo das listas de inimigos e entidades.
     * </p>
     *
     * @param enemy o inimigo a ser removido
     */
    public void removeEnemy(Enemy enemy) {
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
     * Adiciona um terreno do mapa.
     *
     * @param terrain o terreno a ser adicionado
     */
    public void addTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    /**
     * Adiciona um terreno vazio do mapa.
     *
     * @param blackTerrain o terreno a ser adicionado
     */
    public void addBlackTerrain(BlackTerrain blackTerrain) {
        blackTerrains.add(blackTerrain);
    }

    /**
     * Adiciona o chefe às listas de inimigos e entidades.
     */
    public void spawnBoss() {
        enemies.add(boss);
        entities.add(boss);
    }

    public DeathPlatform getDeathPlatform() {
        return deathPlatform;
    }

    public Roof getRoof() {
        return roof;
    }
}