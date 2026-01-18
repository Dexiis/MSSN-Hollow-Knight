package game.scenery.characters;

import game.core.SubPlot;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;

public interface IVisualizable {
    void display(PApplet p, LinePainter painter, SubPlot plt);
}