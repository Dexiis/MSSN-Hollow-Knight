package game.scenery.components.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma estrutura geométrica usada para deteção de colisões.
 * <p>
 * Esta classe suporta formas poligonais e retangulares, recorrendo a uma abordagem
 * em duas fases: uma verificação aproximada através de uma caixa delimitadora e,
 * caso necessário, uma verificação precisa baseada em segmentos de reta.
 * </p>
 */
public class Hitbox {

    protected final List<LineSegment> lines = new ArrayList<>();
    protected Point position = new Point(0.0f, 0.0f);
    protected final RoughHitbox roughHitbox;
    protected float width, height;

    /**
     * Cria uma área de colisão definida por uma forma poligonal.
     * <p>
     * A geometria interna é construída a partir da lista de vértices fornecida,
     * sendo igualmente calculadas as dimensões globais e a respetiva caixa
     * delimitadora aproximada.
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
     * Cria uma área de colisão com formato retangular.
     * <p>
     * Os vértices são gerados automaticamente com base na posição central
     * e nas dimensões fornecidas.
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
     * Devolve a altura total da área de colisão.
     * <p>
     * O valor corresponde à extensão vertical máxima da forma,
     * calculada a partir dos seus vértices.
     * </p>
     *
     * @return a altura em unidades do mundo
     */
    public float getHeight() {
        return height;
    }

    /**
     * Disponibiliza os segmentos de reta que definem o perímetro.
     * <p>
     * Cada segmento liga dois vértices consecutivos da forma,
     * permitindo testes de interseção precisos.
     * </p>
     *
     * @return a lista de segmentos que compõem a área de colisão
     */
    public List<LineSegment> getLines() {
        return lines;
    }

    /**
     * Devolve a posição atual da área de colisão.
     * <p>
     * Esta posição representa o ponto central usado como referência
     * para todos os cálculos geométricos.
     * </p>
     *
     * @return o ponto central da área de colisão
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Fornece a caixa delimitadora aproximada associada.
     * <p>
     * Esta estrutura é utilizada para uma verificação rápida de possíveis
     * colisões antes de proceder a cálculos mais detalhados.
     * </p>
     *
     * @return a instância de RoughHitbox associada
     */
    public RoughHitbox getRoughHitbox() {
        return roughHitbox;
    }

    /**
     * Devolve a largura total da área de colisão.
     * <p>
     * O valor corresponde à extensão horizontal máxima da forma,
     * calculada a partir dos seus vértices.
     * </p>
     *
     * @return a largura em unidades do mundo
     */
    public float getWidth() {
        return width;
    }

    /**
     * Atualiza a posição da área de colisão.
     * <p>
     * A nova posição é propagada para a caixa delimitadora aproximada
     * e para todos os segmentos que compõem o perímetro.
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
     * Atualiza a posição usando um vetor do Processing.
     * <p>
     * O vetor fornecido é convertido internamente para um ponto
     * antes de atualizar a posição global da área de colisão.
     * </p>
     *
     * @param position o novo vetor de posição
     */
    public void setPosition(PVector position) {
        setPosition(new Point(position.x, position.y));
    }

    /**
     * Avalia a existência de interseção com outra área de colisão.
     * <p>
     * A verificação inicia-se com um teste rápido entre caixas
     * delimitadoras aproximadas e, se necessário, prossegue com
     * testes detalhados entre os segmentos de ambas as formas.
     * </p>
     *
     * @param other a outra área de colisão a analisar
     * @return {@code true} se existir interseção física, {@code false} caso contrário
     */
    public boolean intersected(Hitbox other) {
        if (!this.roughHitbox.isIntersecting(other.getRoughHitbox())) return false;

        for (LineSegment line : lines)
            for (LineSegment otherLine : other.getLines()) if (line.intersects(otherLine)) return true;

        return false;
    }

    /**
     * Calcula as dimensões globais da forma.
     * <p>
     * A largura e a altura são determinadas com base nos valores
     * mínimos e máximos dos vértices fornecidos.
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
     * Constrói os segmentos que ligam os vértices da forma.
     * <p>
     * Os pontos são ligados de forma sequencial, sendo garantido
     * o fecho da forma ao conectar o último vértice ao primeiro.
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
     * Apresenta visualmente a área de colisão e a sua caixa delimitadora.
     * <p>
     * Esta representação gráfica é usada principalmente para apoio
     * à análise e validação do comportamento de colisões.
     * </p>
     *
     * @param p       referência para o contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho dos segmentos
     * @param plt     sistema de conversão de coordenadas
     */
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        draw(painter, plt);
        roughHitbox.draw(p, plt);
    }

    /**
     * Desenha os segmentos que compõem o perímetro da área de colisão.
     * <p>
     * Cada segmento é convertido para coordenadas de ecrã antes
     * de ser representado graficamente.
     * </p>
     *
     * @param painter objeto responsável pelo desenho dos segmentos
     * @param plt     sistema de conversão de coordenadas
     */
    public void draw(LinePainter painter, SubPlot plt) {
        for (LineSegment line : lines) line.draw(painter, plt);
    }

    /**
     * Representa uma caixa delimitadora alinhada aos eixos.
     * <p>
     * Esta estrutura permite uma verificação rápida de sobreposição
     * espacial, reduzindo o custo de cálculos de colisão detalhados.
     * </p>
     */
    public static class RoughHitbox {
        Point position;
        float minX, maxX, minY, maxY;

        /**
         * Cria a caixa delimitadora a partir dos vértices da forma.
         * <p>
         * Os limites extremos são calculados relativamente à posição
         * global da área de colisão associada.
         * </p>
         *
         * @param points   lista de vértices da forma original
         * @param position a posição global da área de colisão
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
         * Avalia a sobreposição entre duas caixas delimitadoras.
         * <p>
         * A verificação é feita de forma independente nos eixos
         * horizontal e vertical.
         * </p>
         *
         * @param other a outra caixa delimitadora a comparar
         * @return {@code true} se existir sobreposição, {@code false} caso contrário
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
         * Desenha visualmente os limites da caixa delimitadora.
         * <p>
         * Esta representação é utilizada para efeitos de depuração
         * e análise do comportamento de colisões.
         * </p>
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