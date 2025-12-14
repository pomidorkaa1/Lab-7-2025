package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function function; //исходная функйция
    private double power; //степень

    public Power (Function func, double pow){
        if(func == null ) throw new IllegalArgumentException("Функция должна быть задана"); //проверка существования фукнкции
        this.power = pow;
        this.function = func;
    }

    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder();
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder();
    }

    public double getFunctionValue(double x){
        return Math.pow( function.getFunctionValue(x), power); //возведение в степень
    }
}
