package game;

import game.core.SubPlot;
import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.attributes.Eye;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.MovementState;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.Aspids;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

public class Game extends PApplet {

    private Map map;
    private TheKnight player;

    private final float[] viewport = {0f, 0f, 1f, 1f};
    private final double[] window = {-800, 800, -450, 450};
    private SubPlot plt;
    private float lastUpdateTime;

    private int now = millis();

    @Override
    public void settings() {
        size(1600, 900);
    }

    @Override
    public void setup() {
        lastUpdateTime = millis();
        plt = new SubPlot(window, viewport, width, height);

        map = new Map(this, painter);
        player = map.getPlayer();

        for (Enemy enemy : map.getEnemies())
            enemy.setEye(new Eye(enemy, player));
    }

    @Override
    public void draw() {
        now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;

        background(255);

        for (Entity entity : map.getEntities())
            if (!(entity instanceof Aspids)) entity.applyForce(gravity(entity.getMass()));

        handleInputMovement();
        handleKnighAttack();
        handleMonstersAttacks(dt);

        for (Entity entity : map.getEntities())
            entity.move(dt);

        checkCollisions();

        setWindow(player.getPosition());
        map.display(plt);
    }

    private PVector gravity(float mass) {
        if (player.getAcceleration().mag() < 12.8f) return new PVector(0, -1280 * mass);
        else return new PVector(0, 0);
    }

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

    private void handleMonstersAttacks(float dt) {
        //TODO OUTROS CONTRA MIM
        if (map.getEnemies() != null) for (Enemy enemy : map.getEnemies()) {
            enemy.applyBehaviour(enemy.getBehaviour(), dt);

            if (enemy.getHitbox().intersected(player.getHitbox())) player.damage(this);
        }
    }

    private void checkCollisions() {
        player.setIsGrounded(false);
        for (Entity entity : map.getEntities())
            for (Terrain terrain : map.getTerrains()) terrain.elaborateIntersects(entity);
    }

    private void setWindow(PVector playerPosition) {
        window[0] = playerPosition.x - 800; // Esquerda
        window[1] = playerPosition.x + 800; // Direita
        window[2] = playerPosition.y - 450; // Topo (Menor valor)
        window[3] = playerPosition.y + 450; // Fundo (Maior valor)

        plt.setWindow(window);
    }

    LinePainter painter = new LinePainter() {
        @Override
        public void paintLine(float x1, float y1, float x2, float y2, SubPlot plt) {
            float[] p1 = plt.getPixelCoord(x1, y1);
            float[] p2 = plt.getPixelCoord(x2, y2);
            line(p1[0], p1[1], p2[0], p2[1]);
        }
    };

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

    @Override
    public void keyReleased() { // ACONTEÇA O QUE ACONTECER, NÃO MEXER NESTE MÉTODO. A LÓGICA NÃO É FEITA AQUI
        if (key == 'w' || key == 'W' || key == ' ') {
            player.setMovingDirection(Direction.UP, false);
            player.setMovingDirection(Direction.UPRELEASED, true);
        }
        if (key == 's' || key == 'S') player.setMovingDirection(Direction.DOWN, false);
        if (key == 'a' || key == 'A') player.setMovingDirection(Direction.LEFT, false);
        if (key == 'd' || key == 'D') player.setMovingDirection(Direction.RIGHT, false);
        if (key == 'm' || key == 'M') player.setPosition(new PVector(0, 50)); //APENAS PARA DEBUG. REMOVER MAIS TARDE
    }

    @Override
    public void mousePressed() {
        if (mouseButton == RIGHT) {
            double[] w = plt.getWorldCoord(mouseX, mouseY);
            player.setPosition(new PVector((float) w[0], (float) w[1]));
            player.setVelocity(new PVector(0, 0));
        } else if (mouseButton == LEFT) player.playerAttack(now);
    }
}