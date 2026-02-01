package game;

import game.core.SubPlot;
import game.scenery.Background;
import game.scenery.GUI;
import game.scenery.World;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.KnightMovement;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.mobs.Squit;
import game.scenery.components.Terrain;
import game.scenery.components.flock.Flock;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Classe principal do jogo.
 * <p>
 * Controla o loop principal, inicialização, atualização e renderização do jogo.
 * </p>
 */
public class Game extends PApplet {
    private final float[] viewport = {0f, 0f, 1f, 1f};
    private final double[] window = {-800, 800, -450, 450};

    private ArrayList<Terrain> loadedTerrains = new ArrayList<>();
    private ArrayList<Enemy> loadedEnemies = new ArrayList<>();

    private Background background;
    private GUI gui;
    private World map;

    private final LinePainter painter = new LinePainter() {
        /**
         * Desenha uma linha convertendo coordenadas do mundo para pixels.
         *
         * @param x1 coordenada X inicial no mundo
         * @param y1 coordenada Y inicial no mundo
         * @param x2 coordenada X final no mundo
         * @param y2 coordenada Y final no mundo
         * @param plt o objeto SubPlot para conversão de coordenadas
         */
        @Override
        public void paintLine(float x1, float y1, float x2, float y2, SubPlot plt) {
            float[] p1 = plt.getPixelCoord(x1, y1);
            float[] p2 = plt.getPixelCoord(x2, y2);
            stroke(255);
            line(p1[0], p1[1], p2[0], p2[1]);
        }
    };
    private SubPlot plt;
    private TheKnight player;

    private float lastUpdateTime;
    public int now = millis();
    private boolean firstLoop = true;

    /**
     * Define as configurações iniciais da janela.
     * <p>
     * Define a resolução da janela para 1600x900 pixels.
     * </p>
     */
    @Override
    public void settings() {
        size(1600, 900);
    }

    /**
     * Executa a configuração inicial do jogo.
     * <p>
     * Inicializa o sistema de coordenadas, mapa, GUI e fundo.
     * </p>
     */
    @Override
    public void setup() {
        plt = new SubPlot(window, viewport, width, height);

        textSize(22);

        map = World.init(this, painter);
        gui = GUI.init(this);
        background = Background.init(this);

        player = map.getPlayer();
    }

    /**
     * Executa o ciclo principal do jogo.
     * <p>
     * Atualiza o tempo, fundo, movimentos, ataques, colisões e renderiza tudo.
     * </p>
     */
    @Override
    public void draw() {
        if (firstLoop) {
            lastUpdateTime = millis();
            firstLoop = false;
        }
        now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;

        loadedTerrains = new ArrayList<>();
        loadedEnemies = new ArrayList<>();

        for(Enemy enemy : map.getEnemies()) {
            float dist = PVector.dist(map.getPlayer().getPosition(), enemy.getPosition());
            if(enemy instanceof FalseKnight) {
                if (dist < 1200) loadedEnemies.add(enemy);
            } else {
                if (dist < 800) loadedEnemies.add(enemy);
            }
        }

        for (Terrain terrain : map.getTerrains()) {
            float dist = PVector.dist(map.getPlayer().getPosition(), terrain.getPosition().toPVector());
            if (dist < 2000) loadedTerrains.add(terrain);
        }

        background.display(player.getPosition());
        moveFlock(background.getFlock(), dt);

        handleNaturalMovements(dt);
        handleMonstersAttacks();

        player.move(dt);

        for (int i = loadedEnemies.size() - 1; i >= 0; i--) {
            Enemy enemy = loadedEnemies.get(i);

            enemy.move(dt);
            if (enemy.isDead()) map.removeEnemy(enemy);
        }

        checkCollisions();

        for (Enemy enemy : loadedEnemies) {
            enemy.getEye().display(this, plt); // DEBUGGING - TODO RETIRAR MAIS TARDE
        }

        setWindow(player.getPosition());
        for (Terrain terrain : loadedTerrains) terrain.display(this, painter, plt);
        for (Enemy enemy : loadedEnemies) enemy.display(this, painter, plt);
        map.display(plt);
        gui.display(map.getPlayer());
    }

    /**
     * Processa o pressionamento de teclas.
     * <p>
     * Atualiza as direções de movimento do jogador ou inicia ataques.
     * </p>
     */
    @Override
    public void keyPressed() { // ACONTEÇA O QUE ACONTECER, NÃO MEXER NESTE MÉT.ODO. A LÓGICA NÃO É FEITA AQUI
        if (key == 'w' || key == 'W' || key == ' ') {
            player.setMovingDirection(KnightMovement.UPRELEASED, false);
            player.setMovingDirection(KnightMovement.UP, true);
        }
        if (key == 's' || key == 'S') player.setMovingDirection(KnightMovement.DOWN, true);
        if (key == 'a' || key == 'A') player.setMovingDirection(KnightMovement.LEFT, true);
        if (key == 'd' || key == 'D') player.setMovingDirection(KnightMovement.RIGHT, true);

        if (key == ENTER || key == RETURN) player.playerAttack();
    }

    /**
     * Processa a libertação de teclas.
     * <p>
     * Para as direções de movimento do jogador.
     * </p>
     */
    @Override
    public void keyReleased() { // ACONTEÇA O QUE ACONTECER, NÃO MEXER NESTE MÉT.ODO. A LÓGICA NÃO É FEITA AQUI
        if (key == 'w' || key == 'W' || key == ' ') player.setMovingDirection(KnightMovement.UPRELEASED, true);
        if (key == 's' || key == 'S') player.setMovingDirection(KnightMovement.DOWN, false);
        if (key == 'a' || key == 'A') player.setMovingDirection(KnightMovement.LEFT, false);
        if (key == 'd' || key == 'D') player.setMovingDirection(KnightMovement.RIGHT, false);
        if (key == 'm' || key == 'M') player.setPosition(new PVector(0, 50)); // DEBUGGING - TODO RETIRAR MAIS TARDE
    }

    /**
     * Processa cliques do mouse.
     * <p>
     * Teleporta o jogador para a posição clicada ou inicia ataque.
     * </p>
     */
    @Override
    public void mousePressed() {
        if (mouseButton == RIGHT) {
            double[] w = plt.getWorldCoord(mouseX, mouseY);
            player.setPosition(new PVector((float) w[0], (float) w[1]));
            player.setVelocity(new PVector(0, 0));
        } else if (mouseButton == LEFT) player.playerAttack();
    }

    private void moveFlock(ArrayList<Flock> flock, float dt) {
        for (int i = flock.size() - 1; i >= 0; i--)
            flock.get(i).applyBehaviours(dt);
    }

    /**
     * Calcula a força gravitacional a ser aplicada a uma entidade.
     *
     * @param entity A entidade aonde será aplicada a gravidade.
     * @return Um vetor {@link PVector} representando a força da gravidade (ou vetor nulo se a velocidade terminal for atingida).
     */
    private PVector gravity(Entity entity) {
        if (entity.getAcceleration().y < 12.8f) return new PVector(0, -1280 * entity.getMass());
        else return new PVector(0, 0);
    }

    /**
     * Atualiza o comportamento e ataques dos inimigos.
     * <p>
     * Executa a inteligência artificial (Behaviour) de cada inimigo e verifica
     * colisão física entre o inimigo e o jogador para aplicar dano ao jogador.
     */
    private void handleMonstersAttacks() {
        if (loadedEnemies != null) for (Enemy enemy : loadedEnemies)
            if (enemy.getHitbox().intersected(player.getHitbox()) && !enemy.isDying())
                player.damage(enemy.getPosition());
    }

    private void handleNaturalMovements(float dt) {
        player.updateTime(dt, now);
        player.applyForce(gravity(player));

        for (Enemy enemy : loadedEnemies) {
            enemy.updateTime(dt, now);

            if (enemy instanceof Squit) {
                if (enemy.isDying())
                    enemy.applyForce(new PVector(0, -450 * enemy.getMass())); // Queda na morte do Squit
            } else if (enemy instanceof FalseKnight)
                enemy.applyForce(new PVector(0, -350 * enemy.getMass())); // Gravidade apenas para o boss
            else enemy.applyForce(gravity(enemy));
        }
    }

    /**
     * Verifica e resolve colisões entre entidades e o terreno.
     * <p>
     * Itera sobre todas as entidades e todos os elementos de terreno para impedir
     * que as entidades atravessem paredes ou chão.
     */
    private void checkCollisions() {
        player.setGrounded(false);
        map.getBoss().setGrounded(false);
        map.getBoss().setWalled(false);
        for (int i = map.getEntities().size() - 1; i >= 0; i--) {
            Entity entity = map.getEntities().get(i);
            if (entity instanceof Squit) ((Squit) entity).setColliding(false);
            World.getInstance().getDeathPlatform().elaborateIntersects(entity);
            World.getInstance().getRoof().elaborateIntersects(entity);
            for (int j = loadedTerrains.size() - 1; j >= 0; j--) {
                Terrain terrain = loadedTerrains.get(j);
                terrain.elaborateIntersects(entity);
            }
        }
    }

    /**
     * Atualiza a janela de visualização (Câmara) para seguir o jogador.
     *
     * @param playerPosition A posição atual do jogador no mundo.
     */
    private void setWindow(PVector playerPosition) {
        window[0] = playerPosition.x - 800; // Esquerda
        window[1] = playerPosition.x + 800; // Direita
        window[2] = playerPosition.y - 450; // Topo (Menor valor)
        window[3] = playerPosition.y + 450; // Fundo (Maior valor)

        plt.setWindow(window);
    }
}