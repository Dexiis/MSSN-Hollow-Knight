package game;

import game.core.SubPlot;
import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.MovementState;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.Aspids;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe principal do jogo que estende a {@link PApplet}.
 * <p>
 * Esta classe atua como o controlador central (Game Loop), gerindo a inicialização,
 * atualização da física, processamento de inputs, renderização gráfica e lógica global
 * do jogo (como a câmara e colisões).
 */
public class Game extends PApplet {
    private final float[] viewport = {0f, 0f, 1f, 1f};
    private final double[] window = {-800, 800, -450, 450};

    private Map map;
    private TheKnight player;
    private SubPlot plt;
    private float lastUpdateTime;
    private int now = millis();

    LinePainter painter = new LinePainter() {
        /**
         * Desenha uma linha no ecrã convertendo coordenadas do mundo para pixels.
         *
         * @param x1  Coordenada X inicial no mundo.
         * @param y1  Coordenada Y inicial no mundo.
         * @param x2  Coordenada X final no mundo.
         * @param y2  Coordenada Y final no mundo.
         * @param plt O objeto SubPlot para conversão de coordenadas.
         */
        @Override
        public void paintLine(float x1, float y1, float x2, float y2, SubPlot plt) {
            float[] p1 = plt.getPixelCoord(x1, y1);
            float[] p2 = plt.getPixelCoord(x2, y2);
            line(p1[0], p1[1], p2[0], p2[1]);
        }
    };

    /**
     * Define as configurações iniciais da janela da aplicação.
     * Especifica a resolução do ecrã.
     */
    @Override
    public void settings() {
        size(1600, 900);
    }

    /**
     * Executa a configuração inicial do jogo.
     * <p>
     * Inicializa o sistema de coordenadas (SubPlot), carrega o mapa e obtém a referência
     * para o jogador. É chamado uma única vez no início da execução.
     */
    @Override
    public void setup() {
        lastUpdateTime = millis();
        plt = new SubPlot(window, viewport, width, height);

        map = new Map(this, painter);
        player = map.getPlayer();
    }

    /**
     * Captura o evento de tecla pressionada.
     * <p>
     * Atualiza o mapa de direções do jogador ou inicia um ataque se a tecla correspondente for premida.
     */
    @Override
    public void keyPressed() { // ACONTEÇA O QUE ACONTECER, NÃO MEXER NESTE MÉTODO. A LÓGICA NÃO É FEITA AQUI
        if (key == 'w' || key == 'W' || key == ' ') {
            player.setMovingDirection(Direction.UPRELEASED, false);
            player.setMovingDirection(Direction.UP, true);
        }
        if (key == 's' || key == 'S') player.setMovingDirection(Direction.DOWN, true);
        if (key == 'a' || key == 'A') player.setMovingDirection(Direction.LEFT, true);
        if (key == 'd' || key == 'D') player.setMovingDirection(Direction.RIGHT, true);

        if (key == ENTER || key == RETURN) player.playerAttack(now);
    }

    /**
     * Captura o evento de tecla libertada.
     * <p>
     * Atualiza o mapa de direções do jogador, indicando que o movimento numa direção cessou.
     */
    @Override
    public void keyReleased() { // ACONTEÇA O QUE ACONTECER, NÃO MEXER NESTE MÉTODO. A LÓGICA NÃO É FEITA AQUI
        if (key == 'w' || key == 'W' || key == ' ') player.setMovingDirection(Direction.UPRELEASED, true);
        if (key == 's' || key == 'S') player.setMovingDirection(Direction.DOWN, false);
        if (key == 'a' || key == 'A') player.setMovingDirection(Direction.LEFT, false);
        if (key == 'd' || key == 'D') player.setMovingDirection(Direction.RIGHT, false);
        if (key == 'm' || key == 'M') player.setPosition(new PVector(0, 50)); //APENAS PARA DEBUG. REMOVER MAIS TARDE
    }

    /**
     * Captura eventos do rato.
     * <p>
     * Botão Direito: Teletransporta o jogador para a posição do rato (Debug).
     * Botão Esquerdo: Inicia um ataque do jogador.
     */
    @Override
    public void mousePressed() {
        if (mouseButton == RIGHT) {
            double[] w = plt.getWorldCoord(mouseX, mouseY);
            player.setPosition(new PVector((float) w[0], (float) w[1]));
            player.setVelocity(new PVector(0, 0));
        } else if (mouseButton == LEFT) player.playerAttack(now);
    }

    /**
     * O ciclo principal de execução do jogo (Game Loop).
     * <p>
     * Este método é executado continuamente frame a frame. É responsável por:
     * 1. Calcular o tempo delta (dt).
     * 2. Limpar o ecrã.
     * 3. Aplicar forças físicas (gravidade).
     * 4. Processar a lógica de jogo (movimento, ataques, IA).
     * 5. Resolver colisões.
     * 6. Atualizar a câmara.
     * 7. Desenhar o estado atual do mapa.
     */
    @Override
    public void draw() {
        now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;

        background(255);

        for (Entity entity : map.getEntities()) {
            if (!(entity instanceof TheKnight)) {
                entity.applyBehaviours(((Enemy) entity).getBehaviours(), dt);
                entity.getEye().display(this, plt); // DEBUGGING - TODO RETIRAR MAIS TARDE
            }
            if (!(entity instanceof Aspids)) entity.applyForce(gravity(entity));
        }

        handleInputMovement();
        handleKnighAttack();
        handleMonstersAttacks(dt);

        for (Entity entity : map.getEntities())
            entity.move(dt);

        checkCollisions();

        setWindow(player.getPosition());
        map.display(plt);
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
    private void handleInputMovement() {
        if ((!(player.getDirections().get(Direction.RIGHT)) && !(player.getDirections().get(Direction.LEFT))) || (player.getDirections().get(Direction.RIGHT)) && (player.getDirections().get(Direction.LEFT))) {
            player.stopMovement();
            player.setMovement(MovementState.IDLE);
        } else {
            if (player.getDirections().get(Direction.RIGHT)) {
                player.setLastFacingDirection(Direction.RIGHT);
                player.moveRight();
            }

            if (player.getDirections().get(Direction.LEFT)) {
                player.setLastFacingDirection(Direction.LEFT);
                player.moveLeft();
            }

            // Este if é 'repetido' umas linhas a baixo, mas este tem uma condição "else" anterior necessária
            if (player.getIsGrounded())
                player.setMovement((player.getDirections().get(Direction.RIGHT) || player.getDirections().get(Direction.LEFT)) ? MovementState.RUNNING : MovementState.IDLE);
        }

        if (player.getIsGrounded()) {
            if (player.getDirections().get(Direction.UP)) player.jump();
        } else player.setMovement(player.getVelocity().y < 0 ? MovementState.FALLING : MovementState.JUMPING);


        // Reinicia a animação
        if (player.getMovement() != player.getLastMovement()) {
            player.resetAnimation(now);
            player.setLastMovement(player.getMovement());
        }

        if (player.getDirections().get(Direction.UPRELEASED) && player.getDirections().get(Direction.UP)) {
            player.setMovingDirection(Direction.UPRELEASED, false);
            player.setMovingDirection(Direction.UP, false);
            if (player.getVelocity().y > 0) player.setVelocity(new PVector(player.getVelocity().x, 0));
        }

    }

    /**
     * Gere a lógica de combate do jogador.
     * <p>
     * Atualiza a posição da área de ataque (hitbox), verifica interseções com inimigos,
     * aplica dano e remove inimigos derrotados.
     */
    private void handleKnighAttack() {
        if (player.getAttack() != null) {
            player.getAttack().setPosition(player.getPosition());

            if (map.getEnemies() != null) for (int i = map.getEnemies().size() - 1; i >= 0; i--) {
                Enemy enemy = map.getEnemies().get(i);
                if (player.getAttack().intersected(enemy.getHitbox())) {
                    enemy.damage(this);
                    if (enemy.isDead()) map.removeEnemy(enemy);
                }
            }

            player.getAttack().draw(painter, plt);
            if (now - player.getAttackTime() > TheKnight.ATTACK_DURATION) player.setAttack(null);
        }
    }

    /**
     * Atualiza o comportamento e ataques dos inimigos.
     * <p>
     * Executa a inteligência artificial (Behaviour) de cada inimigo e verifica
     * colisão física entre o inimigo e o jogador para aplicar dano ao jogador.
     *
     * @param dt O tempo delta decorrido desde o último frame.
     */
    private void handleMonstersAttacks(float dt) {
        //TODO OUTROS CONTRA MIM
        if (map.getEnemies() != null) for (Enemy enemy : map.getEnemies())
            if (enemy.getHitbox().intersected(player.getHitbox())) player.damage(this);
    }

    /**
     * Verifica e resolve colisões entre entidades e o terreno.
     * <p>
     * Itera sobre todas as entidades e todos os elementos de terreno para impedir
     * que as entidades atravessem paredes ou chão.
     */
    private void checkCollisions() {
        player.setIsGrounded(false);
        for (Entity entity : map.getEntities())
            for (Terrain terrain : map.getTerrains()) terrain.elaborateIntersects(entity);
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