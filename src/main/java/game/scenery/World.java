package game.scenery;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
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
 * Esta classe funciona como um contentor central de todos os elementos do nível,
 * incluindo terrenos, jogador, inimigos e chefe, sendo responsável pela sua
 * inicialização, gestão e visualização, seguindo o padrão singleton.
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
     * Cria e inicializa o mapa do jogo.
     * <p>
     * Define todos os terrenos, plataformas, elementos decorativos,
     * jogador, inimigos e chefe, bem como as respetivas relações e
     * comportamentos, preparando o nível para ser jogado.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto auxiliar para o desenho de linhas e hitboxes
     */
    private World(PApplet p, LinePainter painter) {
        this.p = p;
        this.painter = painter;

        // Teto
        blackTerrains.add(new BlackTerrain(new PVector(4000, 3000), 10000, 1000, p));
        roof = new Roof(new PVector(4000, 2500), 10000, 1000, p);

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
        terrains.add(new TrapDoor(new PVector(5575, 1283), 150, 150, p));

        // Death Platform
        deathPlatform = new DeathPlatform(new PVector(4000, -6500), 10000, 10000, p);

        // Spawn Platform
        terrains.add(new SpawnPlatform(new PVector(6200, -400), 100, 500, p));

        this.player = new TheKnight(new PVector(50, 800), p);
        entities.add(player);

        enemies.add(new Squit(new PVector(765, 500), p));
        entities.add(enemies.getLast());
        enemies.add(new Squit(new PVector(1365, 600), p));
        entities.add(enemies.getLast());
        enemies.add(new Squit(new PVector(1865, 1000), p));
        entities.add(enemies.getLast());

        enemies.add(new Squit(new PVector(3565, 1500), p));
        entities.add(enemies.getLast());
        enemies.add(new Squit(new PVector(4165, 1700), p));
        entities.add(enemies.getLast());
        enemies.add(new Squit(new PVector(4790, 1800), p));
        entities.add(enemies.getLast());

        enemies.add(new HuskHornhead(new PVector(300, 200), p));
        entities.add(enemies.getLast());
        enemies.add(new HuskHornhead(new PVector(2600, 875), p));
        entities.add(enemies.getLast());

        boss = new FalseKnight(new PVector(6800, 300), p);
        boss.setEye(new Eye(boss, player));

        for (Enemy enemy : enemies)
            enemy.setEye(new Eye(enemy, player));
    }

    /**
     * Devolve a instância atual do mapa.
     * <p>
     * Permite aceder à instância única do mundo do jogo, apresentando
     * uma mensagem de aviso caso ainda não tenha sido inicializada.
     * </p>
     *
     * @return a instância única de World
     */
    public static World getInstance() {
        if (world == null)
            System.out.println(Thread.currentThread().getStackTrace()[2] + ": É necessário inicializar o mapa primeiro");

        return world;
    }

    /**
     * Inicializa a instância única do mapa.
     * <p>
     * Cria o mundo do jogo caso ainda não exista, garantindo sincronização
     * para evitar múltiplas inicializações concorrentes.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto auxiliar para o desenho de linhas
     * @return a instância criada de World
     */
    public synchronized static World init(PApplet p, LinePainter painter) {
        if (world != null)
            System.err.println("Mapa já foi inicializado");

        world = new World(p, painter);
        return world;
    }

    /**
     * Devolve o chefe do nível.
     * <p>
     * Permite aceder à entidade que representa o inimigo principal
     * do mapa atual.
     * </p>
     *
     * @return o chefe do tipo FalseKnight
     */
    public FalseKnight getBoss() {
        return boss;
    }

    /**
     * Devolve a lista de inimigos do mapa.
     * <p>
     * Contém todas as entidades hostis atualmente presentes no nível,
     * excluindo o jogador.
     * </p>
     *
     * @return a lista de inimigos
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Devolve a lista de todas as entidades do mapa.
     * <p>
     * Inclui o jogador, inimigos e quaisquer outras entidades ativas
     * que interagem no mundo do jogo.
     * </p>
     *
     * @return a lista de entidades
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Devolve a referência para o jogador.
     * <p>
     * Permite aceder à instância do cavaleiro controlado pelo utilizador.
     * </p>
     *
     * @return a instância de TheKnight
     */
    public TheKnight getPlayer() {
        return player;
    }

    /**
     * Devolve a lista de terrenos sólidos.
     * <p>
     * Contém todos os terrenos com colisão ativa presentes no mapa.
     * </p>
     *
     * @return a lista de terrenos
     */
    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    /**
     * Desenha os elementos visuais principais do mapa.
     * <p>
     * Renderiza o jogador, plataformas letais, terrenos especiais,
     * elementos decorativos e o teto do nível.
     * </p>
     *
     * @param plt o objeto SubPlot utilizado para conversão de coordenadas
     */
    public void display(SubPlot plt) {
        player.display(p, painter, plt);
        deathPlatform.display(p, painter, plt);
        for (BlackTerrain blackTerrain : blackTerrains) blackTerrain.display(p, painter, plt);
        for (Bush bush : bushes) bush.display(p, painter, plt);
        roof.display(p, painter, plt);
    }

    /**
     * Remove um inimigo do mapa.
     * <p>
     * Elimina o inimigo das listas de inimigos e de entidades ativas
     * quando este deixa de estar presente no jogo.
     * </p>
     *
     * @param enemy o inimigo a remover
     */
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        entities.remove(enemy);
    }

    /**
     * Remove um terreno do mapa.
     * <p>
     * Elimina o terreno da lista de terrenos sólidos, deixando de
     * participar nas colisões e visualização.
     * </p>
     *
     * @param terrain o terreno a remover
     */
    public void removeTerrain(Terrain terrain) {
        terrains.remove(terrain);
    }

    /**
     * Adiciona um terreno sólido ao mapa.
     * <p>
     * Insere um novo terreno na lista de terrenos ativos,
     * passando este a integrar o cenário.
     * </p>
     *
     * @param terrain o terreno a adicionar
     */
    public void addTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    /**
     * Adiciona um terreno vazio ao mapa.
     * <p>
     * Insere um elemento de terreno sem colisão visível,
     * normalmente utilizado para limitar áreas do nível.
     * </p>
     *
     * @param blackTerrain o terreno vazio a adicionar
     */
    public void addBlackTerrain(BlackTerrain blackTerrain) {
        blackTerrains.add(blackTerrain);
    }

    /**
     * Adiciona o chefe ao mapa.
     * <p>
     * Insere o chefe nas listas de inimigos e entidades,
     * tornando-o ativo no jogo.
     * </p>
     */
    public void spawnBoss() {
        enemies.add(boss);
        entities.add(boss);
    }

    /**
     * Devolve a plataforma letal do mapa.
     * <p>
     * Permite aceder ao elemento responsável por eliminar entidades
     * que atinjam a zona inferior do nível.
     * </p>
     *
     * @return a plataforma letal
     */
    public DeathPlatform getDeathPlatform() {
        return deathPlatform;
    }

    /**
     * Devolve o teto do mapa.
     * <p>
     * Permite aceder ao elemento superior que limita o espaço
     * vertical do nível.
     * </p>
     *
     * @return o teto do mapa
     */
    public Roof getRoof() {
        return roof;
    }
}