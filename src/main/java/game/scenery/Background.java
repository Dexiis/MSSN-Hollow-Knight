package game.scenery;

import game.core.Complex;
import game.core.SubPlot;
import game.scenery.components.flock.Flock;
import game.scenery.components.flock.FlockEye;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Gere o fundo visual dinâmico do jogo.
 * <p>
 * Esta classe é responsável pela criação e atualização do fundo do jogo,
 * combinando a renderização de fractais com um conjunto de entidades em
 * movimento do tipo bando, seguindo o padrão singleton para garantir
 * uma única instância ativa.
 * </p>
 */
public class Background {
    private static Background background = null;

    private final ArrayList<Flock> flock = new ArrayList<>();
    private final PApplet p;
    private final SubPlot plt;

    /**
     * Cria e inicializa o fundo dinâmico do jogo.
     * <p>
     * Configura o sistema de coordenadas, inicializa o conjunto de entidades
     * do bando em posições aleatórias e associa a cada uma um observador
     * responsável pelo seu comportamento coletivo.
     * </p>
     *
     * @param p o contexto gráfico do Processing utilizado para renderização
     */
    private Background(PApplet p) {
        this.p = p;
        float[] VIEWPORT = {0f, 0f, 1f, 1f};
        double[] WINDOW = {-3.55, 3.55, -2.0, 2.0};
        this.plt = new SubPlot(WINDOW, VIEWPORT, p.width, p.height);

        for (int i = 0; i < 20; i++) flock.add(new Flock(randomPVector(), plt, p));

        for (Flock f : flock) f.setEye(new FlockEye(f, flock));

    }

    /**
     * Devolve a instância atual do fundo.
     * <p>
     * Permite aceder à instância única previamente inicializada, avisando
     * caso esta ainda não tenha sido criada.
     * </p>
     *
     * @return a instância única de Background
     */
    public static Background getInstance() {
        if (background == null) System.out.println("É necessário inicializar o mapa primeiro");

        return background;
    }

    /**
     * Inicializa a instância única do fundo.
     * <p>
     * Cria a instância de Background caso ainda não exista, garantindo
     * a sincronização para evitar múltiplas inicializações.
     * </p>
     *
     * @param p o contexto gráfico do Processing
     * @return a instância criada de Background
     */
    public synchronized static Background init(PApplet p) {
        if (background != null) System.err.println("Mapa já foi inicializado");

        background = new Background(p);
        return background;
    }

    /**
     * Devolve a lista de entidades do bando.
     * <p>
     * Permite o acesso ao conjunto de objetos Flock que compõem
     * o fundo animado do jogo.
     * </p>
     *
     * @return a lista de entidades do bando
     */
    public ArrayList<Flock> getFlock() {
        return flock;
    }

    /**
     * Exibe o fundo com base numa posição fornecida.
     * <p>
     * Atualiza a visualização do fractal e desenha todas as entidades
     * do bando, utilizando a posição para influenciar o cálculo
     * do fundo dinâmico.
     * </p>
     *
     * @param position a posição utilizada para calcular o fractal
     */
    public void display(PVector position) {
        drawMandelbrotAndJulia(position);
        drawFlock();
    }

    /**
     * Desenha todas as entidades do bando no ecrã.
     * <p>
     * Percorre a lista de objetos Flock e delega a cada um a sua
     * própria representação gráfica.
     * </p>
     */
    private void drawFlock() {
        for (Flock f : flock)
            f.display(p, plt);
    }

    /**
     * Desenha o fractal de Mandelbrot e Julia com base numa posição.
     * <p>
     * Calcula o parâmetro complexo a partir da posição fornecida e
     * renderiza o fractal píxel a píxel, criando um fundo dinâmico
     * com variações cromáticas em tons de vermelho.
     * </p>
     *
     * @param position a posição utilizada para calcular o parâmetro complexo C
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
     * Gera um vetor aleatório dentro da área visível.
     * <p>
     * Utiliza os limites da janela definida no SubPlot para criar
     * uma posição aleatória válida no espaço do fundo.
     * </p>
     *
     * @return um vetor com coordenadas aleatórias
     */
    private PVector randomPVector() {
        double[] window = plt.getWindow();

        float randomX = p.random((float) window[0], (float) window[1]);
        float randomY = p.random((float) window[2], (float) window[3]);

        return new PVector(randomX, randomY);
    }
}