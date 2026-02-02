package game.scenery.components.hitbox;

import game.core.SubPlot;

public interface LinePainter {
    void paintLine(float x1, float y1, float x2, float y2, SubPlot plt);
}