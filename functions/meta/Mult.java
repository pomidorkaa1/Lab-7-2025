package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function Function_1;//первая функция для умноженния
    private Function Function_2; //вторая функция для умноженния

    public Mult (Function firstFunc, Function secondFunc){
        if(firstFunc == null || secondFunc == null) throw new IllegalArgumentException("Функция должна быть задана");//проверка существования функции
        this.Function_1 = firstFunc;
        this.Function_2 = secondFunc;
    }

    public double getLeftDomainBorder() {
        return Math.max(Function_1.getLeftDomainBorder(), Function_2.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return Math.max(Function_1.getRightDomainBorder(), Function_2.getRightDomainBorder());
    }

    // проверку
    public double getFunctionValue(double x) {
        return Function_1.getFunctionValue(x)* Function_2.getFunctionValue(x);//произведение функций
    }
}
