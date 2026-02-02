package game.scenery.components.flock;

import game.core.SubPlot;
import game.scenery.characters.Movement;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.components.flock.flockbehaviours.Align;
import game.scenery.components.flock.flockbehaviours.Cohesion;
import game.scenery.components.flock.flockbehaviours.Separate;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Representa uma entidade pertencente a um sistema de bando.
 * <p>
 * Esta classe integra comportamentos coletivos como alinhamento, coesão
 * e separação, permitindo simular deslocações coordenadas em ambientes
 * toroidais com controlo físico e genético do movimento.
 * </p>
 */
public class Flock extends Movement {
    private final ArrayList<Behaviour> behaviours = new ArrayList<>();
    private final DNA dna;
    private FlockEye eye;
    private final PImage sprite;
    private final double[] window;
    private final int SPRITE_SIZE = 150;

    /**
     * Cria uma nova entidade de bando numa posição específica.
     * <p>
     * Inicializa os atributos físicos herdados, associa o ADN responsável
     * pelos limites de movimento, configura os comportamentos coletivos
     * base e prepara o sprite utilizado na representação visual.
     * </p>
     *
     * @param position posição inicial no mundo
     * @param plt      subplot que define os limites do espaço toroidal
     * @param p        contexto gráfico do Processing
     */
    public Flock(PVector position, SubPlot plt, PApplet p) {
        super(position);
        dna = new DNA(this);

        window = plt.getWindow();

        behaviours.add(new Align(1));
        behaviours.add(new Cohesion(1));
        behaviours.add(new Separate(1));

        PImage sprites = p.loadImage("images/SquitSprites.png");

        this.sprite = sprites.get(0, 0, SPRITE_SIZE, SPRITE_SIZE);
        sprite.filter(PConstants.BLUR, 5);
    }

    /**
     * Devolve a informação genética associada.
     * <p>
     * O ADN define limites como velocidade máxima e força máxima
     * aplicável durante o movimento.
     * </p>
     *
     * @return instância de ADN associada
     */
    public DNA getDna() {
        return dna;
    }

    /**
     * Devolve o sistema de perceção do bando.
     * <p>
     * O olho é responsável por identificar entidades vizinhas
     * em diferentes alcances de visão.
     * </p>
     *
     * @return instância do olho do bando
     */
    public FlockEye getEye() {
        return eye;
    }

    /**
     * Associa um sistema de perceção à entidade.
     * <p>
     * Este componente permite analisar o espaço envolvente
     * e recolher informação sobre entidades próximas.
     * </p>
     *
     * @param eye sistema de perceção a associar
     */
    public void setEye(FlockEye eye) {
        this.eye = eye;
    }

    /**
     * Atualiza diretamente a posição no espaço.
     * <p>
     * Esta atribuição ignora cálculos físicos intermédios,
     * sendo utilizada para reposicionamentos imediatos.
     * </p>
     *
     * @param position novo vetor de posição
     */
    @Override
    public void setPosition(PVector position) {
        this.position = position;
    }

    /**
     * Aplica os comportamentos coletivos ativos.
     * <p>
     * Calcula a velocidade desejada resultante da combinação
     * ponderada dos comportamentos disponíveis e aplica
     * as forças necessárias para ajustar o movimento.
     * </p>
     *
     * @param dt intervalo de tempo decorrido
     */
    public void applyBehaviours(float dt) {
        if (eye != null) eye.look();
        PVector vd = new PVector();
        float sumWeights = 0;
        for (Behaviour behaviour : behaviours)
            sumWeights += behaviour.getWeight();

        for (Behaviour behaviour : behaviours) {
            PVector vdd = behaviour.getDesiredVelocity(this);
            vdd.mult(behaviour.getWeight() / sumWeights);
            vd.add(vdd);
        }
        this.move(dt, vd);
    }

    /**
     * Desenha a entidade de bando no ecrã.
     * <p>
     * O sprite é apresentado com escala reduzida e transparência,
     * sendo convertido das coordenadas do mundo para píxeis.
     * </p>
     *
     * @param p   contexto gráfico do Processing
     * @param plt subplot onde ocorre o desenho
     */
    public void display(PApplet p, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.pushMatrix();
        p.pushStyle();

        p.translate(pp[0], pp[1]);
        p.scale(0.2f, 0.2f);

        p.tint(255, 100);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f);

        p.popStyle();
        p.popMatrix();
    }

    /**
     * Calcula o vetor de distância considerando um mundo toroidal.
     * <p>
     * O cálculo garante que é escolhida a menor distância possível,
     * tendo em conta a continuidade das bordas do espaço.
     * </p>
     *
     * @param targetPosition posição alvo
     * @return vetor de distância toroidal
     */
    public PVector getToroidalDistanceVector(PVector targetPosition) {
        PVector distance = PVector.sub(targetPosition, position);

        double worldWidth = window[1] - window[0];
        double worldHeight = window[3] - window[2];

        if (Math.abs(distance.x) > worldWidth / 2) {
            if (distance.x > 0) distance.x -= (float) worldWidth;
            else distance.x += (float) worldWidth;
        }

        if (Math.abs(distance.y) > worldHeight / 2) {
            if (distance.y > 0) distance.y -= (float) worldHeight;
            else distance.y += (float) worldHeight;
        }

        return distance;
    }

    /**
     * Atualiza o movimento com base numa velocidade pretendida.
     * <p>
     * A velocidade desejada é normalizada, limitada pelos valores
     * genéticos e aplicada respeitando a dinâmica do espaço toroidal.
     * </p>
     *
     * @param dt intervalo de tempo decorrido
     * @param vd vetor de velocidade pretendida
     */
    public void move(float dt, PVector vd) {
        vd.normalize().mult(dna.getMaxSpeed());
        PVector fs = PVector.sub(vd, velocity);
        applyForce(fs.limit(dna.getMaxForce()));

        super.move(dt);

        if (position.x < window[0]) position.x += (float) (window[1] - window[0]);
        if (position.y < window[2]) position.y += (float) (window[3] - window[2]);
        if (position.x >= window[1]) position.x -= (float) (window[1] - window[0]);
        if (position.y >= window[3]) position.y -= (float) (window[3] - window[2]);
    }

    /**
     * Atualiza a posição física com base no tempo decorrido.
     * <p>
     * Executa apenas o cálculo físico herdado, sem aplicação
     * direta de comportamentos coletivos.
     * </p>
     *
     * @param dt intervalo de tempo decorrido
     */
    @Override
    public void move(float dt) {
        super.move(dt);
    }
}