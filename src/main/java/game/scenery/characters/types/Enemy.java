package game.scenery.characters.types;

import game.scenery.characters.Entity;
import game.scenery.characters.types.enemies.FalseKnight;
import game.scenery.characters.types.enemies.attributes.Behaviour;
import game.scenery.characters.types.enemies.attributes.DNA;
import game.scenery.characters.types.enemies.attributes.Eye;
import game.scenery.characters.types.enemies.attributes.behaviours.Attack;
import game.scenery.characters.types.enemies.attributes.behaviours.Wander;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa um inimigo genérico no jogo.
 * <p>
 * Esta classe serve de base para todos os tipos de monstros, gerindo a sua máquina de estados interna
 * (Parado, A Vaguear, A Atacar, etc.), o carregamento e manipulação de sprites, a gestão de comportamentos
 * de Inteligência Artificial (IA) e a lógica de direção visual.
 */
public abstract class Enemy extends Entity {

    protected ArrayList<Behaviour> behaviours = new ArrayList<>();
    protected final Wander wanderBehaviour;
    protected final Attack attackBehaviour;
    protected float phiWander;
    protected DNA dna;
    private Eye eye;

    protected enum STATE {
        IDLE, TURNING, STARTLED, ANTICIPATION, ATTACK, DEATH
    }

    protected Direction currentDirection;
    protected Direction latestDirection;
    protected STATE latestState;
    protected int multValue = 1;
    protected STATE state;

    protected static float ATTACK_COOLDOWN;
    public static float ATTACK_SPEED;
    protected static float IDLE_SPEED;

    protected final PApplet p;

    /**
     * Construtor da classe Enemy.
     * <p>
     * Inicializa o contexto gráfico, cria o ADN (DNA) único para a instância, instancia
     * os comportamentos padrão (Wander e Attack) e define o estado inicial como IDLE.
     *
     * @param position A posição inicial do inimigo no mundo.
     * @param p        O contexto gráfico do Processing.
     */
    protected Enemy(PVector position, PApplet p) {
        super(position);

        this.p = p;
        this.attackBehaviour = new Attack(1);
        this.wanderBehaviour = new Wander(1);

        SPRITE_COUNT = 8;

        I_FRAMES = 450;

        this.state = STATE.IDLE;
    }

    /**
     * Obtém a lista de comportamentos ativos do inimigo.
     *
     * @return A lista de comportamentos.
     */
    public ArrayList<Behaviour> getBehaviours() {
        return this.behaviours;
    }

    /**
     * Obtém os atributos genéticos (DNA) da entidade.
     * O DNA contém limites físicos como velocidade máxima e força máxima.
     *
     * @return O objeto DNA.
     */
    public DNA getDNA() {
        return dna;
    }

    /**
     * Obtém o sensor visual (Olho) da entidade.
     *
     * @return O objeto Eye.
     */
    public Eye getEye() {
        return this.eye;
    }

    /**
     * Obtém o ângulo atual de "vaguear" (wander angle).
     * Usado por comportamentos de movimento aleatório suave.
     *
     * @return O valor do ângulo em radianos.
     */
    public float getPhiWander() {
        return phiWander;
    }

    /**
     * Define o sensor visual da entidade.
     *
     * @param eye O novo objeto Eye.
     */
    public void setEye(Eye eye) {
        this.eye = eye;
    }

    /**
     * Define o ângulo de "vaguear".
     *
     * @param newPhiWander O novo ângulo em radianos.
     */
    public void setPhiWander(float newPhiWander) {
        this.phiWander = newPhiWander;
    }

    /**
     * Aplica um comportamento de direção (steering behaviour) único à entidade.
     * <p>
     * O método ativa o sensor visual (se existir), calcula a velocidade desejada pelo comportamento
     * e aplica a força de movimento correspondente.
     *
     * @param behaviour O comportamento a aplicar.
     * @param dt        O intervalo de tempo para a atualização física.
     */
    public void applyBehaviour(Behaviour behaviour, float dt) {
        if (eye != null) eye.look();
        PVector vd = behaviour.getDesiredVelocity(this);
        move(dt, vd);
    }

    /**
     * Aplica uma lista de comportamentos combinados.
     * <p>
     * Calcula a soma das velocidades desejadas de todos os comportamentos fornecidos.
     *
     * @param behaviours A lista de comportamentos a processar.
     * @param dt         O intervalo de tempo para a atualização física.
     */
    public void applyBehaviours(List<Behaviour> behaviours, float dt) {
        if (eye != null) eye.look();
        PVector vd = new PVector();
//      float sumWeights = 0;

//      for (Behaviour behaviour : behaviours)                        // ISTO NÃO DEVE DE SER NECESSÁRIO MAS DEIXAR POR ENQUANTO
//          sumWeights += behaviour.getWeight();

        for (Behaviour behaviour : behaviours) {
            PVector vdd = behaviour.getDesiredVelocity(this);
//          vdd.mult(behaviour.getWeight() / sumWeights);             O MESMO PARA ISTO
//          vdd.mult(behaviour.getWeight());
            vd.add(vdd);
        }

        move(dt, vd);
    }

    @Override
    public void damage(PApplet p, PVector other) {
        if (p.millis() - lastTimeHit > I_FRAMES) {
            if (!(this instanceof FalseKnight)) {
                int direction;
                if (other.x > this.position.x) direction = -1;
                else direction = 1;

                this.setVelocity(new PVector(50 * direction, 100));

                this.stunned = true;
                this.stunnedTimer = p.millis();
            }
            health--;
            lastTimeHit = p.millis();
        }
    }

    /**
     * Executa o movimento baseado numa velocidade desejada (Steering Force).
     * <p>
     * Implementa a fórmula de Reynolds: Força = Velocidade Desejada - Velocidade Atual.
     * A força resultante é limitada pela força máxima definida no DNA da entidade.
     *
     * @param dt O intervalo de tempo.
     * @param vd O vetor de velocidade desejada (Desired Velocity).
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
     */
    protected void directionChange() {
        currentDirection = this.getVelocity().x < 0 ? Direction.LEFT : Direction.RIGHT;
        if (currentDirection != latestDirection) {
            resetAnimation(p.millis());
            state = STATE.TURNING;
        }
        latestDirection = currentDirection;
    }

    /**
     * Carrega uma folha de sprites (SpriteSheet) a partir de um ficheiro e divide-a numa matriz de imagens.
     * <p>
     * Preenche a matriz {@code spriteArray} cortando a imagem original em quadrados de tamanho {@code SPRITE_SIZE}.
     *
     * @param filename    O caminho para o ficheiro de imagem.
     * @param p           O contexto do Processing.
     * @param spriteArray A matriz onde os sprites recortados serão armazenados.
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
     * Reinicia os contadores de animação (índice e tempo).
     *
     * @param now O tempo atual em milissegundos.
     */
    protected void resetAnimation(int now) {
        this.spriteIndex = 0;
        this.spriteTime = now;
    }

    /**
     * Gere a máquina de estados finita (FSM) do inimigo.
     * <p>
     * Verifica qual é o estado atual na variável {@code state} e invoca o mét.odo
     * abstrato ou concreto correspondente. Atualiza também o registo do último estado conhecido.
     */
    protected void stateMachine() {
        switch (state) {
            case STATE.IDLE:
                idling();
                break;
            case STATE.TURNING:
                turning();
                break;
            case STATE.STARTLED:
                startled();
                break;
            case STATE.ANTICIPATION:
                anticipating();
                break;
            case STATE.ATTACK:
                attacking();
                break;
            case STATE.DEATH:
                death();
                break;
        }
        latestState = state;
    }

    /**
     * Define a lógica a ser executada quando o inimigo está no estado IDLE (Parado/Vaguear).
     * Deve ser implementado pelas subclasses específicas.
     */
    protected abstract void idling();

    /**
     * Define a lógica a ser executada quando o inimigo está a mudar de direção.
     * Deve ser implementado pelas subclasses específicas.
     */
    protected abstract void turning();

    /**
     * Define a lógica para quando o inimigo é surpreendido.
     * A implementação base é vazia, podendo ser sobrescrita se necessário.
     */
    protected void startled() {
    }

    /**
     * Define a lógica de antecipação antes de um ataque.
     * A implementação base é vazia, podendo ser sobrescrita se necessário.
     */
    protected void anticipating() {
    }

    /**
     * Define a lógica de execução do ataque.
     * Deve ser implementado pelas subclasses específicas.
     */
    protected abstract void attacking();

    /**
     * Define a lógica para a morte do inimigo.
     * Deve ser implementado pelas subclasses específicas.
     */
    protected abstract void death();
}