package game.hitbox;

import processing.core.PVector;

public class LineEquation {
    private PVector position;
    private PVector start;
    private PVector stop;
    private PVector result = new PVector(0.0f, 0.0f);
    private float k;
    private float a;

    public LineEquation(LineSegment segment) {
        this.position = segment.getPosition();
        this.start = segment.getStart();
        this.stop = segment.getStop();
    }

    public void formEquation() {
        if (stop.x == start.x) {
            k = 0.0f;
        } else {
            k = (stop.y - start.y) / (stop.x - start.x);
        }
        a = start.y + position.y - k * (start.x + position.x);
    }

    public float getK() {
        return k;
    }

    public float getA() {
        return a;
    }

    public PVector solveIntersectionPoint(LineEquation other) {
        this.formEquation();
        other.formEquation();

        float resultK = this.k - other.getK();
        float resultA = other.getA() - this.a;

        if (resultK != 0.0f) {
            resultA /= resultK;
        } else {
            resultA = 0.0f;
        }

        float x = resultA;

        result.x = x;
        result.y = calculate(x);

        return result;
    }

    public float calculate(float x) {
        return k * x + a;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }
}