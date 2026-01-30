package game.scenery.components.flock;

import game.core.SubPlot;
import game.scenery.characters.Movement;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.components.flock.flockbehaviours.Align;
import game.scenery.components.flock.flockbehaviours.Cohesion;
import game.scenery.components.flock.flockbehaviours.Separate;
import game.scenery.components.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

/**
 * Representa um bando de entidades que se movem coletivamente.
 * <p>
 * Esta classe gere um conjunto de comportamentos coletivos como alinhamento,
 * coesão e separação, permitindo simular movimentos de rebanho ou cardume.
 * </p>
 */
public class Flock extends Movement {
    private final int SPRITE_SIZE = 150;
    private final PImage sprite;

    private final DNA dna;
    private FlockEye eye;

    private final double[] window;

    private final ArrayList<Behaviour> behaviours = new ArrayList<>();

    /**
     * Constrói um novo Flock na posição especificada.
     * <p>
     * Inicializa os atributos físicos, comportamentos coletivos (Align, Cohesion, Separate),
     * sprites e DNA específico para este bando.
     * </p>
     *
     * @param position a posição inicial do bando no mundo
     * @param plt      o subplot onde o bando se move
     * @param p        o contexto gráfico do Processing
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
     * Aplica os comportamentos coletivos ao bando.
     * <p>
     * Calcula a velocidade desejada baseada nos comportamentos ativos,
     * normaliza e aplica as forças necessárias para o movimento.
     * </p>
     *
     * @param dt o intervalo de tempo decorrido
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
     * Devolve o olho do bando.
     *
     * @return o olho associado ao bando
     */
    public FlockEye getEye() {
        return eye;
    }

    /**
     * Define o olho do bando.
     *
     * @param eye o olho a definir
     */
    public void setEye(FlockEye eye) {
        this.eye = eye;
    }

    /**
     * Devolve o DNA do bando.
     *
     * @return o DNA associado ao bando
     */
    public DNA getDna() {
        return dna;
    }

    /**
     * Move o bando com a velocidade desejada.
     * <p>
     * Normaliza a velocidade desejada, aplica forças e gere o movimento toroidal.
     * </p>
     *
     * @param dt o intervalo de tempo decorrido
     * @param vd a velocidade desejada
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
     * Define manualmente a posição do bando.
     *
     * @param position o novo vetor de posição
     */
    @Override
    public void setPosition(PVector position) {
        this.position = position;
    }

    /**
     * Calcula o vetor de distância toroidal para uma posição alvo.
     * <p>
     * Considera as bordas do mundo toroidal para calcular a distância mais curta.
     * </p>
     *
     * @param targetPosition a posição alvo
     * @return o vetor de distância toroidal
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
     * Atualiza a posição física do bando baseada no tempo delta.
     *
     * @param dt o intervalo de tempo decorrido
     */
    @Override
    public void move(float dt) {
        super.move(dt);
    }

    /**
     * Exibe o bando no ecrã.
     * <p>
     * Desenha o sprite do bando com transparência e escala reduzida.
     * </p>
     *
     * @param p   o contexto gráfico do Processing
     * @param plt o subplot onde desenhar
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
}