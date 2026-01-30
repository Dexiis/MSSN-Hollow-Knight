import game.Game;
import processing.core.PApplet;

/**
 * Menu principal do jogo.
 * <p>
 * Exibe um botão para iniciar o jogo e processa interações do mouse.
 * </p>
 */
public class MainMenu extends PApplet {

    private final String play = "Start Playing";

    /**
     * Define as configurações iniciais da janela.
     * <p>
     * Define a resolução da janela para 360x300 pixels.
     * </p>
     */
    public void settings() {
        size(360, 300);
    }

    /**
     * Inicializa o ambiente de desenho.
     * <p>
     * Configura o alinhamento do texto, tamanho da fonte e modo de retângulo.
     * </p>
     */
    public void setup() {
        textAlign(CENTER, CENTER);
        textSize(16);
        rectMode(CENTER);
    }

    /**
     * Desenha o menu na tela.
     * <p>
     * Define o fundo cinzento, cor do preenchimento e desenha o botão de jogar.
     * </p>
     */
    public void draw() {
        background(200);

        fill(0);

        drawButton(width / 2, 80, 300, 40, play, 1);
    }

    /**
     * Desenha um botão na tela.
     * <p>
     * Verifica se o mouse está sobre o botão para mudar a cor, desenha o retângulo e o texto.
     * </p>
     */
    void drawButton(float x, float y, float w, float h, String label, int id) {
        if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y - h / 2 && mouseY < y + h / 2) fill(150, 200, 255);
        else fill(180);

        rect(x, y, w, h, 5);

        fill(0);
        text(label, x, y);
    }

    /**
     * Inicia a aplicação.
     * <p>
     * Inicia o menu principal do jogo através do Processing.
     * </p>
     *
     * @param args argumentos da linha de comandos
     */
    public static void main(String[] args) {
        PApplet.main(Game.class.getName());
    }

    /**
     * Verifica se o clique foi no botão.
     * <p>
     * Compara as coordenadas do mouse com os limites do botão.
     * </p>
     */
    private boolean checkButton(float buttonY, float x, float w, float h) {
        return mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > buttonY - h / 2 && mouseY < buttonY + h / 2;
    }

    /**
     * Processa cliques do mouse para iniciar o jogo.
     * <p>
     * Verifica se o clique foi no botão de jogar e inicia o jogo principal.
     * </p>
     */
    public void mousePressed() {
        float x = width / 2;
        float w = 300;
        float h = 40;

        if (checkButton(80, x, w, h)) {
            surface.setVisible(false);
            PApplet.main(Game.class);
        }
    }
}