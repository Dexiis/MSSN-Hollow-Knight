package game.scenery.characters;

import game.core.SubPlot;
import game.scenery.components.hitbox.LinePainter;
import processing.core.PApplet;

/**
 * Interface que define o contrato para qualquer entidade ou componente que possa ser visualizado graficamente no jogo.
 * <p>
 * Garante que os objetos implementadores possuem um método padronizado para se desenharem no ecrã,
 * recebendo as ferramentas necessárias para renderização e conversão de coordenadas.
 */
public interface IVisualizable {

    /**
     * Renderiza o objeto no ecrã.
     *
     * @param p       O contexto gráfico do Processing (PApplet) utilizado para operações de desenho diretas.
     * @param painter O pintor de linhas abstrato, usado para desenhar formas geométricas independentes da implementação gráfica.
     * @param plt     O objeto SubPlot responsável pela conversão entre coordenadas do mundo e coordenadas de pixel.
     */
    void display(PApplet p, LinePainter painter, SubPlot plt);
}