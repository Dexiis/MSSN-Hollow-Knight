package game.scenery.characters.types.enemies;

import game.scenery.characters.types.Enemy;
import game.scenery.characters.types.State;
import game.scenery.characters.types.enemies.attributes.enemyBehaviours.Attack;
import game.scenery.characters.types.enemies.attributes.enemyBehaviours.Seek;
import game.scenery.characters.types.enemies.attributes.enemyBehaviours.Wander;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Define a estrutura base para inimigos genéricos.
 * <p>
 * Esta classe abstrata estabelece os fundamentos para a criação de diferentes tipos de monstros,
 * gerindo as máquinas de estados, comportamentos de inteligência artificial e propriedades
 * de animação comuns.
 * </p>
 */
public abstract class Mob extends Enemy {
    protected final Attack attackBehaviour;
    protected final Wander wanderBehaviour;
    protected final Seek seekBehaviour;

    protected float phiWander;

    protected float IDLE_SPEED;
    protected float ATTACK_SPEED_BOOST;

    /**
     * Inicializa uma nova instância de um inimigo genérico.
     * <p>
     * Configura o contexto gráfico, instancia os comportamentos padrão de movimento e ataque,
     * e define as configurações iniciais de sprites e frames de invulnerabilidade.
     * </p>
     *
     * @param position a posição inicial do inimigo no mundo.
     * @param p        o contexto gráfico do Processing.
     */
    protected Mob(PVector position, PApplet p) {
        super(position, p);

        this.seekBehaviour = new Seek(1);
        this.attackBehaviour = new Attack(1);
        this.wanderBehaviour = new Wander(1);

        SPRITE_COUNT = 8;
    }

    /**
     * Obtém o ângulo atual de desvio para o movimento errante.
     * <p>
     * Devolve o valor angular utilizado pelos algoritmos de navegação para calcular
     * trajetórias suaves e aleatórias.
     * </p>
     *
     * @return o ângulo em radianos.
     */
    public float getPhiWander() {
        return phiWander;
    }

    /**
     * Atualiza o ângulo de desvio do movimento.
     * <p>
     * Define um novo valor para a orientação angular, influenciando a direção futura
     * da entidade durante o comportamento de vaguear.
     * </p>
     *
     * @param newPhiWander o novo ângulo em radianos.
     */
    public void setPhiWander(float newPhiWander) {
        this.phiWander = newPhiWander;
    }

    /**
     * Coordena a execução da máquina de estados finita.
     * <p>
     * Avalia a condição atual da entidade e delega o processamento para a rotina
     * específica correspondente, garantindo a transição fluida entre comportamentos
     * como inatividade, ataque ou morte.
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
     * Define o comportamento durante a inatividade ou patrulha.
     * <p>
     * Especifica as ações que a entidade deve realizar quando não está envolvida em
     * combate ou transições. Deve ser implementada pelas subclasses.
     * </p>
     */
    protected abstract void idling();

    /**
     * Estabelece a lógica para a mudança de direção.
     * <p>
     * Implementa as animações e ajustes físicos necessários quando a entidade inverte
     * o seu sentido de movimento. Deve ser implementada pelas subclasses.
     * </p>
     */
    protected abstract void turning();

    /**
     * Reage a eventos de surpresa ou deteção súbita.
     * <p>
     * Proporciona um gancho para comportamentos de reação imediata ao avistar o jogador.
     * A implementação base é inócua, permitindo redefinição nas subclasses se necessário.
     * </p>
     */
    protected void startled() {
    }

    /**
     * Prepara a execução de uma ofensiva.
     * <p>
     * Descreve os sinais visuais ou lógicos que antecedem um ataque.
     * Deve ser implementada pelas subclasses.
     * </p>
     */
    protected abstract void anticipating();

    /**
     * Executa a lógica de ataque ativa.
     * <p>
     * Controla a fase ofensiva, incluindo a aplicação de dano e movimento.
     * Deve ser implementada pelas subclasses.
     * </p>
     */
    protected abstract void attacking();

    /**
     * Processa a sequência de eliminação da entidade.
     * <p>
     * Define as animações e procedimentos finais quando a entidade perde a vida.
     * Deve ser implementada pelas subclasses.
     * </p>
     */
    protected abstract void death();
}