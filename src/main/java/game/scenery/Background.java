package game.scenery;

import processing.core.PApplet;
import processing.core.PVector;
import game.core.SubPlot;
import game.core.Complex;

public class Background {

    private PApplet p;
    private SubPlot plt;
    private final int MAX_ITER = 20;
    private final double ESCAPE_RADIUS = 2.0;
    private final double[] WINDOW = { -3.55, 3.55, -2.0, 2.0 };
    private final float[] VIEWPORT = { 0f, 0f, 1f, 1f };

    public Background(PApplet p) {
        this.p = p;
        this.plt = new SubPlot(WINDOW, VIEWPORT, p.width, p.height);
    }

    public void display(PVector position) {
        p.loadPixels();

        double cRe = PApplet.map(position.x / 5, 0, p.width, -1.0f, 1.0f);
        double cIm = PApplet.map(position.y / 5, 0, p.height, -1.0f, 1.0f);
        Complex C = new Complex(cRe, cIm);

        for (int x = 0; x < p.width; x++) {
            for (int y = 0; y < p.height; y++) {

                double[] zrzi = plt.getWorldCoord(x, y);
                Complex Z = new Complex(zrzi[0], zrzi[1]);

                int iter = 0;
                while (iter < MAX_ITER && Z.norm() <= ESCAPE_RADIUS) {
                    Complex Z_copy = new Complex(Z.getA(), Z.getB());
                    Z.mult(Z_copy);
                    Z.add(C);
                    iter++;
                }

                int col;
                if (iter == MAX_ITER) {
                    col = p.color(255); // Interior branco
                } else {
                    int val = 255 - (iter % 16) * 16;
                    col = p.color(255, val, val);
                }

                p.pixels[x + y * p.width] = col;
            }
        }
        p.updatePixels();
    }
}