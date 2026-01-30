package game.scenery;

import game.scenery.characters.types.TheKnight;
import processing.core.PApplet;

//TODO VIDA, COOLDOWNS, DASHES, ETC
public class GUI {
    private PApplet p;

    /**
     * Gere a interface gráfica do utilizador.
     *
     * @param p o contexto gráfico do Processing
     */
    public GUI(PApplet p) {
        this.p = p;
    }

    /**
     * Exibe a interface com informações do jogador.
     *
     * @param player o cavaleiro para obter as informações
     */
    public void display(TheKnight player) {
        p.fill(255);
        p.text("Health: " + player.getHealth(), 10, 20);
    }
}
