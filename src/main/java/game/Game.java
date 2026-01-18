package game;

import game.core.SubPlot;
import game.scenery.Map;
import game.scenery.characters.Entity;
import game.scenery.characters.types.Direction;
import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.HurtBox;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

public class Game extends PApplet {

    private Map map;
    private TheKnight player;

    private final float[] viewport = {0f, 0f, 1f, 1f};
    private double[] window = {-800, 800, -450, 450};
    private SubPlot plt;
    private float lastUpdateTime;
    private float attackTime;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    //private boolean idle = false;
    private boolean jump = false;
    private boolean lookingDown = false;
    private HurtBox attack = null;

    private static final int ATTACK_COOLDOWN = 700;

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
    }

    @Override
    public void draw() {
        int now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;

        background(255);

        //TODO OUTROS CONTRA MIM
        if (map.getEnemies() != null) {

            for (Enemy enemy : map.getEnemies()) {
                if (enemy.getHitbox().intersected(player.getHitbox())) {
                    player.damage(this);
                    //PVector direction = PVector.sub(player.getPosition(), enemy.getPosition()).normalize();
                    //direction = direction.mult(100);
                    //player.setVelocity(new PVector(0, 0));
                    //player.applyForce(direction);
                }
            }
        }

        if (attack != null) {
            attack.setPosition(player.getPosition());

            if (map.getEnemies() != null) {
                for (int i = map.getEnemies().size() - 1; i >= 0; i--) {
                    Enemy enemy = map.getEnemies().get(i);
                    if (attack.intersected(enemy.getHitbox())) {
                        enemy.damage(this);
                        System.out.println(enemy.getHealth());
                        if (enemy.isDead()) map.removeEnemy(enemy);
                    }
                }
            }

            attack.draw(painter, plt);
            if (now - attackTime > TheKnight.ATTACK_DURANTION) attack = null;
        }

        player.applyForce(gravity(player.getMass()));
        handleInputMovement();
        player.move(dt);

        checkCollisions();

        setWindow(player.getPosition());
        map.display(plt);
    }

    private PVector gravity(float mass) {
        if (player.getAcceleration().mag() < 9.8f) return new PVector(0, -980 * mass);
        else return new PVector(0, 0 * mass);
    }

    /**
     * Gere a velocidade horizontal baseada nas teclas pressionadas.
     */
    private void handleInputMovement() {
        if (moveRight) player.moveRight();
        if (moveLeft) player.moveLeft();
        if ((!moveRight && !moveLeft) || (moveRight && moveLeft)) player.stopMovement();
        if (player.getIsGrounded() && jump) player.jump();
    }

    /**
     * Verifica as colisões do jogador.
     */
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
    public void keyPressed() {
        if (key == 'a' || key == 'A') moveLeft = true;
        if (key == 'd' || key == 'D') moveRight = true;
        if (key == 'w' || key == 'W' || key == ' ') jump = true;
        if (key == 's' || key == 'S') lookingDown = true;
        //if (!moveLeft && !moveRight && !jumping) idle = true;
        //else idle = false;
    }

    @Override
    public void keyReleased() {
        if (key == 'a' || key == 'A') moveLeft = false;
        if (key == 'd' || key == 'D') moveRight = false;
        if (key == 'w' || key == 'W' || key == ' ') jump = false;
        if (key == 's' || key == 'S') lookingDown = false;
    }

    @Override
    public void mousePressed() {
        if (mouseButton == RIGHT) {
            double[] w = plt.getWorldCoord(mouseX, mouseY);
            player.setPosition(new PVector((float) w[0], (float) w[1]));
            player.setVelocity(new PVector(0, 0));
        } else if (mouseButton == LEFT) {
            if (millis() - attackTime > ATTACK_COOLDOWN) {
                if (lookingDown) attack = player.attack(Direction.DOWN);
                else if (jump) attack = player.attack(Direction.UP);
                else {
                    PVector v = player.getVelocity();
                    Direction direction;

                    if (Math.abs(v.x) != 0) direction = (v.x > 0) ? Direction.RIGHT : Direction.LEFT;
                    else direction = Direction.UP;

                    attack = player.attack(direction);
                }
                attackTime = millis();
            }
        }
    }

}