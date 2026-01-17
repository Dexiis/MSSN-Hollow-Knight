package game.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;


/**
 * A 2D polygon hitbox which is formed by an arbitrary amount of points.
 * This works by connecting the points to each other in the order in which
 * they are given, with the last point connected to the first
 */
public class Hitbox {
    protected final RoughHitbox roughHitbox;
    protected final List<LineSegment> lines = new ArrayList<>();

    protected Point position = new Point(0.0f, 0.0f);
    protected float width, height;

    public Hitbox(ArrayList<PVector> points) {
        List<Point> newPoints = new ArrayList<>();
        for (PVector point : points) newPoints.add(new Point(point.x, point.y));
        formLines(newPoints);
        roughHitbox = new RoughHitbox(lines, position);
        this.width = Math.abs(points.get(1).x - points.get(0).x);
        this.height = Math.abs(points.get(1).y - points.get(2).y);
    }

    public Hitbox(List<Point> points) {
        formLines(points);
        roughHitbox = new RoughHitbox(lines, position);
        this.width = Math.abs(points.get(1).x - points.get(0).x);
        this.height = Math.abs(points.get(1).y - points.get(2).y);
    }

    public Hitbox(Point position, float width, float height) {
        this.width = width;
        this.height = height;

        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(position.x - width / 2, position.y - height / 2));
        points.add(new Point(position.x + width / 2, position.y - height / 2));
        points.add(new Point(position.x + width / 2, position.y + height / 2));
        points.add(new Point(position.x - width / 2, position.y + height / 2));
        formLines(points);

        this.position = position;
        this.roughHitbox = new RoughHitbox(lines, this.position);
    }

    /**
     * Forms the {@link LineSegment}s based on the list of {@link Point}s
     */
    private void formLines(List<Point> points) {
        for (int i = 0; i < points.size(); ++i) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());

            lines.add(new LineSegment(position, p1, p2));
        }
    }

    /**
     * @return a rectangular hitbox which envelops the polygon hitbox
     */
    public RoughHitbox getRoughHitbox() {
        return roughHitbox;
    }

    public boolean intersected(Hitbox other) {
        if (!this.roughHitbox.isIntersecting(other.getRoughHitbox())) {
            return false;
        }
        for (LineSegment line : lines) {
            for (LineSegment otherLine : other.getLines()) {
                if (line.intersects(otherLine)) return true;
            }
        }
        return false;
    }

    public void setPosition(PVector position) {
        setPosition(new Point(position.x, position.y));
    }

    public void setPosition(Point position) {
        this.position = position;
        this.roughHitbox.position = position;
        for (LineSegment line : lines) {
            line.setPosition(position);
        }
    }

    public Point getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Draws the hitbox using the given {@LinePainter}.
     * Used for visualizing in case of e.g. debugging
     */
    public void draw(LinePainter painter, SubPlot plt) {
        for (LineSegment line : lines) {
            line.draw(painter, plt);
        }
    }

    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        draw(painter, plt);
        roughHitbox.draw(p, plt);
    }

    public List<LineSegment> getLines() {
        return lines;
    }

    public static class Builder {
        private List<Point> points = new ArrayList<>();

        public Builder addPoint(Point point) {
            points.add(point);
            return this;
        }

        public Builder addPoint(float x, float y) {
            points.add(new Point(x, y));
            return this;
        }

        public Hitbox build() {
            return new Hitbox(points);
        }
    }

    public class RoughHitbox {
        float width = 0.0f;
        float height = 0.0f;
        Point position;

        // Constructs the rough hitbox so that its height and width conforms to the height,
        // width and position of the polygon hitbox which it envelops
        RoughHitbox(List<LineSegment> segments, Point position) {
            float xMin = Float.MAX_VALUE, xMax = Float.MIN_VALUE;
            float yMin = Float.MAX_VALUE, yMax = Float.MIN_VALUE;

            for (LineSegment seg : segments) {

                float startX = seg.getStart().x;
                float startY = seg.getStart().y;
                float stopX = seg.getStop().x;
                float stopY = seg.getStop().y;

                xMax = Math.max(xMax, Math.max(startX, stopX));
                xMin = Math.min(xMin, Math.min(startX, stopX));
                yMax = Math.max(yMax, Math.max(startY, stopY));
                yMin = Math.min(yMin, Math.min(startY, stopY));
            }

            width = xMax - xMin;
            height = yMax - yMin;
            this.position = position;
        }

        public boolean isIntersecting(RoughHitbox other) {

            // Check if the the right wall is between the left and right wall of the other hitbox (and vice versa)
            boolean xOverlap = (this.position.x + this.width >= other.position.x && this.position.x + this.width < other.position.x + other.width) || (other.position.x + other.width >= this.position.x && other.position.x + other.width < this.position.x + this.width);

            // Check if the the bottom wall is between the top and bottom wall of the other hitbox (and vice versa)
            boolean yOverlap = (this.position.y + this.height >= other.position.y && this.position.y + this.height < other.position.y + other.height) || (other.position.y + other.height >= this.position.y && other.position.y + other.height < this.position.y + this.height);

            return xOverlap && yOverlap;
        }

        public void draw(PApplet p, SubPlot plt) {
            p.pushStyle();
            p.noFill();
            p.stroke(0, 255, 0);
            p.strokeWeight(1);

            float[] c = plt.getPixelCoord(position.x, position.y);
            float[] dims = plt.getDimInPixel(width, height);
            p.rectMode(PApplet.CENTER);
            p.rect(c[0], c[1], dims[0], dims[1]);

            p.popStyle();
        }

        public Point getPosition() {
            return position;
        }
    }
}