package Splines;

public class SplineFunctions {
    private double a;
    private double b;
    private double c;
    private double d;

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public void setD(double d) {
        this.d = d;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }

    @Override
    public String toString() {
        return this.getA() + " " + this.getB() + " " + this.getC() + " " + getD();
    }
}
