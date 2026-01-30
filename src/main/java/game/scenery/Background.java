package game.scenery;

import game.core.Complex;
import game.core.SubPlot;
import game.scenery.components.flock.Flock;
import game.scenery.components.flock.FlockEye;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Background {
    private static Background background = null;

    private final ArrayList<Flock> flock = new ArrayList<>();
    private final PApplet p;
    private final SubPlot plt;

    /**
     * Gere o fundo dinâmico do jogo.
     *
     * @param p o contexto gráfico do Processing
     */
    private Background(PApplet p) {
        this.p = p;
        float[] VIEWPORT = {0f, 0f, 1f, 1f};
        double[] WINDOW = {-3.55, 3.55, -2.0, 2.0};
        this.plt = new SubPlot(WINDOW, VIEWPORT, p.width, p.height);

        for (int i = 0; i < 30; i++) flock.add(new Flock(randomPVector(), plt, p));
        
        for (Flock f : flock) f.setEye(new FlockEye(f, flock));
        
    }

    public static Background getInstance() {
        if (background == null)
            System.out.println("É necessário inicializar o mapa primeiro");

        return background;
    }

    public synchronized static Background init(PApplet p) {
        if (background != null)
            System.err.println("Mapa já foi inicializado");

        background = new Background(p);
        return background;
    }

    /**
     * Devolve a lista do bando.
     *
     * @return a lista de entidades do bando
     */
    public ArrayList<Flock> getFlock() {
        return flock;
    }

    /**
     * Exibe o fundo baseado na posição.
     *
     * @param position a posição para calcular o fractal
     */
    public void display(PVector position) {
        drawMandelbrotAndJulia(position);
        drawFlock();
    }

    /**
     * Desenha todas as entidades do bando.
     */
    private void drawFlock() {
        for (Flock f : flock) {
            f.display(p, plt);
        }
    }

    /**
     * Desenha o fractal de Mandelbrot/Julia baseado na posição.
     *
     * @param position a posição para calcular o parâmetro C
     */
    private void drawMandelbrotAndJulia(PVector position) {
        p.loadPixels();

        double cRe = PApplet.map(position.x / 5, 0, p.width, -1.0f, 1.0f);
        double cIm = PApplet.map(position.y / 5, 0, p.height, -1.0f, 1.0f);
        Complex C = new Complex(cRe, cIm);

        for (int x = 0; x < p.width; x++)
            for (int y = 0; y < p.height; y++) {

                double[] zrzi = plt.getWorldCoord(x, y);
                Complex Z = new Complex(zrzi[0], zrzi[1]);

                int iter = 0;
                double ESCAPE_RADIUS = 2.0;
                int MAX_ITER = 20;
                while (iter < MAX_ITER && Z.norm() <= ESCAPE_RADIUS) {
                    Complex Z_copy = new Complex(Z.getA(), Z.getB());
                    Z.mult(Z_copy);
                    Z.add(C);
                    iter++;
                }

                int col;
                if (iter == MAX_ITER) col = p.color(0); // Interior preto
                else {
                    int val = (iter % 16) * 16;
                    col = p.color(val, 0, 0); // Tons de vermelho sobre preto
                }


                p.pixels[x + y * p.width] = col;
            }
        
        p.updatePixels();
    }

    /**
     * Gera um vetor aleatório dentro da janela.
     *
     * @return um PVector aleatório
     */
    private PVector randomPVector() {
        double[] window = plt.getWindow();

        float randomX = p.random((float) window[0], (float) window[1]);
        float randomY = p.random((float) window[2], (float) window[3]);

        return new PVector(randomX, randomY);
    }
}