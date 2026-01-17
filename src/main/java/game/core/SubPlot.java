package game.core;

/**
 * Classe responsável pela gestão de sistemas de coordenadas e transformações gráficas.
 * <p>
 * O SubPlot define uma "janela" (Window) no mundo virtual e mapeia-a para uma área específica
 * do ecrã (Viewport). Realiza a conversão matemática entre coordenadas do mundo (World Coordinates)
 * e coordenadas de pixel (Screen Coordinates).
 */
public class SubPlot {

    private double[] window;
    private float[] viewport;
    private double mx;
    private double bx;
    private double my;
    private double by;
    private double fullHeight, fullWidth;

    /**
     * Construtor do SubPlot.
     * Calcula os coeficientes de transformação linear (declive e deslocamento) para os eixos X e Y
     * necessários para converter coordenadas entre o mundo e o ecrã.
     *
     * @param window     Array de 4 doubles definindo os limites do mundo virtual: [minX, maxX, minY, maxY].
     * @param viewport   Array de 4 floats definindo a área do ecrã (em percentagem 0.0-1.0): [x, y, largura, altura].
     * @param fullWidth  A largura total da janela da aplicação em pixels.
     * @param fullHeight A altura total da janela da aplicação em pixels.
     */
    public SubPlot(double[] window, float[] viewport, float fullWidth, float fullHeight) {
        this.window = window;
        this.viewport = viewport;
        this.fullWidth = fullWidth;
        this.fullHeight = fullHeight;
        mx = viewport[2] * fullWidth / (window[1] - window[0]);
        bx = viewport[0] * fullWidth;
        my = -viewport[3] * fullHeight / (window[3] - window[2]);
        by = (1 - viewport[1]) * fullHeight;
    }

    /**
     * Converte coordenadas do mundo virtual para coordenadas reais de pixel no ecrã.
     *
     * @param x Coordenada X no mundo virtual.
     * @param y Coordenada Y no mundo virtual.
     * @return Array de 2 floats contendo [xPixel, yPixel].
     */
    public float[] getPixelCoord(double x, double y) {
        float[] coord = new float[2];
        coord[0] = (float) (bx + mx * (x - window[0]));
        coord[1] = (float) (by + my * (y - window[2]));
        return coord;
    }

    /**
     * Versão sobrecarregada de {@link #getPixelCoord(double, double)} que aceita um array.
     *
     * @param xy Array contendo [xMundo, yMundo].
     * @return Array de 2 floats contendo [xPixel, yPixel].
     */
    public float[] getPixelCoord(double[] xy) {
        return getPixelCoord(xy[0], xy[1]);
    }

    /**
     * Converte dimensões (largura e altura) do mundo para pixels, sem aplicar translação.
     * Útil para calcular o tamanho de objetos sem se preocupar com a sua posição.
     *
     * @param dimx Largura no mundo virtual.
     * @param dimy Altura no mundo virtual.
     * @return Array de 2 floats contendo [larguraPixel, alturaPixel].
     */
    public float[] getDimInPixel(double dimx, double dimy) {
        float[] d = new float[2];
        d[0] = (float) (dimx * mx);
        d[1] = (float) (-dimy * my);

        return d;
    }

    /**
     * Converte coordenadas de pixel (ex: posição do rato) para coordenadas do mundo virtual.
     * Realiza a operação inversa de {@link #getPixelCoord(double, double)}.
     *
     * @param xx Coordenada X em pixels.
     * @param yy Coordenada Y em pixels.
     * @return Array de 2 doubles contendo [xMundo, yMundo].
     */
    public double[] getWorldCoord(double xx, double yy) {
        double[] coord = new double[2];
        coord[0] = window[0] + (xx - bx) / mx;
        coord[1] = window[2] + (yy - by) / my;
        return coord;
    }

    /**
     * Versão sobrecarregada que aceita um array de floats (coordenadas de pixel).
     *
     * @param xy Array contendo [xPixel, yPixel].
     * @return Array de floats convertido.
     */
    public float[] getWorldCoord(float[] xy) {
        return getPixelCoord(xy[0], xy[1]);
    }

    /**
     * Verifica se uma coordenada de pixel está dentro da área definida por este SubPlot.
     * Útil para detetar cliques do rato dentro de uma vista específica.
     *
     * @param xx Coordenada X em pixels.
     * @param yy Coordenada Y em pixels.
     * @return {@code true} se o ponto estiver dentro do viewport, {@code false} caso contrário.
     */
    public boolean isInside(float xx, float yy) {
        double[] c = getWorldCoord(xx, yy);
        return (c[0] >= window[0] && c[0] <= window[1] && c[1] >= window[2] && c[1] <= window[3]);
    }

    /**
     * Versão sobrecarregada de {@link #isInside(float, float)} que aceita um array.
     *
     * @param xy Array contendo [xPixel, yPixel].
     * @return {@code true} se estiver dentro.
     */
    public boolean isInside(float[] xy) {
        return isInside(xy[0], xy[1]);
    }

    /**
     * Obtém a caixa delimitadora (bounding box) de todo o viewport em pixels.
     *
     * @return Array de 4 floats: [xPixel, yPixel, larguraPixel, alturaPixel].
     */
    public float[] getBoundingBox() {
        float[] c1 = getPixelCoord(window[0], window[2]);
        float[] c2 = getPixelCoord(window[1], window[3]);
        return new float[]{c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};
    }

    /**
     * Converte uma caixa definida no mundo virtual para coordenadas de pixel.
     *
     * @param cx   Centro ou canto X no mundo (depende da lógica do jogo).
     * @param cy   Centro ou canto Y no mundo.
     * @param dimx Largura no mundo.
     * @param dimy Altura no mundo.
     * @return Array de 4 floats: [xPixel, yPixel, larguraPixel, alturaPixel].
     */
    public float[] getBox(double cx, double cy, double dimx, double dimy) {
        float[] c1 = getPixelCoord(cx, cy);
        float[] c2 = getPixelCoord(cx + dimx, cy + dimy);
        return new float[]{c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};
    }

    /**
     * Versão sobrecarregada de {@link #getBox(double, double, double, double)} que aceita um array.
     *
     * @param b Array contendo [x, y, largura, altura] no mundo.
     * @return Array de 4 floats em pixels.
     */
    public float[] getBox(double[] b) {
        return getBox(b[0], b[1], b[2], b[3]);
    }

    /**
     * Converte um vetor (direção e magnitude) do mundo para pixels.
     * Semelhante a converter dimensões, ignora a posição de origem.
     *
     * @param dx Componente X do vetor.
     * @param dy Componente Y do vetor.
     * @return Array de 2 floats representando o vetor em pixels.
     */
    public float[] getVectorCoord(double dx, double dy) {
        float[] v = new float[2];
        v[0] = (float) (dx * mx);
        v[1] = (float) (-dy * my);
        return v;
    }

    /**
     * Versão sobrecarregada de {@link #getVectorCoord(double, double)}.
     *
     * @param dxdy Array contendo [dx, dy].
     * @return Array de 2 floats.
     */
    public float[] getVectorCoord(double[] dxdy) {
        return getVectorCoord(dxdy[0], dxdy[1]);
    }

    /**
     * Obtém as definições atuais da janela do mundo (Window).
     *
     * @return Array [minX, maxX, minY, maxY].
     */
    public double[] getWindow() {
        return window;
    }

    /**
     * Obtém as definições atuais do viewport (Ecrã).
     *
     * @return Array [x%, y%, w%, h%].
     */
    public float[] getViewport() {
        return viewport;
    }

    /**
     * Redefine o viewport e recalcula todos os coeficientes de transformação.
     *
     * @param viewport Novo array de definições de viewport.
     */
    public void setViewport(float[] viewport) {
        this.viewport = viewport;
        mx = viewport[2] * fullWidth / (window[1] - window[0]);
        bx = viewport[0] * fullWidth;
        my = -viewport[3] * fullHeight / (window[3] - window[2]);
        by = (1 - viewport[1]) * fullHeight;
    }

    /**
     * Redefine a janela do mundo (zoom ou pan) e recalcula os coeficientes de transformação.
     *
     * @param window Novo array de definições de window.
     */
    public void setWindow(double[] window) {
        this.window = window;
        mx = viewport[2] * fullWidth / (window[1] - window[0]);
        bx = viewport[0] * fullWidth;
        my = -viewport[3] * fullHeight / (window[3] - window[2]);
        by = (1 - viewport[1]) * fullHeight;
    }
}