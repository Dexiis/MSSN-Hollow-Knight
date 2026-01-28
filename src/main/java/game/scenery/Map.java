package game.scenery;

import game.core.SubPlot;
import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Eye;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.bosses.FalseKnight;
import game.scenery.characters.types.enemies.Squit;
import game.scenery.characters.types.enemies.HuskHornhead;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Representa o mapa ou nível do jogo.
 * <p>
 * Esta classe atua como um contentor para todos os elementos do cenário, incluindo o terreno,
 * o jogador e os inimigos. É responsável por inicializar a disposição do nível e gerir
 * as listas de entidades ativas.
 */
public class Map {
    private final PApplet p;
    private final LinePainter painter;

    private final TheKnight player;
    private final ArrayList<Terrain> terrains = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>();

    /**
     * Construtor da classe Map.
     * <p>
     * Inicializa o ambiente de jogo, criando instâncias de terreno (paredes e chão),
     * posicionando o jogador e gerando os inimigos iniciais com os seus respetivos sensores de visão.
     *
     * @param p       O contexto gráfico do Processing.
     * @param painter O objeto auxiliar para desenho de linhas (debug ou visualização).
     */
    public Map(PApplet p, LinePainter painter) {//TODO ATUALMENTE RAWCODED - TESTE
        this.p = p;
        this.painter = painter;

        // Secção 1
        terrains.add(new Terrain(new PVector(-150, 400), 200, 1200));
        terrains.add(new Terrain(new PVector(450, -100), 1000, 200));
        terrains.add(new Terrain(new PVector(200, 600), 150, 800));

        // Plataformas
        terrains.add(new Terrain(new PVector(700, 200), 400, 75));
        terrains.add(new Terrain(new PVector(1300, 350), 400, 75));
        terrains.add(new Terrain(new PVector(1800, 600), 400, 75));

        // Secção 2
        terrains.add(new Terrain(new PVector(2700, 300), 1000, 1000));

        // Plataformas
        terrains.add(new Terrain(new PVector(3500, 1000), 400, 75));
        terrains.add(new Terrain(new PVector(4100, 1200), 400, 75));
        terrains.add(new Terrain(new PVector(4750, 1325), 600, 75));
        terrains.add(new Terrain(new PVector(5275, 1325), 150, 75));

        // Boss Room
        terrains.add(new Terrain(new PVector(6000, -100), 2500, 200));
        terrains.add(new Terrain(new PVector(4650, 200), 200, 800));
        terrains.add(new Terrain(new PVector(7350, 200), 200, 800));

        this.player = new TheKnight(new PVector(50, 800), p);
        entities.add(player);

        enemies.add(new Squit(new PVector(-100, 100), p));
        entities.add(enemies.getLast());

        enemies.add(new HuskHornhead(new PVector(50, 1000), p));
        entities.add(enemies.getLast());

        enemies.add(new FalseKnight(new PVector(6100, 300), p));
        entities.add(enemies.getLast());

        for (Enemy enemy : enemies)
            enemy.setEye(new Eye(enemy, player));
    }

    /**
     * Obtém a lista de todos os obstáculos e superfícies (terrenos) presentes no mapa.
     *
     * @return Uma lista de objetos {@link Terrain}.
     */
    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    /**
     * Obtém a lista de inimigos ativos no mapa.
     *
     * @return Uma lista de objetos {@link Enemy}.
     */
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Obtém a lista completa de todas as entidades (jogador e inimigos) presentes no mapa.
     *
     * @return Uma lista de objetos {@link Entity}.
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * Obtém a referência para o jogador principal (The Knight).
     *
     * @return A instância de {@link TheKnight}.
     */
    public TheKnight getPlayer() {
        return player;
    }

    /**
     * Remove um inimigo específico do jogo.
     * <p>
     * Elimina o inimigo tanto da lista de inimigos como da lista geral de entidades,
     * cessando a sua atualização e renderização.
     *
     * @param enemy O inimigo a ser removido.
     */
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        entities.remove(enemy);
    }

    /**
     * Renderiza todos os elementos do mapa no ecrã.
     * <p>
     * Itera sobre as listas de terrenos e entidades para invocar os seus respetivos métodos de desenho,
     * utilizando o sistema de coordenadas fornecido pelo SubPlot.
     *
     * @param plt O objeto SubPlot para conversão de coordenadas (Mundo para Pixel).
     */
    public void display(SubPlot plt) {
        for (Terrain terrain : terrains) terrain.display(p, painter, plt);
        for (Entity entity : entities) entity.display(p, painter, plt);
    }
}