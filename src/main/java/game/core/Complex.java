package game.core;

/**
 * Representa um número complexo na forma z = a + bi.
 * <p>
 * Esta classe fornece operações matemáticas fundamentais para números complexos, incluindo adição, multiplicação
 * e cálculo da norma. É útil em cálculos físicos, rotações, algoritmos de fractais (como os Conjuntos de Julia)
 * e outras aplicações matemáticas que envolvem números complexos.
 * </p>
 */
public class Complex {
    private double a, b;

    /**
     * Constrói um novo número complexo com as partes real e imaginária especificadas.
     * <p>
     * Este construtor inicializa um objeto Complex com os valores fornecidos para a parte real (a) e a parte imaginária (b),
     * representando o número complexo na forma z = a + bi.
     * </p>
     *
     * @param a a parte real do número complexo
     * @param b a parte imaginária do número complexo
     */
    public Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Devolve a parte real do número complexo.
     * <p>
     * Esta operação devolve o valor da parte real (a) do número complexo, que representa a componente
     * não imaginária na forma z = a + bi.
     * </p>
     *
     * @return a parte real deste número complexo
     */
    public double getA() {
        return a;
    }

    /**
     * Devolve a parte imaginária do número complexo.
     * <p>
     * Esta operação devolve o valor da parte imaginária (b) do número complexo, que representa a componente
     * multiplicada por i na forma z = a + bi.
     * </p>
     *
     * @return a parte imaginária deste número complexo
     */
    public double getB() {
        return b;
    }

    /**
     * Adiciona outro número complexo a este número complexo.
     * <p>
     * Esta operação realiza a adição de números complexos, somando as partes reais e as partes imaginárias
     * independentemente: (a + bi) + (c + di) = (a+c) + (b+d)i. O resultado modifica o estado do objeto atual
     * e permite o encadeamento de operações.
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
     * Esta operação realiza a multiplicação de números complexos utilizando a fórmula: (a + bi) × (c + di) = (ac - bd) + (ad + bc)i.
     * O resultado modifica o estado do objeto atual, atualizando as partes real e imaginária com os novos valores calculados.
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
     * Calcula a norma deste número complexo.
     * <p>
     * A norma representa a magnitude ou módulo do número complexo, calculada como a distância do ponto (a, b)
     * à origem no plano complexo, utilizando a fórmula √(a² + b²). Este valor é sempre não negativo.
     * </p>
     *
     * @return a norma deste número complexo
     */
    public double norm() {
        return Math.sqrt(a * a + b * b);
    }
}