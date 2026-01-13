package game.characters.hitbox;

import processing.core.PApplet;

public class LineDrawer implements LinePainter {
    private final PApplet pApplet;

    public LineDrawer(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    @Override
    public void paintLine(float x1, float y1, float x2, float y2) {
        pApplet.line(x1, y1, x2, y2);
    }
}