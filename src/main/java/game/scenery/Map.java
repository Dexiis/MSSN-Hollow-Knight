package game.scenery;

import game.characters.types.TheKnight;
import game.core.SubPlot;
import game.hitbox.Hitbox;
import game.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Map {
    private Hitbox ground;
    private TheKnight player;

    public Map() {
        // Criação do chão (Ground)
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(1280, 0));
        points.add(new Point(1280, 50));
        points.add(new Point(0, 50));

        this.ground = new Hitbox(points);
        // Define a posição do chão no ecrã (Y = 600)
        this.ground.setPosition(new Point(0, 600));

        // Inicializa o jogador
        this.player = new TheKnight(new PVector(100, 100));
    }

    /**
     * Método atualizado: Agora aceita SubPlot para passar ao jogador
     */
    public void display(PApplet p, SubPlot plt) {
        // O chão (Hitbox) usa apenas o PApplet por enquanto (método legacy)
        ground.display(p, plt);

        // O jogador precisa do SubPlot para calcular a posição relativa à câmara
        player.display(p, plt);
    }

    public Hitbox getGround() {
        return ground;
    }

    public TheKnight getPlayer() {
        return player;
    }
}