package game.core;

/**
 * Representa um número complexo na forma $z = a + bi$.
 * <p>
 * Esta classe fornece operações matemáticas fundamentais para números complexos,
 * úteis para cálculos físicos, rotações ou algoritmos de fractais (ex: Julia Sets).
 */
public class Complex {
    private double a, b;

    /**
     * Construtor de um número complexo.
     *
     * @param a A parte real.
     * @param b A parte imaginária.
     */
    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Obtém a parte real do número complexo.
     *
     * @return O valor de a.
     */
    public double getA() {
        return a;
    }

    /**
     * Obtém a parte imaginária do número complexo.
     *
     * @return O valor de b.
     */
    public double getB() {
        return b;
    }

    /**
     * Adiciona outro número complexo a este ($z1 + z2$).
     * <p>
     * Soma as partes reais e as partes imaginárias independentemente.
     *
     * @param x O outro número complexo a somar.
     * @return A própria instância atualizada (permite encadeamento).
     */
    public Complex add(Complex x) {
        this.a += x.a;
        this.b += x.b;
        return this;
    }

    /**
     * Realiza a multiplicação deste número complexo por outro ($z1 * z2$).
     * <p>
     * A fórmula utilizada é: $(a + bi)(c + di) = (ac - bd) + (ad + bc)i$.
     * O estado deste objeto é atualizado com o resultado.
     *
     * @param x O outro número complexo a multiplicar.
     */
    public void mult(Complex x) {
        double real = this.a * x.a - this.b * x.b;
        double imaginary = this.a * x.b + this.b * x.a;
        this.a = real;
        this.b = imaginary;
    }

    /**
     * Calcula a norma (ou módulo) do número complexo.
     * <p>
     * Representa a distância do ponto (a,b) à origem no plano complexo: $\sqrt{a^2 + b^2}$.
     *
     * @return O valor da norma.
     */
    public double norm() {
        return Math.sqrt(a * a + b * b);
    }
}