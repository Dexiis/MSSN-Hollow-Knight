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
 * Classe principal responsável pela execução do jogo.
 * <p>
 * Gere o ciclo de vida da aplicação, incluindo inicialização,
 * atualização do estado, processamento de entradas e desenho
 * de todos os elementos visuais.
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

    private static boolean paused = false;

    private final LinePainter painter = new LinePainter() {

        /**
         * Desenha uma linha no ecrã a partir de coordenadas do mundo.
         * <p>
         * Converte as coordenadas do espaço do jogo para coordenadas
         * em píxeis antes de efetuar o desenho gráfico.
         * </p>
         *
         * @param x1 coordenada X inicial no mundo
         * @param y1 coordenada Y inicial no mundo
         * @param x2 coordenada X final no mundo
         * @param y2 coordenada Y final no mundo
         * @param plt objeto responsável pela conversão de coordenadas
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
     * Define as configurações iniciais da janela da aplicação.
     * <p>
     * Estabelece a resolução gráfica e desativa suavização
     * para melhorar o desempenho.
     * </p>
     */
    @Override
    public void settings() {
        size(1600, 900);
        // MELHORA PERFORMANCE
        noSmooth();
    }

    /**
     * Executa a configuração inicial do jogo.
     * <p>
     * Inicializa o sistema de coordenadas, cria o mapa,
     * a interface gráfica e o fundo animado.
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
     * Controla o ciclo principal de execução.
     * <p>
     * Atualiza o tempo, gere movimentos, ataques,
     * colisões e apresenta todos os elementos visuais.
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

        for (Enemy enemy : map.getEnemies()) {
            float dist = PVector.dist(map.getPlayer().getPosition(), enemy.getPosition());
            if (enemy instanceof FalseKnight) {
                if (dist < 2000) loadedEnemies.add(enemy);
            } else if (dist < 1500) loadedEnemies.add(enemy);
        }

        for (Terrain terrain : map.getTerrains()) {
            float dist = PVector.dist(map.getPlayer().getPosition(), terrain.getPosition().toPVector());
            if (dist < 3000) loadedTerrains.add(terrain);
        }

        moveFlock(background.getFlock(), dt);

        handleNaturalMovements(dt);
        handleMonstersAttacks();

        player.move(dt);

        //Chekar mortes
        for (int i = loadedEnemies.size() - 1; i >= 0; i--) {
            Enemy enemy = loadedEnemies.get(i);

            enemy.move(dt);
            if (enemy.isDead()) map.removeEnemy(enemy);
        }

        if (player.isDead()) {
            fill(255);
            text("Game Over", width / 2f, height / 2f);
            noLoop();
        }

        checkCollisions();

        //Displays
        setWindow(player.getPosition());
        background.display(player.getPosition());
        for (Terrain terrain : loadedTerrains) terrain.display(this, painter, plt);
        for (Enemy enemy : loadedEnemies) enemy.display(this, painter, plt);
        map.display(plt);
        gui.display(map.getPlayer());
    }

    /**
     * Processa o pressionamento de teclas.
     * <p>
     * Atualiza as direções de movimento do jogador,
     * ativa ataques e permite pausar a execução.
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
        if (key == 'p' || key == 'P') {
            if (!paused) {
                noLoop();
                paused = true;
            } else {
                loop();
                paused = false;
                now = millis();
                lastUpdateTime = now;
            }
        }

        if (key == ENTER || key == RETURN) player.playerAttack();
    }

    /**
     * Processa a libertação de teclas.
     * <p>
     * Interrompe as direções de movimento previamente ativadas.
     * </p>
     */
    @Override
    public void keyReleased() {
        if (key == 'w' || key == 'W' || key == ' ') player.setMovingDirection(KnightMovement.UPRELEASED, true);
        if (key == 's' || key == 'S') player.setMovingDirection(KnightMovement.DOWN, false);
        if (key == 'a' || key == 'A') player.setMovingDirection(KnightMovement.LEFT, false);
        if (key == 'd' || key == 'D') player.setMovingDirection(KnightMovement.RIGHT, false);
    }

    /**
     * Processa interações com o rato.
     * <p>
     * Permite teletransporte do jogador ou execução de ataque,
     * consoante o botão pressionado.
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

    /**
     * Atualiza o comportamento do bando.
     * <p>
     * Aplica as regras de movimento coletivo a cada entidade
     * com base no intervalo de tempo.
     * </p>
     *
     * @param flock conjunto de entidades do bando
     * @param dt    intervalo de tempo desde a última atualização
     */
    private void moveFlock(ArrayList<Flock> flock, float dt) {
        for (int i = flock.size() - 1; i >= 0; i--)
            flock.get(i).applyBehaviours(dt);
    }

    /**
     * Calcula a força gravitacional aplicada a uma entidade.
     * <p>
     * A força depende da massa e é limitada por uma velocidade
     * terminal definida.
     * </p>
     *
     * @param entity entidade alvo da força
     * @return vetor que representa a força da gravidade
     */
    private PVector gravity(Entity entity) {
        if (entity.getAcceleration().y < 12.8f) return new PVector(0, -1280 * entity.getMass());
        else return new PVector(0, 0);
    }

    /**
     * Processa ataques e interações ofensivas dos inimigos.
     * <p>
     * Verifica colisões entre inimigos e o jogador
     * para aplicar dano quando apropriado.
     * </p>
     */
    private void handleMonstersAttacks() {
        if (loadedEnemies != null) for (Enemy enemy : loadedEnemies)
            if (enemy.getHitbox().intersected(player.getHitbox()) && !enemy.isDying())
                player.damage(enemy.getPosition());
    }

    /**
     * Atualiza movimentos naturais das entidades.
     * <p>
     * Aplica forças físicas, atualiza tempo interno
     * e trata comportamentos especiais de inimigos.
     * </p>
     *
     * @param dt intervalo de tempo desde a última atualização
     */
    private void handleNaturalMovements(float dt) {
        player.updateTime(dt, now);
        player.applyForce(gravity(player));

        for (Enemy enemy : loadedEnemies) {
            enemy.updateTime(dt, now);

            if (enemy instanceof Squit) {
                if (enemy.isDying())
                    enemy.applyForce(new PVector(0, -450 * enemy.getMass())); // Queda na morte do Squit
            } else enemy.applyForce(gravity(enemy));
        }
    }

    /**
     * Verifica e resolve colisões físicas.
     * <p>
     * Garante que entidades não atravessam
     * terrenos, teto ou plataformas letais.
     * </p>
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
     * Atualiza a área visível do jogo.
     * <p>
     * Ajusta a câmara para acompanhar a posição
     * atual do jogador.
     * </p>
     *
     * @param playerPosition posição atual do jogador no mundo
     */
    private void setWindow(PVector playerPosition) {
        window[0] = playerPosition.x - 800;
        window[1] = playerPosition.x + 800;
        window[2] = playerPosition.y - 450;
        window[3] = playerPosition.y + 450;

        plt.setWindow(window);
    }
}