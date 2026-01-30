package game.core;

/**
 * Representa um número complexo na forma z = a + bi.
 * <p>
 * Esta classe fornece operações matemáticas fundamentais para números complexos,
 * incluindo adição, multiplicação e cálculo da norma. É útil para cálculos físicos,
 * rotações ou algoritmos de fractais (ex: Conjuntos de Julia).
 * </p>
 */
public class Complex {
    private double a, b;

    /**
     * Constrói um novo número complexo com a parte real e imaginária especificadas.
     *
     * @param a a parte real do número complexo
     * @param b a parte imaginária do número complexo
     */
    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Obtém a parte real do número complexo.
     *
     * @return a parte real (a) deste número complexo
     */
    public double getA() {
        return a;
    }

    /**
     * Obtém a parte imaginária do número complexo.
     *
     * @return a parte imaginária (b) deste número complexo
     */
    public double getB() {
        return b;
    }

    /**
     * Adiciona outro número complexo a este número complexo.
     * <p>
     * A operação realizada é: (a + bi) + (c + di) = (a+c) + (b+d)i
     * </p>
     * <p>
     * Modifica o estado do objeto atual, somando as partes reais
     * e as partes imaginárias independentemente.
     * </p>
     *
     * @param x o número complexo a adicionar a este número complexo
     * @return a própria instância atualizada, permitindo encadeamento de operações
     */
    public Complex add(Complex x) {
        this.a += x.a;
        this.b += x.b;
        return this;
    }

    /**
     * Multiplica este número complexo por outro número complexo.
     * <p>
     * A operação realizada é: (a + bi) × (c + di) = (ac - bd) + (ad + bc)i
     * </p>
     * <p>
     * Modifica o estado do objeto atual com o resultado da multiplicação.
     * </p>
     *
     * @param x o número complexo pelo qual multiplicar este número complexo
     */
    public void mult(Complex x) {
        double real = this.a * x.a - this.b * x.b;
        double imaginary = this.a * x.b + this.b * x.a;
        this.a = real;
        this.b = imaginary;
    }

    /**
     * Calcula a norma (módulo) deste número complexo.
     * <p>
     * A norma representa a distância do ponto (a, b) à origem no plano complexo
     * e é calculada como: √(a² + b²)
     * </p>
     *
     * @return a norma deste número complexo
     */
    public double norm() {
        return Math.sqrt(a * a + b * b);
    }
}