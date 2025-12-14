package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {

    private TabulatedFunctions(){

    } //приватный конструктор

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory(); //фабрика по умолчанию

    //метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory) {
        factory = newFactory; //устанавливаем новую фабрику
    }

    //три перегруженных метода
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount); //создает функцию через фабрику с границами и количеством точек
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values); //создает функцию через фабрику с границами и значениями
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points); //создает функцию через фабрику из точек
    }


    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null)  throw new IllegalArgumentException("Функция не задана");

        if (pointsCount < 2) throw new IllegalArgumentException("Количсетво точек должно быть не менее 2"); //проверка на минимальное количсетво точек

        if (leftX >= rightX) throw new IllegalArgumentException("Левая граница должнна быть меньше правой");

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder())
        {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения"); //проверка на вхождение границ в область определения
        }

        TabulatedFunction tabulatedFunc = createTabulatedFunction(leftX, rightX, pointsCount);

        //записываем значения в массив
        for (int i = 0; i < pointsCount; i++){
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }
        return tabulatedFunc;
    }

    //рефлексия
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount) {
        try {
            //проверяем, что класс реализует tabulatedFunction
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("Класс должен реализовывать интерфейс TabulatedFunction");
            }
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class); //ищем нужный конструктор

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount); //создаем объект через рефлексию
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) { //проверяем, что класс реализует tabulatedFunction
                throw new IllegalArgumentException("Класс должен реализовывать интерфейс TabulatedFunction");
            }
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class); //щем нужный конструктор

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values); //создаем объект через рефлексию
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта", e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        try {
            if (!TabulatedFunction.class.isAssignableFrom(functionClass)) { //проверяем, что класс реализует TabulatedFunction
                throw new IllegalArgumentException("Класс должен реализовывать интерфейс TabulatedFunction");
            }
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class); //ищем нужный конструктор

            return (TabulatedFunction) constructor.newInstance((Object) points); //создаем объект через рефлексию
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Ошибка при создании объекта", e);
        }
    }

    //метод tabulate с рефлексией
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, double leftX, double rightX, int pointsCount) {
        if (function == null)
            throw new IllegalArgumentException("Функция null"); //проверяем что функция не null
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Недостаточное количество точек"); //проверка на минимальное кол-во точек
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница меньше правой");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения"); //проверка на нахлждение границ в области определения
        }

        TabulatedFunction tabulatedFunc = createTabulatedFunction(functionClass, leftX, rightX, pointsCount);
        // записываем значения в массив
        for (int i = 0; i < pointsCount; i++){
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }

        return tabulatedFunc;
    }


    public static TabulatedFunction inputTabulatedFunction(Class<?> functionClass, InputStream in) throws IOException {
        DataInputStream inputData = new DataInputStream(in); //поток для чтения данных
        int pointsCount = inputData.readInt(); //читаем кол-во точек
        FunctionPoint[] points = new FunctionPoint[pointsCount]; //массив для точек

        for (int i = 0; i < pointsCount; i++) {
            double x = inputData.readDouble(); //координата х
            double y = inputData.readDouble(); //координата у
            points[i] = new FunctionPoint(x, y); //новая точка
        }

        return createTabulatedFunction(functionClass, points); //создание табулированной функции через рефлексию

    }

    public static TabulatedFunction readTabulatedFunction(Class<?> functionClass, Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval; //координата х
            tokenizer.nextToken();
            double y = tokenizer.nval; //координата у
            points[i] = new FunctionPoint(x, y);//создаем новую точку
        }

        return createTabulatedFunction(functionClass,points);
    }



    //работа с байтовым потоком
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        int pointsCount = function.getPointsCount();
        DataOutputStream outputData = new DataOutputStream(out);
        outputData.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            outputData.writeDouble(function.getPointX(i));
            outputData.writeDouble(function.getPointY(i));
        }
        outputData.flush();
    }

    //чтение данных из файла
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream inputData = new DataInputStream(in);
        int pointsCount = inputData.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = inputData.readDouble();
            double y = inputData.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);

    }

    //работа с символьным потоком
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        PrintWriter writer = new PrintWriter(out);
        int pointsCount = function.getPointsCount();
        writer.print(pointsCount); //записываем количество точек
        writer.print(" ");//пробел

        for (int i = 0; i < pointsCount; i++) {
            writer.print(function.getPointX(i)); //записываем х
            writer.print(" "); //пробел
            writer.print(function.getPointY(i)); //записываем у
            writer.print(" ");//пробел
        }
        writer.flush(); //сбрасываем буфер
    }


    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in); //токенизатор для чтения
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval; //кол-во точек\

        //читаем количество точек и создаем массив
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken();
            double x = tokenizer.nval; //х
            tokenizer.nextToken();
            double y = tokenizer.nval; // у
            points[i] = new FunctionPoint(x, y); //новая точка
        }

        return createTabulatedFunction(points);
    }

}