package game.scenery.components.flock;

import game.core.SubPlot;
import game.scenery.characters.Movement;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.behaviours.Align;
import game.scenery.characters.types.enemies.attributes.behaviours.Cohesion;
import game.scenery.characters.types.enemies.attributes.behaviours.Separate;
import game.scenery.components.hitbox.Hitbox;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public class Flock extends Movement {
    private final int SPRITE_SIZE = 150;
    private final PImage sprite;

    private final DNA dna;
    private FlockEye eye;

    private final double[] window;

    private final ArrayList<Behaviour> behaviours = new ArrayList<>();

    public Flock(PVector position, SubPlot plt, PApplet p) {
        super(position);
        dna = new DNA(this);

        window = plt.getWindow();

        behaviours.add(new Align(1));
        behaviours.add(new Cohesion(1));
        behaviours.add(new Separate(1));

        PImage sprites = p.loadImage("images/SquitSprites.png");
        this.sprite = sprites.get(0, 0, SPRITE_SIZE, SPRITE_SIZE);
    }

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

    public FlockEye getEye() {
        return eye;
    }

    public void setEye(FlockEye eye) {
        this.eye = eye;
    }

    public DNA getDna() {
        return dna;
    }

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
     * Define manualmente a posição da entidade.
     * <p>
     * Sobrescreve o mét.odo da superclasse para sincronizar imediatamente a {@link Hitbox}
     * para o mesmo local.
     *
     * @param position O novo vetor de posição.
     */
    @Override
    public void setPosition(PVector position) {
        this.position = position;
    }

    public PVector getToroidalDistanceVector(PVector targetPosition) {
        PVector distance = PVector.sub(targetPosition, position);

        double worldWidth = window[1] - window[0];
        double worldHeight = window[3] - window[2];

        if (Math.abs(distance.x) > worldWidth / 2) {
            if (distance.x > 0)
                distance.x -= (float) worldWidth;
            else
                distance.x += (float) worldWidth;
        }

        if (Math.abs(distance.y) > worldHeight / 2) {
            if (distance.y > 0)
                distance.y -= (float) worldHeight;
            else
                distance.y += (float) worldHeight;
        }

        return distance;
    }

    /**
     * Atualiza a posição física da entidade baseada no tempo delta.
     * <p>
     * Sobrescreve o mét.odo da superclasse para garantir que a {@link Hitbox} acompanha
     * sempre a nova posição da entidade.
     *
     * @param dt O intervalo de tempo decorrido.
     */
    @Override
    public void move(float dt) {
        super.move(dt);
    }

    public void display(PApplet p, SubPlot plt) {
        float[] pp = plt.getPixelCoord(getPosition().x, getPosition().y);

        p.pushMatrix();
        p.pushStyle();

        p.translate(pp[0], pp[1]);
        p.scale(0.2f, 0.2f);

        p.tint(255, 128);
        p.image(this.sprite, -SPRITE_SIZE / 2f, -SPRITE_SIZE / 2f);

        p.popStyle();
        p.popMatrix();
    }
}