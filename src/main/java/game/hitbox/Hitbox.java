package game.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma área de colisão (Hitbox) que pode ser poligonal ou retangular.
 * <p>
 * Esta classe gere a geometria da colisão utilizando uma lista de segmentos de reta
 * e uma caixa delimitadora aproximada (RoughHitbox) para otimização de desempenho.
 */
public class Hitbox {

    protected final RoughHitbox roughHitbox;
    protected final List<LineSegment> lines = new ArrayList<>();

    protected Point position = new Point(0.0f, 0.0f);
    protected float width, height;

    /**
     * Construtor para criar uma Hitbox baseada numa forma complexa (Polígono).
     *
     * @param points Lista de pontos que definem os vértices do polígono em coordenadas locais.
     */
    public Hitbox(List<Point> points) {
        formLines(points);
        calculateDimensions(points);
        roughHitbox = new RoughHitbox(points, position);
    }

    /**
     * Construtor utilitário para criar uma Hitbox a partir de uma lista de PVectors do Processing.
     * Converte internamente os PVectors para a classe Point do sistema.
     *
     * @param points ArrayList de PVector contendo os vértices.
     */
    public Hitbox(ArrayList<PVector> points) {
        List<Point> newPoints = new ArrayList<>();
        for (PVector point : points) newPoints.add(new Point(point.x, point.y));

        formLines(newPoints);
        calculateDimensions(newPoints);
        roughHitbox = new RoughHitbox(newPoints, position);
    }

    /**
     * Construtor para criar uma Hitbox retangular simples.
     * Gera automaticamente os quatro vértices baseados na largura e altura fornecidas.
     *
     * @param position A posição central da Hitbox.
     * @param width    A largura do retângulo.
     * @param height   A altura do retângulo.
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
     * Cria os segmentos de reta (LineSegment) ligando os pontos sequencialmente.
     *
     * @param points A lista de vértices da forma.
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
     * Calcula a largura e altura reais da Hitbox iterando por todos os pontos para encontrar os extremos.
     *
     * @param points A lista de vértices da forma.
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
     * Verifica se esta Hitbox colide com outra Hitbox.
     * O processo ocorre em duas fases: primeiro verifica a RoughHitbox (rápido),
     * e se houver sobreposição, verifica detalhadamente linha por linha (preciso).
     *
     * @param other A outra Hitbox para verificar a colisão.
     * @return {@code true} se houver colisão, {@code false} caso contrário.
     */
    public boolean intersected(Hitbox other) {
        if (!this.roughHitbox.isIntersecting(other.getRoughHitbox())) return false;

        for (LineSegment line : lines)
            for (LineSegment otherLine : other.getLines()) if (line.intersects(otherLine)) return true;

        return false;
    }

    /**
     * Define a nova posição da Hitbox utilizando a classe Point.
     * Atualiza também a posição da RoughHitbox e de todos os segmentos de reta.
     *
     * @param position O novo ponto de posição.
     */
    public void setPosition(Point position) {
        this.position = position;
        this.roughHitbox.position = position;
        for (LineSegment line : lines) line.setPosition(position);
    }

    /**
     * Sobrecarga do método setPosition que aceita um PVector.
     *
     * @param position O novo vetor de posição.
     */
    public void setPosition(PVector position) {
        setPosition(new Point(position.x, position.y));
    }

    /**
     * Obtém a RoughHitbox associada a esta Hitbox.
     *
     * @return O objeto RoughHitbox.
     */
    public RoughHitbox getRoughHitbox() {
        return roughHitbox;
    }

    /**
     * Obtém a posição atual da Hitbox.
     *
     * @return O ponto de posição.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Obtém a largura da Hitbox.
     *
     * @return A largura em unidades do mundo.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Obtém a altura da Hitbox.
     *
     * @return A altura em unidades do mundo.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Obtém a lista de segmentos de reta que compõem a Hitbox.
     *
     * @return Lista de LineSegment.
     */
    public List<LineSegment> getLines() {
        return lines;
    }

    /**
     * Desenha as linhas da Hitbox utilizando um pintor abstrato e o sistema de coordenadas.
     *
     * @param painter O objeto responsável por desenhar as linhas.
     * @param plt     O objeto SubPlot para conversão de coordenadas.
     */
    public void draw(LinePainter painter, SubPlot plt) {
        for (LineSegment line : lines) line.draw(painter, plt);
    }

    /**
     * Método de conveniência para exibir a Hitbox, podendo incluir elementos de depuração.
     *
     * @param p       O contexto gráfico do Processing.
     * @param painter O objeto responsável por desenhar as linhas.
     * @param plt     O objeto SubPlot para conversão de coordenadas.
     */
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        draw(painter, plt);
    }

    /**
     * Classe interna que representa uma Caixa Delimitadora Alinhada ao Eixo (AABB - Axis-Aligned Bounding Box).
     * Usada para otimizar a deteção de colisões rejeitando rapidamente casos onde não há sobreposição.
     */
    public class RoughHitbox {
        float minX, maxX, minY, maxY;
        Point position;

        /**
         * Construtor da RoughHitbox.
         * Calcula os limites locais (min/max) baseando-se nos pontos fornecidos.
         *
         * @param points   Lista de pontos da forma original.
         * @param position A posição global da Hitbox.
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
         * Verifica se esta RoughHitbox interseta outra, utilizando lógica AABB.
         * Converte coordenadas locais para globais durante a verificação.
         *
         * @param other A outra RoughHitbox.
         * @return {@code true} se houver sobreposição, {@code false} caso contrário.
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
         * Desenha a RoughHitbox no ecrã (geralmente para fins de depuração).
         *
         * @param p   O contexto gráfico do Processing.
         * @param plt O objeto SubPlot para conversão de coordenadas.
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