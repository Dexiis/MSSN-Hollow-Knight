package game.hitbox;

import game.core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Hitbox {
    protected final RoughHitbox roughHitbox;
    protected final List<LineSegment> lines = new ArrayList<>();
    protected PVector position = new PVector(0.0f, 0.0f);
    protected float width, height;

    public Hitbox(List<PVector> points) {
        formLines(points);
        roughHitbox = new RoughHitbox(lines, position);
        this.width = Math.abs(points.get(1).x - points.get(0).x);
        this.height = Math.abs(points.get(1).y - points.get(2).y);
    }

    public Hitbox(PVector position, float width, float height) {
        this.width = width;
        this.height = height;

        ArrayList<PVector> points = new ArrayList<>();
        points.add(new PVector(position.x - width / 2, position.y - height / 2));
        points.add(new PVector(position.x + width / 2, position.y - height / 2));
        points.add(new PVector(position.x + width / 2, position.y + height / 2));
        points.add(new PVector(position.x - width / 2, position.y + height / 2));
        formLines(points);

        this.position = position;
        this.roughHitbox = new RoughHitbox(lines, this.position);
    }

    private void formLines(List<PVector> points) {
        for (int i = 0; i < points.size(); ++i) {
            PVector p1 = points.get(i);
            PVector p2 = points.get((i + 1) % points.size());

            lines.add(new LineSegment(position, p1, p2));
        }
    }

    public RoughHitbox getRoughHitbox() {
        return roughHitbox;
    }

    public boolean intersected(Hitbox other) {
        if (!this.roughHitbox.intersected(other.getRoughHitbox())) {
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
        this.position = position;
        this.roughHitbox.position = position;
        for (LineSegment line : lines) {
            line.setPosition(position);
        }
    }

    public PVector getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.stroke(255, 0, 255);
        p.strokeWeight(2);

        for (LineSegment line : lines) {
            PVector p1 = line.getStart();
            PVector p2 = line.getStop();
            PVector posOffset = line.getPosition();

            float wx1 = p1.x + posOffset.x;
            float wy1 = p1.y + posOffset.y;
            float wx2 = p2.x + posOffset.x;
            float wy2 = p2.y + posOffset.y;

            float[] c1 = plt.getPixelCoord(wx1, wy1);
            float[] c2 = plt.getPixelCoord(wx2, wy2);

            p.line(c1[0], c1[1], c2[0], c2[1]);
        }

        p.stroke(255, 0, 255);
        p.strokeWeight(5);
        float[] cPos = plt.getPixelCoord(position.x, position.y);
        p.point(cPos[0], cPos[1]);

        p.popStyle();
        roughHitbox.display(p, plt);
    }

    public List<LineSegment> getLines() {
        return lines;
    }

    public static class Builder {
        private List<PVector> points = new ArrayList<>();

        public Builder addPoint(PVector point) {
            points.add(point);
            return this;
        }

        public Builder addPoint(float x, float y) {
            points.add(new PVector(x, y));
            return this;
        }

        public Hitbox build() {
            return new Hitbox(points);
        }
    }

    public class RoughHitbox {
        float width = 0.0f;
        float height = 0.0f;
        PVector position;

        RoughHitbox(List<LineSegment> segments, PVector position) {
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

        public boolean intersected(RoughHitbox other) {
            float thisX = this.position.x;
            float thisY = this.position.y;
            float otherX = other.position.x;
            float otherY = other.position.y;

            return (thisX < otherX + other.width && thisX + this.width > otherX && thisY < otherY + other.height && thisY + this.height > otherY);
        }

        public void display(PApplet p, SubPlot plt) {
            p.pushStyle();
            p.stroke(0, 255, 0);
            p.strokeWeight(2);

            for (LineSegment line : lines) {
                PVector p1 = line.getStart();
                PVector p2 = line.getStop();
                PVector posOffset = line.getPosition();

                float wx1 = p1.x + posOffset.x;
                float wy1 = p1.y + posOffset.y;
                float wx2 = p2.x + posOffset.x;
                float wy2 = p2.y + posOffset.y;

                float[] c1 = plt.getPixelCoord(wx1, wy1);
                float[] c2 = plt.getPixelCoord(wx2, wy2);

                p.line(c1[0], c1[1], c2[0], c2[1]);
            }

            p.stroke(0, 255, 0);
            p.strokeWeight(5);
            float[] cPos = plt.getPixelCoord(position.x, position.y);
            p.point(cPos[0], cPos[1]);

            p.popStyle();
        }

        public PVector getPosition() {
            return position;
        }
    }
}