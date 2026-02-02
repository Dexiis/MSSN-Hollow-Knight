package game.scenery.components.terraintypes;

import game.core.SubPlot;
import game.scenery.World;
import game.scenery.characters.Entity;
import game.scenery.characters.types.TheKnight;
import game.scenery.characters.types.enemies.Mob;
import game.scenery.components.Terrain;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Define uma plataforma que provoca consequências fatais às entidades.
 * <p>
 * Esta classe especializa um terreno capaz de causar dano ao cavaleiro
 * ou remover inimigos do mundo sempre que ocorre contacto com a sua área.
 * </p>
 */
public class DeathPlatform extends Terrain {

    /**
     * Cria uma nova plataforma com comportamento letal.
     * <p>
     * A posição central e as dimensões são encaminhadas para a classe base,
     * permitindo definir corretamente a área de interação no cenário.
     * </p>
     *
     * @param center posição central da plataforma
     * @param width  largura total da plataforma
     * @param height altura total da plataforma
     * @param p      contexto gráfico do Processing
     */
    public DeathPlatform(PVector center, float width, float height, PApplet p) {
        super(center, width, height, p);
    }

    /**
     * Gere a interação entre o terreno e uma entidade.
     * <p>
     * É aplicado o comportamento específico desta plataforma e, de seguida,
     * é mantido o processamento definido na classe base.
     * </p>
     *
     * @param entity entidade que entra em contacto com o terreno
     */
    @Override
    public void elaborateIntersects(Entity entity) {
        this.intersected(entity);
        super.elaborateIntersects(entity);
    }

    /**
     * Aplica o efeito letal sobre a entidade em contacto.
     * <p>
     * Caso a entidade seja o cavaleiro, é aplicado dano e a sua posição é
     * reinicializada; no caso de inimigos, estes são removidos do mundo.
     * </p>
     *
     * @param entity entidade afetada pela interseção
     */
    public void intersected(Entity entity) {
        if (super.intersected(entity.getHitbox())) {
            if (entity instanceof TheKnight) {
                ((TheKnight) entity).playDamageSound();
                entity.damage();
                entity.setPosition(new PVector(50, 800));
                entity.setVelocity(new PVector(0, 0));
                entity.setAcceleration(new PVector(0, 0));
            } else World.getInstance().removeEnemy((Mob) entity);
        }
    }

    /**
     * Apresenta graficamente a plataforma no ecrã.
     * <p>
     * Esta implementação não realiza qualquer desenho, mantendo a plataforma
     * invisível enquanto continua ativa no sistema de colisões.
     * </p>
     *
     * @param p       contexto gráfico do Processing
     * @param painter objeto responsável pelo desenho de linhas
     * @param plt     sistema de conversão entre coordenadas do mundo e píxeis
     */
    @Override
    public void display(PApplet p, LinePainter painter, SubPlot plt) {
    }
}
