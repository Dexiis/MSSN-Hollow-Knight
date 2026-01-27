package game.core;

public class Complex {

	private double a, b;

	public Complex(double a, double b) {
		this.a = a;
		this.b = b;
	}

	public Complex(double[] ab) {
		this.a = ab[0];
		this.b = ab[1];
	}

	public Complex() {
		this.a = this.b = 0;
	}
	
	public double getA() {
		return a;
	}
	
	public double getB() {
		return b;
	}

	public Complex mult(Complex x) {
		double real = this.a * x.a - this.b * x.b;
		double imaginary = this.a * x.b + this.b * x.a;
		this.a = real;
		this.b = imaginary;
		return this;
	}

	public Complex add(Complex x) {
		this.a += x.a;
		this.b += x.b;
		return this;
	}

	public double norm() {
		return Math.sqrt(a * a + b * b);
	}

}