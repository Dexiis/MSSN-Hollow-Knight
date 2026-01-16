package game.hitbox;

import processing.core.PApplet;
import processing.core.PVector;

public class LineSegment {
    private PVector position;
    private final PVector start;
    private final PVector stop;
    private LineEquation equation;

    public LineSegment(PVector position, PVector start, PVector stop) {
        this.position = position;
        this.start = start;
        this.stop = stop;
        this.equation = new LineEquation(this);
    }

    public LineEquation getEquation() {
        return equation;
    }

    public PVector getStart() {
        return start;
    }

    public PVector getStop() {
        return stop;
    }

    public PVector getPosition() {
        return position;
    }

    public boolean isVertical() {
        return Math.abs(start.x - stop.x) < 0.001f;
    }

    public boolean intersects(LineSegment other) {
        if (this.isVertical() && other.isVertical()) {
            if (Math.abs((this.start.x + position.x) - (other.start.x + other.position.x)) > 0.001f) return false;

            return rangeOverlap(this.start.y + position.y, this.stop.y + position.y, other.start.y + other.position.y, other.stop.y + other.position.y);
        }

        if (this.isVertical() || other.isVertical()) {
            LineSegment vert = this.isVertical() ? this : other;
            LineSegment nonVert = this.isVertical() ? other : this;

            nonVert.getEquation().formEquation();

            float vertX = vert.start.x + vert.position.x;
            float intersectionY = nonVert.getEquation().calculate(vertX);

            return vert.isPointOnLine(vertX, intersectionY) && nonVert.isPointOnLine(vertX, intersectionY);
        }

        PVector p = equation.solveIntersectionPoint(other.getEquation());
        return this.isPointOnLine(p.x, p.y) && other.isPointOnLine(p.x, p.y);
    }

    private boolean rangeOverlap(float a1, float a2, float b1, float b2) {
        return Math.max(a1, a2) >= Math.min(b1, b2) && Math.min(a1, a2) <= Math.max(b1, b2);
    }

    public boolean isPointOnLine(float x, float y) {
        float epsilon = 0.05f;
        float xGlobal = position.x;
        float yGlobal = position.y;

        boolean betweenX = x >= Math.min(start.x, stop.x) + xGlobal - epsilon && x <= Math.max(start.x, stop.x) + xGlobal + epsilon;

        boolean betweenY = y >= Math.min(start.y, stop.y) + yGlobal - epsilon && y <= Math.max(start.y, stop.y) + yGlobal + epsilon;

        return betweenX && betweenY;
    }

    public void display(PApplet p) {
        float x1 = start.x + position.x;
        float y1 = start.y + position.y;
        float x2 = stop.x + position.x;
        float y2 = stop.y + position.y;

        p.pushStyle();
        p.stroke(0);
        p.strokeWeight(2);
        p.line(x1, y1, x2, y2);
        p.popStyle();
    }

    public void setPosition(PVector position) {
        this.position = position;
        this.equation.setPosition(position);
    }
}