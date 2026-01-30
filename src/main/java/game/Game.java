package game;

import game.core.SubPlot;
import game.scenery.Background;
import game.scenery.GUI;
import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.KnightMovement;
import game.scenery.characters.types.State;
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

    private Background background;
    private GUI gui;
    private Map map;
    private LinePainter painter = new LinePainter() {
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

        map = new Map(this, painter);
        gui = new GUI(this);
        background = new Background(this);

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

        background.display(player.getPosition());
        moveFlock(background.getFlock(), dt);

        handleNaturalMovements(dt);
        if (!player.isStunned()) handleInputMovements();
        handleKnightAttack();
        handleMonstersAttacks();

        for (int i = map.getEntities().size() - 1; i >= 0; i--) {
            Entity entity = map.getEntities().get(i);

            entity.move(dt);
            if (entity.isDead()) map.removeEnemy((Enemy) entity);
        }

        checkCollisions();

        for (Enemy enemy : map.getEnemies()) {
            enemy.getEye().display(this, plt); // DEBUGGING - TODO RETIRAR MAIS TARDE
        }

        setWindow(player.getPosition());
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
     * Gere a lógica de movimento do jogador baseada nas entradas (inputs).
     * <p>
     * Controla a máquina de estados de movimento (IDLE, RUNNING, JUMPING, FALLING)
     * e aplica as ações correspondentes como mover para os lados ou saltar.
     */
    private void handleInputMovements() {
        if ((!(player.getDirections().get(KnightMovement.RIGHT)) && !(player.getDirections().get(KnightMovement.LEFT))) || (player.getDirections().get(KnightMovement.RIGHT)) && (player.getDirections().get(KnightMovement.LEFT))) {
            player.stopMovement();
            player.setMovement(State.IDLE);
        } else {
            if (player.getDirections().get(KnightMovement.RIGHT)) {
                player.setLastFacingDirection(KnightMovement.RIGHT);
                player.moveRight();
            }

            if (player.getDirections().get(KnightMovement.LEFT)) {
                player.setLastFacingDirection(KnightMovement.LEFT);
                player.moveLeft();
            }

            // Este if é 'repetido' umas linhas a baixo, mas este tem uma condição "else" anterior necessária
            if (player.isGrounded())
                player.setMovement((player.getDirections().get(KnightMovement.RIGHT) || player.getDirections().get(KnightMovement.LEFT)) ? State.RUNNING : State.IDLE);
        }

        if (player.isGrounded()) {
            if (player.getDirections().get(KnightMovement.UP)) player.jump();
        } else player.setMovement(player.getVelocity().y < 0 ? State.FALLING : State.JUMPING);


        // Reinicia a animação
        if (player.getMovement() != player.getLastMovement()) {
            player.resetAnimation();
            player.setLastMovement(player.getMovement());
        }

        if (player.getDirections().get(KnightMovement.UPRELEASED) && player.getDirections().get(KnightMovement.UP)) {
            player.setMovingDirection(KnightMovement.UPRELEASED, false);
            player.setMovingDirection(KnightMovement.UP, false);
            if (player.getVelocity().y > 0) player.setVelocity(new PVector(player.getVelocity().x, 0));
        }

    }

    /**
     * Gere a lógica de combate do jogador.
     * <p>
     * Atualiza a posição da área de ataque (hitbox), verifica interseções com inimigos,
     * aplica dano e remove inimigos derrotados.
     */
    private void handleKnightAttack() {
        if (player.getAttack() != null) {
            player.getAttack().setPosition(player.getPosition());

            if (map.getEnemies() != null) for (int i = map.getEnemies().size() - 1; i >= 0; i--) {
                Enemy enemy = map.getEnemies().get(i);
                if (player.getAttack().intersected(enemy.getHitbox())) {
                    enemy.damage(player.getPosition());

                    // Pequeno salto ao bater para baixo no ar
                    if (!player.isGrounded() && player.getFacingDirection() == KnightMovement.DOWN)
                        player.setVelocity(new PVector(player.getVelocity().x, 400f));
                }
            }

            player.getAttack().display(this, painter, plt);
            if (now - player.getAttackTime() > player.ATTACK_DURATION) player.setAttack(null);
        }
    }

    /**
     * Atualiza o comportamento e ataques dos inimigos.
     * <p>
     * Executa a inteligência artificial (Behaviour) de cada inimigo e verifica
     * colisão física entre o inimigo e o jogador para aplicar dano ao jogador.
     */
    private void handleMonstersAttacks() {
        if (map.getEnemies() != null) for (Enemy enemy : map.getEnemies())
            if (enemy.getHitbox().intersected(player.getHitbox()) && !enemy.isDying())
                player.damage(enemy.getPosition());
    }

    private void handleNaturalMovements(float dt) {
        for (Entity entity : map.getEntities()) {
            entity.updateTime(dt, now);

            if (entity instanceof Squit) {
                if (entity.isDying())
                    entity.applyForce(new PVector(0, -450 * entity.getMass())); // Queda na morte do Squit
            } else if (entity instanceof FalseKnight)
                entity.applyForce(new PVector(0, -450 * entity.getMass())); // Gravidade apenas para o boss
            else entity.applyForce(gravity(entity));
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
        for (int i = map.getEntities().size() - 1; i >= 0; i--) {
            Entity entity = map.getEntities().get(i);
            if (entity instanceof Squit) ((Squit) entity).setColliding(false);
            for (int j = map.getTerrains().size() - 1; j >= 0; j--) {
                Terrain terrain = map.getTerrains().get(j);
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