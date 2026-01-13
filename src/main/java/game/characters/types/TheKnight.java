package game.characters.types;

import game.characters.Entity;
import game.core.SubPlot;
import game.hitbox.Hitbox;
import game.hitbox.LinePainter;
import game.hitbox.Point;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class TheKnight extends Entity {

    // Cor do personagem (Roxo)
    private int color = 0xFF800080;

    public TheKnight(PVector position) {
        super(position);

        // Define a forma física (Hitbox) como um quadrado 50x50
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(50, 0));
        points.add(new Point(50, 50));
        points.add(new Point(0, 50));

        this.hitbox = new Hitbox(points);
        this.mass = 1f;
    }

    @Override
    public void display(PApplet p, SubPlot plt) {
        p.pushStyle(); // Guarda o estilo anterior

        // 1. Converter coordenadas do Mundo (Física) para Pixels (Ecrã)
        float[] pixelCoord = plt.getPixelCoord(position.x, position.y);

        // 2. Converter dimensões do Mundo para Pixels
        // Assumindo que o cavaleiro tem 50x50 unidades de tamanho no mundo
        float[] dim = plt.getDimInPixel(50, 50);

        p.fill(this.color);
        p.noStroke();

        // Desenha o retângulo na posição correta
        // Nota: Ajusta se a hitbox for centrada ou canto superior esquerdo.
        // O código assume que 'pos' é o canto superior esquerdo (baseado nos pontos 0,0 a 50,50)
        p.rect(pixelCoord[0], pixelCoord[1], dim[0], dim[1]);

        p.popStyle(); // Restaura o estilo
    }

    /**
     * Desenha as linhas de debug da hitbox
     */
    public void draw(LinePainter painter) {
        painter.setStroke(0xFF00FF00); // Verde brilhante para a Hitbox
        painter.setStrokeWeight(2);    // Linha um pouco mais grossa
        hitbox.draw(painter);
    }
}