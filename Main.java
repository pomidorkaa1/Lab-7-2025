import functions.*;
import functions.basic.Cos;
import functions.basic.Sin;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n1. Проверка работы итераторов классов табулированных функций.");
        TabulatedFunction arrFunc = new ArrayTabulatedFunction(0, 10, 5);//создание табулированной функции

        System.out.println("Массив:");
        for (FunctionPoint p : arrFunc) { //цикл for-each по точкам функции
            System.out.println(p); //вывод текущей точки
        }

        TabulatedFunction linkFunc = new LinkedListTabulatedFunction(0, 10, 5);//создание табулированной функции
        System.out.println("\nСписок:");
        for (FunctionPoint p : linkFunc) {// цикл for-each по точкам функции
            System.out.println(p); //вывод текущей точки
        }

        System.out.println("\n2. Проверка работы фабрик.");
        Function f = new Cos();
        TabulatedFunction tf;
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());
        TabulatedFunctions.setTabulatedFunctionFactory(new
                ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println(tf.getClass());


        System.out.println("\n3. Проверка рефлексии.");
        TabulatedFunction func;

        func = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.createTabulatedFunction(
                ArrayTabulatedFunction.class, 0, 10, new double[] {0, 10});
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.createTabulatedFunction(
                LinkedListTabulatedFunction.class,
                new FunctionPoint[] {
                        new FunctionPoint(0, 0),
                        new FunctionPoint(10, 10)
                }
        );
        System.out.println(func.getClass());
        System.out.println(func);

        func = TabulatedFunctions.tabulate(
                LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11);
        System.out.println(func.getClass());
        System.out.println(func);
    }



}


