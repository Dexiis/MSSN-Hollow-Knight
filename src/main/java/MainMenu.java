import game.Game;
import processing.core.PApplet;
import processing.core.PImage;
import processing.sound.SoundFile;

/**
 * Representa o menu principal do jogo.
 * <p>
 * Esta classe é responsável por apresentar o ecrã inicial da aplicação,
 * incluindo o fundo, o título e o botão que permite iniciar o jogo.
 * Gere também as interações do utilizador com o rato.
 * </p>
 */
public class MainMenu extends PApplet {

    private final String play = "Start Game";
    private static PImage background;
    private static PImage title;

    /**
     * Define as configurações iniciais da janela da aplicação.
     * <p>
     * Especifica as dimensões da janela onde o menu será apresentado,
     * determinando a largura e a altura em píxeis.
     * </p>
     */
    public void settings() {
        size(1024, 900);
    }

    /**
     * Inicializa os recursos gráficos e sonoros do menu.
     * <p>
     * Carrega as imagens utilizadas no ecrã inicial, configura o alinhamento
     * e tamanho do texto, define o modo de desenho de retângulos e inicia
     * a música de fundo em ciclo contínuo.
     * </p>
     */
    public void setup() {
        SoundFile song = new SoundFile(this, "sounds/backgroundsong.wav");
        song.loop();
        song.amp(0.1f);

        textAlign(CENTER, CENTER);
        textSize(16);
        rectMode(CENTER);

        background = loadImage("images/Menu.png");
        title = loadImage("images/title.png");
    }

    /**
     * Desenha continuamente o conteúdo visual do menu.
     * <p>
     * Atualiza o fundo do ecrã, desenha a imagem de fundo e o título,
     * e apresenta o botão que permite iniciar o jogo.
     * </p>
     */
    public void draw() {
        background(200);
        pushMatrix();
        scale(0.52f);
        image(background, 0, 0);
        popMatrix();

        image(title, 0, 100);

        fill(0);

        drawButton(width / 2, 700, 300, 40, play, 1);
    }

    /**
     * Desenha um botão interativo no ecrã.
     * <p>
     * Verifica se o cursor do rato se encontra sobre a área do botão,
     * alterando a sua cor em conformidade, e desenha o retângulo
     * juntamente com o texto associado.
     * </p>
     *
     * @param x     coordenada horizontal do centro do botão
     * @param y     coordenada vertical do centro do botão
     * @param w     largura do botão
     * @param h     altura do botão
     * @param label texto apresentado no botão
     * @param id    identificador do botão
     */
    void drawButton(float x, float y, float w, float h, String label, int id) {
        if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y - h / 2 && mouseY < y + h / 2) fill(150, 200, 255);
        else fill(180);

        rect(x, y, w, h, 5);

        fill(0);
        text(label, x, y);
    }

    /**
     * Ponto de entrada da aplicação.
     * <p>
     * Inicia a execução do programa através da biblioteca Processing,
     * lançando o menu principal do jogo.
     * </p>
     *
     * @param args argumentos fornecidos pela linha de comandos
     */
    public static void main(String[] args) {
        PApplet.main(MainMenu.class.getName());
    }

    /**
     * Verifica se o cursor do rato se encontra dentro dos limites de um botão.
     * <p>
     * Compara as coordenadas atuais do rato com a área definida pelo botão,
     * permitindo determinar se houve uma interação válida.
     * </p>
     *
     * @param buttonY coordenada vertical do centro do botão
     * @param x       coordenada horizontal do centro do botão
     * @param w       largura do botão
     * @param h       altura do botão
     * @return verdadeiro se o cursor estiver dentro da área do botão,
     * falso caso contrário
     */
    private boolean checkButton(float buttonY, float x, float w, float h) {
        return mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > buttonY - h / 2 && mouseY < buttonY + h / 2;
    }

    /**
     * Processa a ação de clique do rato.
     * <p>
     * Verifica se o utilizador clicou sobre o botão de iniciar o jogo
     * e, em caso afirmativo, fecha o menu principal e lança o jogo.
     * </p>
     */
    public void mousePressed() {
        float x = width / 2;
        float w = 300;
        float h = 40;

        if (checkButton(700, x, w, h)) {
            surface.setVisible(false);
            PApplet.main(Game.class);
        }
    }
}