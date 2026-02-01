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

public class TrapDoor extends Terrain {

    private static PImage sprite;

    /**
     * Representa uma armadilha que remove-se ao ser tocada pelo cavaleiro.
     * <p>
     * Permite passagem para inimigos, mas desaparece para o cavaleiro, atuando como
     * um mecanismo de progressão no jogo.
     * </p>
     *
     * @param center a posição central
     * @param width a largura
     * @param height a altura
     * @param p o contexto gráfico
     */
    public TrapDoor(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);

        sprite = p.loadImage("images/trapdoor.png");
    }

    /**
     * Processa a interseção com uma entidade.
     * <p>
     * Remove a armadilha se for o cavaleiro, caso contrário processa normalmente.
     * </p>
     *
     * @param entity a entidade que interseta
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
     * Desenha a plataforma no ecrã.
     * <p>
     * Renderiza o sprite e delega o desenho da hitbox.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);
        // draw(painter, plt);

        p.image(sprite, pp[0] - getWidth() / 2f, pp[1] - getHeight() / 2f);
    }
}
