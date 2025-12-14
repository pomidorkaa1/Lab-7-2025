package functions.basic;

import functions.Function;

public class Exp implements Function {

    //левая граница области определения
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    //правая граница области определения
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    //вычисление значения экспоненты в точке х
    public double getFunctionValue(double x) {
        return Math.exp(x);
    }
}
