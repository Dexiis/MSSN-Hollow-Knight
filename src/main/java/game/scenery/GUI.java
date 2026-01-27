package game.scenery;

import game.scenery.characters.types.TheKnight;
import processing.core.PApplet;

//TODO VIDA, COOLDOWNS, DASHES, ETC
public class GUI {
    private PApplet p;

    public GUI(PApplet p) {
        this.p = p;
    }

    public void display(TheKnight player) {
        p.fill(0);
        p.text("Health: " + player.getHealth(), 10, 20);
    }
}
