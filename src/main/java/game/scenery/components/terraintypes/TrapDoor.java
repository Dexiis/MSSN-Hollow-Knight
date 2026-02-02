package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.World;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Representa um tipo de terreno que funciona como uma porta de armadilha.
 * <p>
 * Esta classe define um elemento do cenário que desaparece quando é tocado
 * pelo cavaleiro, permitindo controlar a progressão do jogo, enquanto continua
 * a comportar-se como terreno sólido para outras entidades.
 * </p>
 */
public class TrapDoor extends Terrain {

    private static PImage sprite;

    /**
     * Cria uma nova porta de armadilha com posição e dimensões definidas.
     * <p>
     * Inicializa o terreno com base na posição central, largura e altura
     * fornecidas, utilizando o contexto gráfico do Processing e carregando
     * o sprite associado à armadilha.
     * </p>
     *
     * @param center a posição central da armadilha no mundo do jogo
     * @param width  a largura total da armadilha
     * @param height a altura total da armadilha
     * @param p      o contexto gráfico do Processing
     */
    public TrapDoor(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/trapdoor.png");
    }

    /**
     * Gere a interação entre a armadilha e uma entidade.
     * <p>
     * Verifica se a entidade corresponde ao cavaleiro e, em caso de interseção,
     * remove a armadilha do cenário e adiciona novos elementos de terreno ao
     * mundo do jogo; para outras entidades, o comportamento padrão é mantido.
     * </p>
     *
     * @param entity a entidade que entra em contacto com a armadilha
     */
    @Override
    public void elaborateIntersects(Entity entity) {
        if (entity instanceof TheKnight) {
            if (super.intersected(entity.getHitbox())) {
                World.getInstance().removeTerrain(this);
                World.getInstance().addTerrain(new Floor(new PVector(6000, -800), 2500, 200, p));
                World.getInstance().addBlackTerrain(new BlackTerrain(new PVector(6000, -1400), 2500, 1000, p));
                World.getInstance().addTerrain(new Wall(new PVector(4250, 0), 1000, 1400, p, Wall.SIDE.RIGHT));
                World.getInstance().addBlackTerrain(new BlackTerrain(new PVector(4250, -1200), 1000, 2000, p));
                World.getInstance().addTerrain(new Wall(new PVector(7750, 0), 1000, 1400, p, Wall.SIDE.LEFT));
                World.getInstance().addBlackTerrain(new BlackTerrain(new PVector(7750, -1200), 1000, 2000, p));
            }
        } else super.elaborateIntersects(entity);
    }

    /**
     * Desenha a porta de armadilha no ecrã.
     * <p>
     * Converte as coordenadas do mundo para píxeis e renderiza o sprite
     * correspondente à armadilha na posição adequada.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot utilizado para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.image(sprite, pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f);
    }
}
