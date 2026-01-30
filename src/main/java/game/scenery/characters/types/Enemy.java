package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.Eye;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

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

    protected ArrayList<Behaviour> behaviours = new ArrayList<>();

    protected DNA dna;
    private Eye eye;

    protected KnightMovement currentDirection;
    protected KnightMovement latestDirection;
    protected int multValue = 1;

    public static float ATTACK_COOLDOWN;
    public static float ATTACK_SPEED;

    protected final PApplet p;

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

        this.state = State.IDLE;
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
     * Devolve a lista de comportamentos atualmente atribuídos ao inimigo.
     *
     * @return uma lista contendo os objetos Behaviour ativos
     */
    public ArrayList<Behaviour> getBehaviours() {
        return this.behaviours;
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
        if (eye != null) eye.look();
        PVector vd = behaviour.getDesiredVelocity(this);
        move(dt, vd);
    }

    /**
     * Aplica uma lista de comportamentos de direção ao inimigo.
     * <p>
     * Soma as velocidades desejadas de todos os comportamentos fornecidos na lista
     * e executa o movimento resultante dessa combinação.
     * </p>
     *
     * @param behaviours a lista de comportamentos a processar
     * @param dt         o intervalo de tempo para a atualização física
     */
    public void applyBehaviours(List<Behaviour> behaviours, float dt) {
        if (eye != null) eye.look();
        PVector vd = new PVector();

        for (Behaviour behaviour : behaviours) {
            PVector vdd = behaviour.getDesiredVelocity(this);
            vd.add(vdd);
        }

        move(dt, vd);
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

    /**
     * Aplica dano ao inimigo se o período de invencibilidade tiver passado.
     * <p>
     * Reduz a vida da entidade e atualiza o registo temporal do último golpe sofrido
     * para gerir os "frames de invencibilidade" (I_FRAMES).
     * </p>
     *
     * @param p     o contexto gráfico do Processing para acesso ao tempo
     * @param other o vetor de posição da fonte do dano
     */
    public void damage(PApplet p, PVector other) {
        if (p.millis() - lastTimeHit > I_FRAMES) {
            health--;
            lastTimeHit = p.millis();
        }
    }

    /**
     * Reinicia o ciclo de animação do sprite.
     * <p>
     * Coloca o índice do sprite a zero e define o tempo de referência da animação
     * para o valor atual fornecido.
     * </p>
     *
     * @param now o tempo atual em milissegundos
     */
    protected void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }
}