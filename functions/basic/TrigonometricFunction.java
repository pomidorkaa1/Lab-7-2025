package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {

    //тригонометрические функции определены на всей числовой прямой
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    //абстрактный метод для вычисления значения функции
    public abstract double getFunctionValue(double x);
}
