package game.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma área de colisão (Hitbox) composta por segmentos de reta.
 * <p>
 * Esta classe utiliza uma abordagem de duas etapas para detetar colisões:
 * 1. Verifica uma "RoughHitbox" (caixa delimitadora simples) para rejeição rápida.
 * 2. Verifica interseções detalhadas segmento por segmento se a primeira verificação passar.
 */
public class Hitbox {
    private final RoughHitbox roughHitbox;
    private Point position = new Point(0.0f, 0.0f);
    private final List<LineSegment> lines = new ArrayList<LineSegment>();

    /**
     * Construtor da Hitbox.
     * Cria as linhas de colisão com base numa lista ordenada de pontos (vértices).
     *
     * @param points Lista de pontos que definem o polígono da Hitbox.
     */
    public Hitbox(List<Point> points) {
        formLines(points);
        roughHitbox = new RoughHitbox(lines, position);
    }

    private void formLines(List<Point> points) {
        for (int i = 0; i < points.size(); ++i) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());

            lines.add(new LineSegment(position, p1, p2));
        }
    }

    /**
     * Obtém a caixa de colisão aproximada (Bounding Box).
     * Útil para verificações rápidas de interseção ou renderização de depuração.
     *
     * @return A instância de {@link RoughHitbox} associada a esta Hitbox.
     */
    public RoughHitbox getRoughHitbox() {
        return roughHitbox;
    }

    /**
     * Verifica se esta Hitbox interseta (colide) com outra Hitbox.
     * <p>
     * Primeiro verifica a interseção das caixas aproximadas (RoughHitbox).
     * Se estas colidirem, verifica a interseção detalhada entre cada segmento de reta.
     *
     * @param other A outra Hitbox para verificar a colisão.
     * @return {@code true} se houver colisão, {@code false} caso contrário.
     */
    public boolean intersects(Hitbox other) {
        if (!this.roughHitbox.intersects(other.getRoughHitbox())) {
            return false;
        }
        for (LineSegment line : lines) {
            for (LineSegment otherLine : other.getLines()) {
                if (line.intersects(otherLine)) return true;
            }
        }
        return false;
    }

    /**
     * Define a nova posição da Hitbox no mundo.
     * Atualiza tanto a posição central como a posição de todos os segmentos de reta e da RoughHitbox.
     *
     * @param position O novo ponto de posição (x, y).
     */
    public void setPosition(Point position) {
        this.position = position;
        this.roughHitbox.position = position;
        for (LineSegment line : lines) {
            line.setPosition(position);
        }
    }

    /**
     * Desenha a Hitbox utilizando um pintor de linhas abstrato.
     * Útil para sistemas gráficos que não dependem diretamente do Processing.
     *
     * @param painter O objeto responsável por desenhar as linhas.
     */
    public void draw(LinePainter painter) {
        for (LineSegment line : lines) {
            Point p1 = line.getStart();
            Point p2 = line.getStop();
            Point posOffset = line.getPosition();

            float x1 = p1.pos.x + posOffset.pos.x;
            float y1 = p1.pos.y + posOffset.pos.y;
            float x2 = p2.pos.x + posOffset.pos.x;
            float y2 = p2.pos.y + posOffset.pos.y;

            painter.paintLine(x1, y1, x2, y2);
        }
    }

    /**
     * Desenha a Hitbox na tela utilizando a biblioteca Processing.
     * Converte as coordenadas do mundo para coordenadas de pixel utilizando o SubPlot.
     *
     * @param p   A instância da PApplet (contexto gráfico do Processing).
     * @param plt O objeto SubPlot usado para conversão de coordenadas.
     */
    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.stroke(255, 0, 255);
        p.strokeWeight(2);

        for (LineSegment line : lines) {
            Point p1 = line.getStart();
            Point p2 = line.getStop();
            Point posOffset = line.getPosition();

            float wx1 = p1.pos.x + posOffset.pos.x;
            float wy1 = p1.pos.y + posOffset.pos.y;
            float wx2 = p2.pos.x + posOffset.pos.x;
            float wy2 = p2.pos.y + posOffset.pos.y;

            float[] c1 = plt.getPixelCoord(wx1, wy1);
            float[] c2 = plt.getPixelCoord(wx2, wy2);

            p.line(c1[0], c1[1], c2[0], c2[1]);
        }
        p.popStyle();
    }

    /**
     * Obtém a lista de segmentos de reta que compõem esta Hitbox.
     *
     * @return Lista de {@link LineSegment}.
     */
    public List<LineSegment> getLines() {
        return lines;
    }

    /**
     * Classe utilitária (Builder Pattern) para facilitar a criação de instâncias de Hitbox.
     * Permite adicionar pontos sequencialmente antes de criar o objeto final.
     */
    public static class Builder {
        private List<Point> points = new ArrayList<>();

        /**
         * Adiciona um ponto existente à lista de vértices da Hitbox.
         *
         * @param point O ponto a adicionar.
         * @return O próprio Builder para encadeamento de chamadas.
         */
        public Builder addPoint(Point point) {
            points.add(point);
            return this;
        }

        /**
         * Cria e adiciona um novo ponto à lista de vértices da Hitbox com base nas coordenadas.
         *
         * @param x Coordenada X.
         * @param y Coordenada Y.
         * @return O próprio Builder para encadeamento de chamadas.
         */
        public Builder addPoint(float x, float y) {
            points.add(new Point(x, y));
            return this;
        }

        /**
         * Finaliza a construção e retorna uma nova instância de Hitbox.
         *
         * @return Uma nova Hitbox configurada com os pontos adicionados.
         */
        public Hitbox build() {
            return new Hitbox(points);
        }
    }

    /**
     * Representa uma área retangular aproximada (Axis-Aligned Bounding Box - AABB).
     * Usada para otimizar a deteção de colisões, descartando rapidamente casos onde
     * não há sobreposição.
     */
    public class RoughHitbox {
        private static final long serialVersionUID = 1L;
        float width = 0.0f;
        float height = 0.0f;
        Point position;

        /**
         * Construtor da RoughHitbox.
         * Calcula automaticamente a largura e altura baseada nos extremos (mínimo e máximo)
         * das coordenadas dos segmentos fornecidos.
         *
         * @param segments Lista de segmentos que compõem a forma original.
         * @param position A posição inicial da caixa.
         */
        RoughHitbox(List<LineSegment> segments, Point position) {
            float xMin = Float.MAX_VALUE, xMax = Float.MIN_VALUE;
            float yMin = Float.MAX_VALUE, yMax = Float.MIN_VALUE;

            for (LineSegment seg : segments) {
                float startX = seg.getStart().pos.x;
                float startY = seg.getStart().pos.y;
                float stopX = seg.getStop().pos.x;
                float stopY = seg.getStop().pos.y;

                xMax = Math.max(xMax, Math.max(startX, stopX));
                xMin = Math.min(xMin, Math.min(startX, stopX));
                yMax = Math.max(yMax, Math.max(startY, stopY));
                yMin = Math.min(yMin, Math.min(startY, stopY));
            }

            width = xMax - xMin;
            height = yMax - yMin;
            this.position = position;
        }

        /**
         * Verifica a interseção entre duas RoughHitboxes (lógica AABB).
         *
         * @param other A outra RoughHitbox para verificar.
         * @return {@code true} se os retângulos se sobrepõem, {@code false} caso contrário.
         */
        public boolean intersects(RoughHitbox other) {
            float thisX = this.position.pos.x;
            float thisY = this.position.pos.y;
            float otherX = other.position.pos.x;
            float otherY = other.position.pos.y;

            return (thisX < otherX + other.width && thisX + this.width > otherX && thisY < otherY + other.height && thisY + this.height > otherY);
        }

        /**
         * Obtém a posição atual da RoughHitbox.
         *
         * @return O ponto representando a posição (canto superior esquerdo ou centro, dependendo da implementação do Point).
         */
        public Point getPosition() {
            return position;
        }
    }
}