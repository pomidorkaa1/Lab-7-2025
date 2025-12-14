package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function Function_1;//первая функция для сложения
    private Function Function_2;//вторая функция для сложения

    public Sum (Function firstFunc, Function secondFunc){
        if(firstFunc == null || secondFunc == null) throw new IllegalArgumentException("Функция должна быть задана"); //проверка на существование функции
        this.Function_1 = firstFunc; //установка первой функции
        this.Function_2 = secondFunc;//установка второй функции
    }

    public double getLeftDomainBorder() {
        return Math.max(Function_1.getLeftDomainBorder(), Function_2.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return Math.max(Function_1.getRightDomainBorder(), Function_2.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        return Function_1.getFunctionValue(x) + Function_2.getFunctionValue(x); //сумма функций
    }

}
