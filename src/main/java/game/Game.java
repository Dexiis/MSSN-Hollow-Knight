package game;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.core.Terrain;
import game.scenery.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class Game extends PApplet {

    private Map map;
    private TheKnight player;

    private final float[] viewport = {0f, 0f, 1f, 1f};
    private double[] window = {-800, 800, -450, 450};
    private SubPlot plt;
    private float lastUpdateTime;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean jumping = false;

    @Override
    public void settings() {
        size(1600, 900);
    }

    @Override
    public void setup() {
        lastUpdateTime = millis();
        plt = new SubPlot(window, viewport, width, height);

        map = new Map(this);
        player = map.getPlayer();
    }

    @Override
    public void draw() {
        int now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;

        background(255);

        player.applyForce(gravity(player.getMass()));
        handleInputMovement();
        player.move(dt);
        checkCollisions(dt);

        //setWindow(player.getPosition());
        map.display(plt);

        float[] pStart = plt.getPixelCoord(0, 0);
        float[] pEnd = plt.getPixelCoord(200, 200);

        line(pStart[0], pStart[1], pEnd[0], pEnd[1]);

        pushMatrix();
        translate(pEnd[0], pEnd[1]);
        rotate(atan2(pEnd[1] - pStart[1], pEnd[0] - pStart[0]));
        line(0, 0, -10, -5);
        line(0, 0, -10, 5);
        popMatrix();
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
        if (player.getIsGrounded() && jumping) player.jump();
    }

    /**
     * Verifica as colisões do jogador.
     */
    private void checkCollisions(float dt) {
        player.setIsGrounded(false);
        for (Terrain terrain : map.getTerrains()) terrain.elaborateIntersects(player);
    }

    private void setWindow(PVector playerPosition) {
        window[0] = playerPosition.x - 800; // Esquerda
        window[1] = playerPosition.x + 800; // Direita
        window[2] = playerPosition.y - 450; // Topo (Menor valor)
        window[3] = playerPosition.y + 450; // Fundo (Maior valor)

        plt.setWindow(window);
    }

    @Override
    public void keyPressed() {
        if (key == 'a' || key == 'A') moveLeft = true;
        if (key == 'd' || key == 'D') moveRight = true;
        if (key == 'w' || key == 'W') jumping = true;
    }

    @Override
    public void keyReleased() {
        if (key == 'a' || key == 'A') moveLeft = false;
        if (key == 'd' || key == 'D') moveRight = false;
        if (key == 'w' || key == 'W') jumping = false;
    }

    @Override
    public void mousePressed() {
        if (mouseButton == RIGHT) {
            double[] w = plt.getWorldCoord(mouseX, mouseY);
            player.setPosition(new PVector((float)w[0], (float)w[1]));
            player.setVelocity(new PVector(0, 0));
        } else if (mouseButton == LEFT) {
            player.setPosition(new PVector(0, 0));
            player.setVelocity(new PVector(0, 0));
        }
    }

}