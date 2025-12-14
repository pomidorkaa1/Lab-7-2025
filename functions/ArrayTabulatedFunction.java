package functions;

import java.io.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable{
    private FunctionPoint[] points; //массив точек, задающих функцию
    private int pointsCount;

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;  //текущий индекс для итерации

            @Override
            public boolean hasNext() { //проверка на существование точки
                return index < pointsCount;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Следующей точки не существует");
                }
                FunctionPoint point = points[index];
                FunctionPoint copyPoint = new FunctionPoint(point.getX(), point.getY()); //создаем копию точки
                index++;
                return copyPoint;//возвращает копию
            }

            @Override
            public void remove() { //удаление точки не предусмотрено
                throw new UnsupportedOperationException("Удаление точки невозможно");
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);//массивная функция с границами и количеством точек
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);//массивная функция с границами и значениями
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);//массивная табулированная функция из точек
        }
    }

    public ArrayTabulatedFunction() {}

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.pointsCount = in.readInt();
        this.points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    @Override
    public String toString(){
        String result = "{";
        for (int i = 0; i<pointsCount; i++){
            result += String.format("(%.1f; %.1f)", points[i].getX(), points[i].getY());
        }
        result+= "}";
        return result;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        //реализации того же класса
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction func = (ArrayTabulatedFunction) o;
            if (this.pointsCount != func.pointsCount) //сравниваем количество точек
                return false;

            for (int i = 0; i < pointsCount; i++){
                if (!this.points[i].equals(func.points[i])) //сравниваем точки с помощью equals
                    return false;

            }
        }

        //другие реализации TabulatedFunction
        else {
            TabulatedFunction func = (TabulatedFunction) o;
            if (this.getPointsCount() != func.getPointsCount()) return false; //сравниваем количество точек

            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(func.getPoint(i))) return false; //сравниваем точки
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        int hash = pointsCount;

        //проходим по всем точкам массива и комбинируем хэш точки через хor
        for (int i = 0; i < pointsCount; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }


    @Override
    public Object clone(){
        FunctionPoint[] pointsClone = new FunctionPoint[pointsCount];

        //проходим по всем точкам массива и копируем каждую
        for(int i =0; i < pointsCount; i++){
            pointsClone[i] = (FunctionPoint) points[i].clone();
        }
        return new ArrayTabulatedFunction(pointsClone);
    }


    //создаёт объект табулированной функции по заданным левой и правой границам области определения, а также количеству точек для табулирования
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException{

        if(leftX >= rightX) throw new IllegalArgumentException("Левая граница больше правой.");
        if(pointsCount < 2) throw new IllegalArgumentException("Количество точек меьше двух.");

        this.pointsCount = pointsCount;
        points = new FunctionPoint[pointsCount];
        double step = Math.abs(rightX - leftX) / (pointsCount - 1); //шаг между точками
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, 0);
        }
    }

    //для получения точек FunctionPoint
    public ArrayTabulatedFunction(FunctionPoint[] arrayPoints ) throws IllegalArgumentException{

        //проверка количества точек
        if (arrayPoints.length< 2 )throw new IllegalArgumentException("Количество точек меньше 2.");

        this.pointsCount = arrayPoints.length;

        //упорядоченность точек
        for (int i=1; i < pointsCount; i++){
            if  (arrayPoints[i].getX()<= arrayPoints[i-1].getX()) throw new IllegalArgumentException("Гарушена упорядоченность. Точки должны быть по возрастанию x");
        }

        //масив точек
        this.points = new FunctionPoint[pointsCount];
        for (int i=0; i<pointsCount; i++) this.points[i] = new FunctionPoint(arrayPoints[i]);
    }

    //создает объект табулированной функции по заданным левой и правой границам области определения, а также значениям функции
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {

        if(leftX >= rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой.");
        if(values.length < 2) throw new IllegalArgumentException("Количество точек меньше двух.");

        int pointsCount = values.length;
        this.pointsCount = pointsCount;
        points = new FunctionPoint[pointsCount];
        double step = Math.abs(rightX - leftX) / (pointsCount - 1); //интервал (шаг между точками)
        for (int i = 0; i < pointsCount; i++) {
            points[i] = new FunctionPoint(leftX + i * step, values[i]);
        }
    }


    public double getLeftDomainBorder() {
        return points[0].getX(); //возвращение левой границы области опрделения
    }

    public double getRightDomainBorder() {
        return points[this.pointsCount - 1].getX(); // возвращение правой границы области определения
    }

    //возвращение значения функции в точке x, если эта точка лежит в области определения функции
    public double getFunctionValue(double x) {
        double leftX = getLeftDomainBorder();
        double rightX = getRightDomainBorder();

        if (x >= leftX && x <= rightX) {
            //поиск соседних точек
            for (int i = 0; i < this.pointsCount - 1; i++) {
                double x1 = points[i].getX();
                double x2 = points[i + 1].getX();

                if (x >= x1 && x <= x2) {
                    if (Math.abs(x - x1) < 1e-10) return points[i].getY();
                    if (Math.abs(x - x2) < 1e-10) return points[i + 1].getY();

                    double y1 = points[i].getY();
                    double y2 = points[i + 1].getY();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
            }

        }

        return Double.NaN;
    }

    //возвращения количество точек
    public int getPointsCount(){
        return pointsCount;
    }

    //возвращает копию точки по указанному индексу
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index<0 || index >=pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует, недопустимое значение индекса.");

        return new FunctionPoint(points[index]);
    }

    //заменяет указанную точку табулированной функции на переданную
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        if(index<0 || index>= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует, недопустимое значение индекса.");

        if ( ( index != 0 && point.getX() <= points[index - 1].getX()) || (index != pointsCount-1 && point.getX() >= points[index+1].getX())) //текущий х должен быть больше левой и меньше правой точки
            throw new InappropriateFunctionPointException("Неверное значение Х. Точка должна входить в интервал.");

        points[index]= new FunctionPoint(point);
    }

    //возвращает значение абсциссы точки с указанным номером
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException{
        if(index<0 || index>= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует, недопустимое значение индекса.");
        return points[index].getX();
    }

    //измененяет значения х
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException{
        if (index < 0 || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует, недопустимое значение индекса.");
        if ((index != 0 && x <= points[index - 1].getX()) || (index != pointsCount-1 &&  x >= points[index + 1].getX()))
            throw new InappropriateFunctionPointException("Неверное значение Х. Точка должна входить в интервал");

        points[index].setX(x);
    }

    //возвращает координаты у
    public double getPointY (int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует, недопустимое значение индекса.");
        return points[index].getY();
    }

    //изменяет значения у
    public void setPointY(int index, double y)throws FunctionPointIndexOutOfBoundsException{
        if (index < 0 || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует, недопустимое значение индекса.");

        points[index].setY(y);
    }

    //удаление точки по указанному индексу
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if(index < 0 || index >= pointsCount) throw new FunctionPointIndexOutOfBoundsException("Точки не существует.");
        if(pointsCount<3) throw new FunctionPointIndexOutOfBoundsException("Недосточное количество точек.");

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        points[--pointsCount] = null;
    }

    //добавление новой точки в массив с сохранением порядка по Х
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index= 0;

        double newX = point.getX();
        //находим позицию для вставки точки
        while (index < pointsCount && newX > points[index].getX())
            index++;

        if( index < pointsCount && Math.abs(points[index].getX() - newX) < 1e-10) throw new InappropriateFunctionPointException("Точка  с таким значением Х уже существет.");

        //проверка для увелечение массива
        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        if (index < pointsCount)
            System.arraycopy(points, index, points, index + 1, pointsCount - index);

        points[index] = new FunctionPoint(point);
        pointsCount++;

    }
    //вывод
    public void printFunc(){
        for(int i = 0; i< pointsCount; i++)
            System.out.printf("(%.2f, %.2f) ", points[i].getX(), points[i].getY());

        System.out.println();
    }

}
