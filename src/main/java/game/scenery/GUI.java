package game.scenery;

import game.scenery.characters.types.TheKnight;
import processing.core.PApplet;

/**
 * Gere a interface gráfica do utilizador do jogo.
 * <p>
 * Esta classe é responsável por apresentar informações visuais relevantes
 * ao jogador, como o estado do cavaleiro e dados de desempenho, seguindo
 * o padrão singleton para garantir uma única instância ativa.
 * </p>
 */
public class GUI {
    private static GUI gui = null;

    private final PApplet p;

    /**
     * Cria e inicializa a interface gráfica do utilizador.
     * <p>
     * Guarda o contexto gráfico do Processing, que será utilizado
     * para desenhar todos os elementos visuais da interface.
     * </p>
     *
     * @param p o contexto gráfico do Processing
     */
    private GUI(PApplet p) {
        this.p = p;
    }

    /**
     * Devolve a instância atual da interface gráfica.
     * <p>
     * Permite aceder à instância única previamente inicializada,
     * apresentando uma mensagem de aviso caso ainda não exista.
     * </p>
     *
     * @return a instância única de GUI
     */
    public static GUI getInstance() {
        if (gui == null) System.out.println("É necessário inicializar o mapa primeiro");

        return gui;
    }

    /**
     * Inicializa a instância única da interface gráfica.
     * <p>
     * Cria a instância de GUI caso ainda não tenha sido criada,
     * garantindo sincronização para evitar múltiplas inicializações.
     * </p>
     *
     * @param p o contexto gráfico do Processing
     * @return a instância criada de GUI
     */
    public synchronized static GUI init(PApplet p) {
        if (gui != null) System.err.println("Mapa já foi inicializado");

        gui = new GUI(p);
        return gui;
    }

    /**
     * Exibe no ecrã as informações do jogador.
     * <p>
     * Desenha elementos de texto com dados relevantes do cavaleiro,
     * como a vida atual e a taxa de fotogramas, utilizando o contexto
     * gráfico do Processing.
     * </p>
     *
     * @param player o cavaleiro de onde são obtidas as informações a apresentar
     */
    public void display(TheKnight player) {
        p.fill(255);
        p.text("Health: " + player.getHealth(), 10, 20);
        p.text("FPS: " + PApplet.round(p.frameRate), 10, 50);
    }
}