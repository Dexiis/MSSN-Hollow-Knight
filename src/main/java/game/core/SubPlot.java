package game.core;

/**
 * Responsável pela gestão de sistemas de coordenadas e transformações gráficas.
 * <p>
 * Define uma "janela" (Window) no mundo virtual e mapeia-a para uma área específica
 * do ecrã (Viewport). Realiza a conversão matemática entre coordenadas do mundo
 * (World Coordinates) e coordenadas de pixel (Screen Coordinates).
 * </p>
 */
public class SubPlot {

    private double[] window;
    private float[] viewport;
    private final double fullHeight, fullWidth;

    private double mx;
    private double bx;
    private double my;
    private double by;

    /**
     * Constrói um novo SubPlot e calcula os coeficientes de transformação linear.
     * <p>
     * Calcula os valores de declive e deslocamento para os eixos X e Y,
     * necessários para converter coordenadas entre o mundo virtual e o ecrã.
     * </p>
     *
     * @param window     array de 4 doubles definindo os limites do mundo virtual: [minX, maxX, minY, maxY]
     * @param viewport   array de 4 floats definindo a área do ecrã (em percentagem 0.0-1.0): [x, y, largura, altura]
     * @param fullWidth  a largura total da janela da aplicação em pixels
     * @param fullHeight a altura total da janela da aplicação em pixels
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
     * Obtém as definições atuais da janela do mundo (Window).
     * <p>
     * Retorna o array com os limites do mundo virtual.
     * </p>
     *
     * @return array com os limites do mundo: [minX, maxX, minY, maxY]
     */
    public double[] getWindow() {
        return window;
    }

    /**
     * Redefine a janela do mundo e recalcula os coeficientes de transformação.
     * <p>
     * Permite operações de zoom ou deslocamento (pan) no mundo virtual.
     * </p>
     *
     * @param window novo array de definições da janela do mundo
     */
    public void setWindow(double[] window) {
        this.window = window;
        mx = viewport[2] * fullWidth / (window[1] - window[0]);
        bx = viewport[0] * fullWidth;
        my = -viewport[3] * fullHeight / (window[3] - window[2]);
        by = (1 - viewport[1]) * fullHeight;
    }

    /**
     * Obtém as definições atuais do viewport (área do ecrã).
     *
     * @return array com as definições do viewport: [x%, y%, largura%, altura%]
     */
    public float[] getViewport() {
        return viewport;
    }

    /**
     * Redefine o viewport e recalcula todos os coeficientes de transformação.
     *
     * @param viewport novo array de definições do viewport
     */
    public void setViewport(float[] viewport) {
        this.viewport = viewport;
        mx = viewport[2] * fullWidth / (window[1] - window[0]);
        bx = viewport[0] * fullWidth;
        my = -viewport[3] * fullHeight / (window[3] - window[2]);
        by = (1 - viewport[1]) * fullHeight;
    }

    /**
     * Converte coordenadas do mundo virtual para coordenadas reais de pixel no ecrã.
     *
     * @param x coordenada X no mundo virtual
     * @param y coordenada Y no mundo virtual
     * @return array de 2 floats contendo [xPixel, yPixel]
     */
    public float[] getPixelCoord(double x, double y) {
        float[] coord = new float[2];
        coord[0] = (float) (bx + mx * (x - window[0]));
        coord[1] = (float) (by + my * (y - window[2]));
        return coord;
    }

    /**
     * Converte coordenadas do mundo virtual para coordenadas de pixel.
     * <p>
     * Versão sobrecarregada que aceita um array como parâmetro.
     * </p>
     *
     * @param xy array contendo [xMundo, yMundo]
     * @return array de 2 floats contendo [xPixel, yPixel]
     */
    public float[] getPixelCoord(double[] xy) {
        return getPixelCoord(xy[0], xy[1]);
    }

    /**
     * Converte dimensões do mundo para pixels, sem aplicar translação.
     * <p>
     * Útil para calcular o tamanho de objetos sem considerar a sua posição.
     * </p>
     *
     * @param dimx largura no mundo virtual
     * @param dimy altura no mundo virtual
     * @return array de 2 floats contendo [larguraPixel, alturaPixel]
     */
    public float[] getDimInPixel(double dimx, double dimy) {
        float[] d = new float[2];
        d[0] = (float) (dimx * mx);
        d[1] = (float) (-dimy * my);

        return d;
    }

    /**
     * Converte coordenadas de pixel para coordenadas do mundo virtual.
     * <p>
     * Realiza a operação inversa da conversão de coordenadas mundo-para-pixel.
     * Útil para converter posições do rato em coordenadas do mundo.
     * </p>
     *
     * @param xx coordenada X em pixels
     * @param yy coordenada Y em pixels
     * @return array de 2 doubles contendo [xMundo, yMundo]
     */
    public double[] getWorldCoord(double xx, double yy) {
        double[] coord = new double[2];
        coord[0] = window[0] + (xx - bx) / mx;
        coord[1] = window[2] + (yy - by) / my;
        return coord;
    }

    /**
     * Converte coordenadas de pixel para coordenadas do mundo virtual.
     * <p>
     * Versão sobrecarregada que aceita um array de floats.
     * </p>
     *
     * @param xy array contendo [xPixel, yPixel]
     * @return array de floats com as coordenadas convertidas
     */
    public float[] getWorldCoord(float[] xy) {
        return getPixelCoord(xy[0], xy[1]);
    }

    /**
     * Converte um vetor do mundo para pixels.
     * <p>
     * Aplica apenas a escala (não considera translação), útil para converter
     * direções e magnitudes sem depender da posição de origem.
     * </p>
     *
     * @param dx componente X do vetor no mundo
     * @param dy componente Y do vetor no mundo
     * @return array de 2 floats representando o vetor em pixels
     */
    public float[] getVectorCoord(double dx, double dy) {
        float[] v = new float[2];
        v[0] = (float) (dx * mx);
        v[1] = (float) (-dy * my);
        return v;
    }

    /**
     * Converte um vetor do mundo para pixels.
     * <p>
     * Versão sobrecarregada que aceita um array como parâmetro.
     * </p>
     *
     * @param dxdy array contendo [dx, dy] no mundo
     * @return array de 2 floats representando o vetor em pixels
     */
    public float[] getVectorCoord(double[] dxdy) {
        return getVectorCoord(dxdy[0], dxdy[1]);
    }

    /**
     * Converte uma caixa definida no mundo virtual para coordenadas de pixel.
     *
     * @param cx   posição X no mundo (centro ou canto, dependente da implementação)
     * @param cy   posição Y no mundo (centro ou canto, dependente da implementação)
     * @param dimx largura da caixa no mundo
     * @param dimy altura da caixa no mundo
     * @return array de 4 floats: [xPixel, yPixel, larguraPixel, alturaPixel]
     */
    public float[] getBox(double cx, double cy, double dimx, double dimy) {
        float[] c1 = getPixelCoord(cx, cy);
        float[] c2 = getPixelCoord(cx + dimx, cy + dimy);
        return new float[]{c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};
    }

    /**
     * Converte uma caixa do mundo virtual para coordenadas de pixel.
     * <p>
     * Versão sobrecarregada que aceita um array como parâmetro.
     * </p>
     *
     * @param b array contendo [x, y, largura, altura] no mundo
     * @return array de 4 floats: [xPixel, yPixel, larguraPixel, alturaPixel]
     */
    public float[] getBox(double[] b) {
        return getBox(b[0], b[1], b[2], b[3]);
    }

    /**
     * Obtém a caixa delimitadora de t.odo o viewport em pixels.
     * <p>
     * Calcula as coordenadas de pixel que correspondem aos limites completos
     * da janela do mundo definida.
     * </p>
     *
     * @return array de 4 floats: [xPixel, yPixel, larguraPixel, alturaPixel]
     */
    public float[] getBoundingBox() {
        float[] c1 = getPixelCoord(window[0], window[2]);
        float[] c2 = getPixelCoord(window[1], window[3]);
        return new float[]{c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};
    }

    /**
     * Verifica se uma coordenada de pixel está dentro da área definida por este SubPlot.
     * <p>
     * Útil para detetar cliques do rato dentro de uma vista específica.
     * </p>
     *
     * @param xx coordenada X em pixels
     * @param yy coordenada Y em pixels
     * @return {@code true} se o ponto estiver dentro do viewport, {@code false} caso contrário
     */
    public boolean isInside(float xx, float yy) {
        double[] c = getWorldCoord(xx, yy);
        return (c[0] >= window[0] && c[0] <= window[1] && c[1] >= window[2] && c[1] <= window[3]);
    }

    /**
     * Verifica se uma coordenada de pixel está dentro da área definida por este SubPlot.
     * <p>
     * Versão sobrecarregada que aceita um array como parâmetro.
     * </p>
     *
     * @param xy array contendo [xPixel, yPixel]
     * @return {@code true} se o ponto estiver dentro do viewport, {@code false} caso contrário
     */
    public boolean isInside(float[] xy) {
        return isInside(xy[0], xy[1]);
    }
}