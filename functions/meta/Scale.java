package functions.meta;

import functions.Function;

public class Scale implements Function{
    private Function function; //исходная функция
    private double scaleX; //масштабирование по х
    private double scaleY; //масштабирование по у

    public Scale(Function func, double scaleX, double scaleY){
        if(func == null ) throw new IllegalArgumentException("Функция должна быть задана"); //проверка существвования функции
        this.function = func; //устанавливаем исходную функцию
        this.scaleX = scaleX; //устанавливаем масштабирование по х
        this.scaleY = scaleY; //устанавливаем масштабирование по у
    }

    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder()*scaleX;
    }

    public double getRightDomainBorder() {
        return function.getRightDomainBorder()*scaleY;
    }

    public double getFunctionValue(double x) {
        return function.getFunctionValue(x*scaleX)*scaleY; //масштабирование значение функции
    }
}
