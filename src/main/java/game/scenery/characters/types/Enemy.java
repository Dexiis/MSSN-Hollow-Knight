package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.Eye;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Classe abstrata que define a estrutura base para todos os inimigos do jogo.
 * <p>
 * Gere estados, comportamentos de direção (steering behaviors), atributos genéticos (DNA),
 * sensores visuais e animações de sprites. Estende a entidade base do jogo.
 * </p>
 */
public abstract class Enemy extends Entity {
    protected State latestState;
    protected State state;

    protected KnightMovement currentDirection;
    protected KnightMovement latestDirection;

    protected DNA dna;
    private Eye eye;

    protected final PApplet p;

    protected int multValue = 1;

    /**
     * Constrói um novo inimigo na posição especificada.
     * <p>
     * Inicializa o contexto gráfico e define o estado inicial como inativo (IDLE).
     * A posição base é gerida pela superclasse.
     * </p>
     *
     * @param position o vetor de posição inicial da entidade
     * @param p        o contexto gráfico do Processing
     */
    protected Enemy(PVector position, PApplet p) {
        super(position);
        this.p = p;
        now = p.millis();

        I_FRAMES = 500;
        this.state = State.IDLE;
    }

    /**
     * Obtém os atributos genéticos (DNA) do inimigo.
     * <p>
     * O DNA contém os limites físicos da entidade, tais como a velocidade máxima
     * e a força máxima aplicável.
     * </p>
     *
     * @return o objeto DNA do inimigo
     */
    public DNA getDna() {
        return dna;
    }

    /**
     * Obtém o sensor visual (olho) associado a este inimigo.
     *
     * @return o objeto Eye atual
     */
    public Eye getEye() {
        return this.eye;
    }

    /**
     * Define o sensor visual para este inimigo.
     *
     * @param eye o novo objeto Eye a ser associado
     */
    public void setEye(Eye eye) {
        this.eye = eye;
    }

    /**
     * Aplica um comportamento de direção específico ao inimigo.
     * <p>
     * Atualiza o sensor visual, calcula a velocidade desejada ditada pelo comportamento
     * e executa o movimento correspondente.
     * </p>
     *
     * @param behaviour o comportamento a aplicar
     * @param dt        o intervalo de tempo para a atualização física
     */
    public void applyBehaviour(Behaviour behaviour, float dt) {
        if (this instanceof FalseKnight) System.out.println(eye);
        if (eye != null) eye.look();
        PVector vd = behaviour.getDesiredVelocity(this);
        move(dt, vd);
    }

    /**
     * Aplica dano à entidade de forma genérica.
     * <p>
     * Implementa um sistema de "invencibilidade temporária" (I-Frames). A vida só é reduzida
     * se tiver passado tempo suficiente desde o último golpe recebido.
     * </p>
     *
     */
    public void damage(PVector other) {
        if (this instanceof FalseKnight) super.damage();
        else if (now - hitTime > I_FRAMES) {
            health--;
            hitTime = now;

            int direction;
            if (other.x > this.position.x) direction = -1;
            else direction = 1;

            this.setVelocity(new PVector(50 * direction, 100));
        }
    }

    /**
     * Aplica o movimento baseado numa velocidade desejada e num intervalo de tempo.
     * <p>
     * Calcula a força de direção necessária (steering force) subtraindo a velocidade atual
     * à velocidade desejada e limitando o resultado pela força máxima definida no DNA.
     * </p>
     *
     * @param dt o intervalo de tempo para a atualização física
     * @param vd o vetor de velocidade desejada
     */
    public void move(float dt, PVector vd) {
        vd.normalize().mult(dna.getMaxSpeed());
        PVector fs = PVector.sub(vd, velocity);
        applyForce(fs.limit(dna.getMaxForce()));
        move(dt);
    }

    /**
     * Verifica e atualiza a direção visual (esquerda/direita) baseada na velocidade horizontal atual.
     * <p>
     * Se a direção mudar, reinicia a animação e coloca o estado como {@code TURNING}.
     * </p>
     */
    protected void directionChange(float IDLE_SPEED) {
        if (state == State.IDLE) {
            PVector vector = PVector.sub(getPosition(), this.getEye().getTarget().getPosition()).normalize();
            currentDirection = vector.x < 0 ? KnightMovement.RIGHT : KnightMovement.LEFT;
            if (currentDirection != latestDirection) {
                resetAnimation();
                state = State.TURNING;
                this.getDna().setMaxSpeed(IDLE_SPEED);
                this.getDna().setMaxForce(IDLE_SPEED);
            }
            latestDirection = currentDirection;
        }
    }

    protected void directionChange() {
        if (state == State.IDLE) {
            PVector vector = PVector.sub(getPosition(), this.getEye().getTarget().getPosition()).normalize();
            currentDirection = vector.x < 0 ? KnightMovement.RIGHT : KnightMovement.LEFT;
            if (currentDirection != latestDirection) {
                resetAnimation();
                state = State.TURNING;
            }
            latestDirection = currentDirection;
        }
    }

    /**
     * Carrega uma folha de sprites de um ficheiro e preenche a matriz de imagens fornecida.
     * <p>
     * Divide a imagem original em quadros individuais com base no tamanho de sprite definido
     * e armazena-os na matriz. Define também o sprite inicial.
     * </p>
     *
     * @param filename    o caminho para o ficheiro de imagem
     * @param p           o contexto gráfico do Processing
     * @param spriteArray a matriz onde os sprites recortados serão armazenados
     */
    protected void loadSpriteSheet(String filename, PApplet p, PImage[][] spriteArray) {
        // Enche o array de sprites iterativamente
        PImage sprites = p.loadImage(filename);
        for (int y = 0; y < SPRITE_COUNT; y++)
            for (int x = 0; x < SPRITE_COUNT; x++)
                spriteArray[x][y] = sprites.get(x * SPRITE_SIZE, y * SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);

        this.sprite = spriteArray[0][0];
    }
}