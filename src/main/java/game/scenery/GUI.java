package game.scenery;

import game.scenery.characters.types.TheKnight;
import processing.core.PApplet;

public class GUI {
    private static GUI gui = null;

    private final PApplet p;

    /**
     * Gere a interface gráfica do utilizador.
     *
     * @param p o contexto gráfico do Processing
     */
    private GUI(PApplet p) {
        this.p = p;
    }

    public static GUI getInstance() {
        if (gui == null)
            System.out.println("É necessário inicializar o mapa primeiro");

        return gui;
    }

    public synchronized static GUI init(PApplet p) {
        if (gui != null)
            System.err.println("Mapa já foi inicializado");

        gui = new GUI(p);
        return gui;
    }

    /**
     * Exibe a interface com informações do jogador.
     *
     * @param player o cavaleiro para obter as informações
     */
    public void display(TheKnight player) {
        p.fill(255);
        p.text("Health: " + player.getHealth(), 10, 20);
        p.text("FPS: " + PApplet.round(p.frameRate), 10, 50);
    }
}
