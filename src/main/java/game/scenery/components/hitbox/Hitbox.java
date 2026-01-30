package game.scenery.components.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Define uma estrutura de colisão geométrica.
 * <p>
 * Suporta formas poligonais e retangulares, com lógica de interseção em duas fases.
 * </p>
 */
public class Hitbox {

    protected final RoughHitbox roughHitbox;
    protected final List<LineSegment> lines = new ArrayList<>();

    protected Point position = new Point(0.0f, 0.0f);
    protected float width, height;

    /**
     * Instancia uma nova Hitbox baseada numa forma poligonal.
     * <p>
     * Constrói a geometria interna e a caixa delimitadora a partir de uma lista de vértices.
     * </p>
     *
     * @param points lista de pontos que definem os vértices da forma
     */
    public Hitbox(List<Point> points) {
        formLines(points);
        calculateDimensions(points);
        roughHitbox = new RoughHitbox(points, position);
    }

    /**
     * Cria uma Hitbox a partir de uma lista de vetores do Processing.
     * <p>
     * Converte os vetores para o sistema interno antes de processar.
     * </p>
     *
     * @param points lista de vetores que representam os vértices
     */
    public Hitbox(ArrayList<PVector> points) {
        List<Point> newPoints = new ArrayList<>();
        for (PVector point : points) newPoints.add(new Point(point.x, point.y));

        formLines(newPoints);
        calculateDimensions(newPoints);
        roughHitbox = new RoughHitbox(newPoints, position);
    }

    /**
     * Inicializa uma Hitbox com formato retangular.
     * <p>
     * Gera automaticamente os vértices baseando-se nas dimensões fornecidas.
     * </p>
     *
     * @param position o ponto central da área de colisão
     * @param width    a largura total da área
     * @param height   a altura total da área
     */
    public Hitbox(Point position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;

        ArrayList<Point> points = new ArrayList<>();
        float halfW = width / 2;
        float halfH = height / 2;

        points.add(new Point(-halfW, -halfH));
        points.add(new Point(halfW, -halfH));
        points.add(new Point(halfW, halfH));
        points.add(new Point(-halfW, halfH));

        formLines(points);
        this.roughHitbox = new RoughHitbox(points, this.position);
    }

    /**
     * Recupera a estrutura de colisão aproximada.
     *
     * @return o objeto RoughHitbox associado
     */
    public RoughHitbox getRoughHitbox() {
        return roughHitbox;
    }

    /**
     * Obtém a coordenada atual da Hitbox.
     *
     * @return o ponto que representa a posição central
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Devolve a largura total da área de colisão.
     *
     * @return a largura em unidades do mundo
     */
    public float getWidth() {
        return width;
    }

    /**
     * Devolve a altura total da área de colisão.
     *
     * @return a altura em unidades do mundo
     */
    public float getHeight() {
        return height;
    }

    /**
     * Disponibiliza a lista de segmentos de reta que compõem o perímetro da Hitbox.
     *
     * @return lista de objetos LineSegment
     */
    public List<LineSegment> getLines() {
        return lines;
    }

    /**
     * Atualiza a posição espacial da Hitbox.
     * <p>
     * Propaga a alteração para a caixa delimitadora e para todos os segmentos de reta.
     * </p>
     *
     * @param position o novo ponto de posição
     */
    public void setPosition(Point position) {
        this.position = position;
        this.roughHitbox.position = position;
        for (LineSegment line : lines) line.setPosition(position);
    }

    /**
     * Define a posição utilizando um vetor do Processing.
     *
     * @param position o novo vetor de posição
     */
    public void setPosition(PVector position) {
        setPosition(new Point(position.x, position.y));
    }

    /**
     * Analisa a existência de colisão com outra instância de Hitbox.
     * <p>
     * Executa um teste rápido de sobreposição de caixas delimitadoras, seguido de verificação precisa.
     * </p>
     *
     * @param other a outra geometria a ser testada
     * @return {@code true} se houver interseção física, caso contrário {@code false}
     */
    public boolean intersected(Hitbox other) {
        if (!this.roughHitbox.isIntersecting(other.getRoughHitbox())) return false;

        for (LineSegment line : lines)
            for (LineSegment otherLine : other.getLines()) if (line.intersects(otherLine)) return true;

        return false;
    }

    /**
     * Renderiza a representação visual da Hitbox e da sua caixa delimitadora.
     * <p>
     * Útil para fins de depuração.
     * </p>
     *
     * @param p       referência para o contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho das linhas
     * @param plt     sistema de conversão de coordenadas do mundo para pixéis
     */
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        draw(painter, plt);
        roughHitbox.draw(p, plt);
    }

    /**
     * Executa o desenho vetorial dos segmentos de reta que compõem a Hitbox.
     *
     * @param painter objeto responsável pelo desenho das linhas
     * @param plt     sistema de conversão de coordenadas
     */
    public void draw(LinePainter painter, SubPlot plt) {
        for (LineSegment line : lines) line.draw(painter, plt);
    }

    /**
     * Gera os segmentos de reta ligando sequencialmente os vértices fornecidos.
     * <p>
     * Fecha a forma conectando o último ponto ao primeiro.
     * </p>
     *
     * @param points a lista de vértices da forma
     */
    private void formLines(List<Point> points) {
        lines.clear();
        for (int i = 0; i < points.size(); ++i) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());

            lines.add(new LineSegment(position, p1, p2));
        }
    }

    /**
     * Calcula as dimensões baseando-se nos vértices fornecidos.
     * <p>
     * Define os valores internos de largura e altura.
     * </p>
     *
     * @param points a lista de vértices da forma
     */
    private void calculateDimensions(List<Point> points) {
        float minX = Float.MAX_VALUE, maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

        for (Point p : points) {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    /**
     * Implementa uma caixa delimitadora alinhada ao eixo para otimização espacial.
     */
    public static class RoughHitbox {
        Point position;
        float minX, maxX, minY, maxY;

        /**
         * Inicializa a RoughHitbox calculando os limites extremos.
         *
         * @param points   lista de pontos da forma original
         * @param position a posição global da Hitbox
         */
        RoughHitbox(List<Point> points, Point position) {
            this.position = position;

            minX = Float.MAX_VALUE;
            maxX = -Float.MAX_VALUE;
            minY = Float.MAX_VALUE;
            maxY = -Float.MAX_VALUE;

            for (Point p : points) {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y);
                maxY = Math.max(maxY, p.y);
            }
        }

        /**
         * Verifica a sobreposição entre esta caixa delimitadora e outra.
         *
         * @param other a outra instância de RoughHitbox
         * @return {@code true} se as caixas se sobrepuserem, {@code false} caso contrário
         */
        public boolean isIntersecting(RoughHitbox other) {
            float thisLeft = this.position.x + this.minX;
            float thisRight = this.position.x + this.maxX;
            float thisTop = this.position.y + this.minY;
            float thisBottom = this.position.y + this.maxY;

            float otherLeft = other.position.x + other.minX;
            float otherRight = other.position.x + other.maxX;
            float otherTop = other.position.y + other.minY;
            float otherBottom = other.position.y + other.maxY;

            boolean overlapX = thisLeft < otherRight && thisRight > otherLeft;
            boolean overlapY = thisTop < otherBottom && thisBottom > otherTop;

            return overlapX && overlapY;
        }

        /**
         * Desenha os limites da caixa no ecrã para efeitos de visualização.
         *
         * @param p   referência para o contexto gráfico
         * @param plt sistema de conversão de coordenadas
         */
        public void draw(PApplet p, SubPlot plt) {
            p.pushStyle();
            p.noFill();
            p.stroke(0, 255, 0);
            p.strokeWeight(1);

            float w = maxX - minX;
            float h = maxY - minY;
            float centerX = position.x + (minX + maxX) / 2;
            float centerY = position.y + (minY + maxY) / 2;

            float[] c = plt.getPixelCoord(centerX, centerY);
            float[] dims = plt.getDimInPixel(w, h);
            p.rectMode(PApplet.CENTER);
            p.rect(c[0], c[1], dims[0], dims[1]);

            p.popStyle();
        }
    }
}