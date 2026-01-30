package game.scenery.characters.types.enemies;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.KnightMovement;
import game.scenery.characters.types.State;
import game.scenery.characters.types.enemies.attributes.enemyBehaviours.Attack;
import game.scenery.characters.types.enemies.attributes.enemyBehaviours.Wander;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Classe abstrata que representa um inimigo genérico (mob) no jogo.
 * <p>
 * Esta classe serve de base para todos os tipos de monstros, gerindo a sua máquina de estados interna
 * (Parado, A Vaguear, A Atacar, etc.), o carregamento e manipulação de sprites, a gestão de comportamentos
 * de Inteligência Artificial (IA) e a lógica de direção visual.
 * </p>
 */
public abstract class Mob extends Enemy {
    protected final Wander wanderBehaviour;
    protected final Attack attackBehaviour;
    protected float phiWander;

    protected static float IDLE_SPEED;

    /**
     * Constrói um novo Mob na posição especificada.
     * <p>
     * Inicializa o contexto gráfico, cria os comportamentos padrão (Wander e Attack),
     * define o número de sprites e os frames de invulnerabilidade.
     * </p>
     *
     * @param position a posição inicial do inimigo no mundo
     * @param p        o contexto gráfico do Processing
     */
    protected Mob(PVector position, PApplet p) {
        super(position, p);

        this.attackBehaviour = new Attack(1);
        this.wanderBehaviour = new Wander(1);

        SPRITE_COUNT = 8;
        I_FRAMES = 450;
    }

    /**
     * Obtém o ângulo atual de vaguear (wander angle).
     * <p>
     * Usado por comportamentos de movimento aleatório suave.
     * </p>
     *
     * @return o valor do ângulo em radianos
     */
    public float getPhiWander() {
        return phiWander;
    }

    /**
     * Define o ângulo de vaguear.
     *
     * @param newPhiWander o novo ângulo em radianos
     */
    public void setPhiWander(float newPhiWander) {
        this.phiWander = newPhiWander;
    }

    /**
     * Aplica dano ao inimigo e gere o recuo (knockback).
     * <p>
     * Verifica se os frames de invulnerabilidade expiraram antes de aplicar dano.
     * Quando atingido, o inimigo sofre recuo na direção oposta à origem do dano,
     * fica atordoado temporariamente e perde um ponto de vida.
     * </p>
     *
     * @param p     o contexto gráfico do Processing
     * @param other a posição da origem do dano
     */
    @Override
    public void damage(PApplet p, PVector other) {
        if (p.millis() - lastTimeHit > I_FRAMES) {
            int direction;
            if (other.x > this.position.x) direction = -1;
            else direction = 1;

            this.setVelocity(new PVector(50 * direction, 100));

            this.stunned = true;
            this.stunnedTimer = p.millis();

            health--;
            lastTimeHit = p.millis();
        }
    }

    /**
     * Gere a máquina de estados finita (FSM) do inimigo.
     * <p>
     * Verifica qual é o estado atual na variável {@code state} e invoca a função
     * abstrata ou concreta correspondente. Atualiza também o registo do último estado conhecido.
     * </p>
     */
    protected void stateMachine() {
        switch (this.state) {
            case State.IDLE:
                idling();
                break;
            case State.TURNING:
                turning();
                break;
            case State.STARTLED:
                startled();
                break;
            case State.ANTICIPATION:
                anticipating();
                break;
            case State.ATTACK:
                attacking();
                break;
            case State.DEATH:
                death();
                break;
        }
        latestState = state;
    }

    /**
     * Verifica e atualiza a direção visual (esquerda/direita) baseada na velocidade horizontal atual.
     * <p>
     * Se a direção mudar, reinicia a animação e coloca o estado como {@code TURNING}.
     * </p>
     */
    protected void directionChange() {
        currentDirection = this.getVelocity().x < 0 ? KnightMovement.LEFT : KnightMovement.RIGHT;
        if (currentDirection != latestDirection) {
            resetAnimation(p.millis());
            state = State.TURNING;
        }
        latestDirection = currentDirection;
    }

    /**
     * Define a lógica a ser executada quando o inimigo está no estado IDLE (Parado/Vaguear).
     * <p>
     * Deve ser implementado pelas subclasses específicas.
     * </p>
     */
    protected abstract void idling();

    /**
     * Define a lógica a ser executada quando o inimigo está a mudar de direção.
     * <p>
     * Deve ser implementado pelas subclasses específicas.
     * </p>
     */
    protected abstract void turning();

    /**
     * Define a lógica para quando o inimigo é surpreendido ou sobressaltado.
     * <p>
     * A implementação base é vazia, podendo ser sobrescrita se necessário.
     * </p>
     */
    protected void startled() {
    }

    /**
     * Define a lógica de antecipação antes de um ataque.
     * <p>
     * A implementação base é vazia, podendo ser sobrescrita se necessário.
     * </p>
     */
    protected void anticipating() {
    }

    /**
     * Define a lógica de execução do ataque.
     * <p>
     * Deve ser implementado pelas subclasses específicas.
     * </p>
     */
    protected abstract void attacking();

    /**
     * Define a lógica para a morte do inimigo.
     * <p>
     * A implementação base é vazia, podendo ser sobrescrita se necessário.
     * </p>
     */
    protected void death() {
    }
}