package game;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.hitbox.Point;
import game.scenery.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class Game extends PApplet {

    private Map map;
    private TheKnight player;

    private final float[] viewport = {0f, 0f, 1f, 1f};
    private double[] window = {-800, 800, 450, -450};
    private SubPlot plt;
    private float lastUpdateTime;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean isGrounded = false; // Sabemos se ele está no chão?

    @Override
    public void settings() {
        size(1600, 900);
    }

    @Override
    public void setup() {
        lastUpdateTime = millis();
        plt = new SubPlot(window, viewport, width, height);

        map = new Map();
        player = map.getPlayer();
    }

    @Override
    public void draw() {
        int now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;

        background(255);

        PVector gravity = new PVector(0, 980 * player.getMass());

        player.applyForce(gravity);
        handleInputMovement();
        player.move(dt);
        isGrounded = checkCollisions(dt);

        map.display(this, plt);
        setWindow(player.getPosition());
    }

    /**
     * Gere a velocidade horizontal baseada nas teclas pressionadas.
     */
    private void handleInputMovement() {
        float speed = 200f; // Velocidade de movimento lateral
        float currentVy = player.getVelocity().y; // Mantemos a velocidade vertical atual

        if (moveLeft) {
            player.setVelocity(new PVector(-speed, currentVy));
        } else if (moveRight) {
            player.setVelocity(new PVector(speed, currentVy));
        } else {
            // Se não carregar em nada, para (Fricção instantânea)
            player.setVelocity(new PVector(0, currentVy));
        }
    }

    /**
     * Verifica se o jogador tocou no chão.
     *
     * @return true se estiver no chão.
     */
    private boolean checkCollisions(float dt) {
        if (player.getHitbox().intersects(map.getGround())) {
            // Pára a queda (Vy = 0)
            // Mantemos a velocidade X (player.getVelocity().x)
            player.setVelocity(new PVector(player.getVelocity().x, 0));

            Point groundPos = map.getGround().getRoughHitbox().getPosition();
            float groundY = groundPos.pos.y;

            // Assumindo que o player tem 50px de altura, subtraímos 49
            player.setPosition(new PVector(player.getPosition().x, groundY - 49));
            player.getHitbox().setPosition(new Point(player.getPosition().x, player.getPosition().y));
            System.out.println("TO NO CHAO");
            return true; // Está no chão
        }
        return false;
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
        if (key == 'a' || key == 'A') {
            moveLeft = true;
        }
        if (key == 'd' || key == 'D') {
            moveRight = true;
        }
        if (key == 'w' || key == 'W') {
            // Só salta se estiver no chão!
            if (isGrounded) {
                float jumpStrength = -600f;
                player.setPosition(player.getPosition().add(new PVector(0, 0)));
                player.setVelocity(new PVector(player.getVelocity().x, jumpStrength));
                isGrounded = false; // Deixa de estar no chão assim que salta
            }
        }
    }

    @Override
    public void keyReleased() {
        if (key == 'a' || key == 'A') {
            moveLeft = false;
        }
        if (key == 'd' || key == 'D') {
            moveRight = false;
        }
    }
}