package game.characters;

import game.core.SubPlot;
import game.hitbox.LinePainter;
import processing.core.PApplet;

public interface IVisualizable {
    void display(PApplet p, LinePainter painter, SubPlot plt);
}