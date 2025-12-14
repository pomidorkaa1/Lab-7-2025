package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base; //основание логарифма

    //конструктор для основания
    public Log (double base){
        if (base <= 0 || base == 1) throw new IllegalArgumentException("Основание логарифма должно быть не равным 1 и положительным.");
        this.base = base;
    }

    public double getLeftDomainBorder() {
        return 0; //логарифм определен при x > 0
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY; //логарифм определен до бесконечности
    }

    //значение логарифма в точке х
    public double getFunctionValue(double x) {
        if (x<= 0){
            return Double.NaN; //логарифм определен только для положительных чисел
        }
        return Math.log(x) / Math.log(base); //вычисляем логарифм по формуле перехода к новому основанию
    }
}
