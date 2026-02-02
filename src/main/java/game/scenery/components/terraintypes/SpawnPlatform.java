package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.World;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Define uma plataforma especial responsável por iniciar o aparecimento do chefe.
 * <p>
 * Esta classe representa um tipo de terreno que reage à interação com o cavaleiro,
 * sendo removida do mundo quando ativada e desencadeando o início do confronto
 * com o chefe do nível.
 * </p>
 */
public class SpawnPlatform extends Terrain {

    /**
     * Cria uma nova plataforma de ativação do chefe.
     * <p>
     * Inicializa o terreno com a posição central e dimensões fornecidas,
     * utilizando o contexto gráfico do Processing para a sua gestão
     * e integração no cenário.
     * </p>
     *
     * @param center a posição central da plataforma no mundo do jogo
     * @param width  a largura total da plataforma
     * @param height a altura total da plataforma
     * @param p      o contexto gráfico do Processing
     */
    public SpawnPlatform(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);
    }

    /**
     * Trata a interação entre a plataforma e uma entidade.
     * <p>
     * Verifica se a entidade é o cavaleiro e se ocorre interseção
     * com a área da plataforma, removendo-a do cenário e iniciando
     * o aparecimento do chefe.
     * </p>
     *
     * @param entity a entidade que interage com a plataforma
     */
    @Override
    public void elaborateIntersects(Entity entity) {
        if (entity instanceof TheKnight) {
            if (super.intersected(entity.getHitbox())) {
                World.getInstance().removeTerrain(this);
                World.getInstance().spawnBoss();
            }
        }
    }

    /**
     * Desenha a plataforma no ecrã.
     * <p>
     * Esta implementação não apresenta qualquer representação visual,
     * sendo a plataforma utilizada apenas como elemento lógico de ativação
     * no mundo do jogo.
     * </p>
     *
     * @param p       o contexto gráfico do Processing
     * @param painter o objeto responsável pelo desenho das linhas
     * @param plt     o objeto SubPlot utilizado para conversão de coordenadas
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
    }
}