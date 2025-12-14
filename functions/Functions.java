package functions;

import functions.meta.*;

public class Functions {

    private Functions(){} //приватный конструткор

    public static double integrate(Function func, double leftBound, double rightBound, double step) throws IllegalArgumentException{

        //проверка левой границы
        if(leftBound < func.getLeftDomainBorder() ) throw new IllegalArgumentException("Неподходящее значение левой границы");

        //проверка правой границы
        if(rightBound > func.getRightDomainBorder()) throw new IllegalArgumentException("Неподходящее значение правой границы");

        //левая граница должна быть меньше правой
        if(leftBound > rightBound) throw new IllegalArgumentException("Левая граница должна быть меньше правой");

        //вычисление интеграла
        double current = leftBound;
        double result = 0;
        while (current < rightBound){
            double next = Math.min(rightBound, current+step);

            double a = func.getFunctionValue(current);
            double b = func.getFunctionValue(next);
            double h = next - current;

            result+= ((a+b)*h)/2;

            current = next;
        }
        return result;

    }


    public static Function shift( Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY); //функция сдвига
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f, scaleX, scaleY); //функция масштабирования
    }

    public static Function power(Function f, double power){
        return new Power(f, power); //функция возведения в степень
    }

    public static Function sum(Function f1, Function f2){
        return new Sum(f1,f2); //функция суммы
    }

    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2); //функция произведения
    }

    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2); //композиция функций
    }
}
