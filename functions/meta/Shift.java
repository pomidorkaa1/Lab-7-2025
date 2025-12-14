package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function function; //исходная функция
    private double shiftX; //сдвиг по х
    private double shiftY; //сдвиг по у

    public Shift(Function func, double shiftX, double shiftY) throws IllegalArgumentException{
        if(func == null )
            throw new IllegalArgumentException("Функция должна быть задана"); //проверка существования функции
        this.function = func;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder(){
        return function.getLeftDomainBorder() + shiftX; //сдвиг левой границы
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder() + shiftX; //сдвиг правой границы
    }

    public double getFunctionValue(double x){
        return function.getFunctionValue(x+ shiftX)+ shiftY; // сдвигаем значение функции
    }
}
