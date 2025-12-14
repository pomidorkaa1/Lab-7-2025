package functions;
import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    private static class FunctionNode {
        private FunctionNode prev;
        private FunctionNode next;
        private FunctionPoint point;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
            this.next = null;
            this.prev = null;
        }

        //геттеры и сеттеры
        public FunctionPoint getPoint() {return point;}

        public void setPoint(FunctionPoint point) {this.point = point;}

        //возвращаем предыдущий узел
        public FunctionNode getPrev() {return prev;}

        //устанавливаем предыдущий узел
        public void setPrev(FunctionNode prev) {this.prev = prev;}

        //возвращаем следующий узел
        public FunctionNode getNext() {return next;}

        //устанавливаем следующий узел
        public void setNext(FunctionNode next) {this.next = next;}
    }

    private FunctionNode head = new FunctionNode(null); //голова списка
    private int pointsCount;


    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode curr = head.getNext();

            @Override
            public boolean hasNext() {
                return curr != head;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Элемента не существует");//если точек нет
                }
                FunctionPoint point = new FunctionPoint(curr.getPoint());
                curr = curr.getNext();
                return point;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление невозможно");
            }
        };
    }


    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount); //создание связной функции с границами и количеством точек
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values); //создание связной функции с границами и значением
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points); //создание табулированной связной функции из точек
        }
    }

    private FunctionNode getNodeByIndex(int index) {

        FunctionNode curr;
        if (index < pointsCount / 2) {
            //двигаемся от головы вперед
            curr = head.getNext(); //начинаем с первого значащего элемента
            for (int i = 0; i < index; i++) {
                curr = curr.getNext();
            }
        } else {
            //двигаемся от хвоста назад
            curr = head.getPrev(); //с последнего элемента
            for (int i = pointsCount - 1; i > index; i--) {
                curr = curr.getPrev();
            }
        }

        return curr;
    }

    //добвление в конец
    private FunctionNode addNodeToTail(FunctionPoint point) {

        FunctionNode tail = head.getPrev();
        FunctionNode newNode = new FunctionNode(point);

        tail.setNext(newNode);
        newNode.setPrev(tail);
        newNode.setNext(head);
        head.setPrev(newNode);

        pointsCount++;

        return newNode;
    }

    private FunctionNode addNodeByIndex(int index, FunctionPoint point) {
        FunctionNode newNode = new FunctionNode(point);

        if (index == pointsCount)
            return addNodeToTail(point); // добавляем в конец

        FunctionNode currNode = getNodeByIndex(index);

        newNode.setPrev(currNode.getPrev());
        newNode.setNext(currNode);
        currNode.getPrev().setNext(newNode);
        currNode.setPrev(newNode);

        pointsCount++;

        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {

        FunctionNode delNode = getNodeByIndex(index);

        delNode.getNext().setPrev(delNode.getPrev());
        delNode.getPrev().setNext(delNode.getNext());

        pointsCount--;

        return delNode;
    }
    public LinkedListTabulatedFunction() {}

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        // записываем все точки
        FunctionNode current = head.getNext();
        while (current != head) {
            out.writeObject(current.getPoint());
            current = current.getNext();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        pointsCount = in.readInt();
        this.head = new FunctionNode(null);
        this.head.setPrev(head);
        this.head.setNext(head);

        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = (FunctionPoint) in.readObject();
            addNodeToTail(point);
        }
    }

    @Override
    public String toString(){
        String result = "{";
        FunctionNode curr = head.getNext();

        //проходим по всем узлам списка, пока не вернемся к голове
        while (curr!= head){
            result += String.format("(%.1f; %.1f)", curr.getPoint().getX(), curr.getPoint().getY());
            curr =  curr.getNext();
        }
        result+= "}";
        return result;
    }

    @Override
    public boolean equals(Object o){
        //если этот же объект, то возвращается true
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        //сранвение с той же реализацией
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction func = (LinkedListTabulatedFunction) o;

            //сравниваем количество точек
            if (this.pointsCount != func.pointsCount) return false;

            FunctionNode currFirst = this.head.getNext();
            FunctionNode currSecond = func.head.getNext();

            //сравниваем точки
            while (currFirst != head ){
                if (!currFirst.getPoint().equals(currSecond.getPoint())) return false;
                currFirst = currFirst.getNext();
                currSecond = currSecond.getNext();
            }
        }

        //если объект другой реализации TabulatedFunction
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

        FunctionNode curr = head.getNext();

        //проходим по всем узлам
        while (curr != head ){
            hash ^= curr.getPoint().hashCode();
            curr = curr.getNext();
        }
        return hash;
    }

    @Override
    public Object clone(){

        //создаем временный массив для хранения копий всех точек
        FunctionPoint[] pointsCopy = new FunctionPoint[pointsCount];
        FunctionNode curr = head.getNext();

        for (int i = 0; curr!= head; i++) {
            pointsCopy[i] = new FunctionPoint(curr.getPoint()); //создаем новую точку с такими же координатвми
            curr = curr.getNext();
        }
        return new LinkedListTabulatedFunction(pointsCopy);
    }

    //создание табулированной функции
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX)
            throw new IllegalArgumentException("Левая граница больше правой");
        if (pointsCount < 2)
            throw new IllegalArgumentException("Количество точек меньше двух");

        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = Math.abs(rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint point = new FunctionPoint(leftX + i * step, 0);
            addNodeToTail(point);
        }
    }

    //табулированная функция с заданными значениями по оси ординат
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] value) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница больше правой");
        }
        if (value.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }

        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        double step = Math.abs(rightX - leftX) / (value.length - 1);
        for (int i = 0; i < value.length; i++) {
            FunctionPoint point = new FunctionPoint(leftX + i * step, value[i]);
            addNodeToTail(point);
        }
    }

    //конструктор для получения точек FunctionPoint
    public LinkedListTabulatedFunction(FunctionPoint[] arrayPoints) throws IllegalArgumentException{

        //проверка количства точек
        if(arrayPoints.length < 2) throw new IllegalArgumentException("Количсевто точек меньше 2.");

        //упорядоченность точек
        for (int i =0; i < arrayPoints.length - 1; i++){
            if(arrayPoints[i].getX() >= arrayPoints[i + 1].getX()){
                throw new IllegalArgumentException("Нарушена упорядоченность, точки должны быть по возрастанию.");
            }
        }

        //инициализация головы списка
        head.setPrev(head);
        head.setNext(head);
        this.pointsCount = 0;

        //список с последовательным добавлением точек в конец
        for(int i=0; i< arrayPoints.length; i++){
            addNodeToTail(new FunctionPoint(arrayPoints[i]));
        }


    }

    //возвращение левой границы
    public double getLeftDomainBorder() {return head.getNext().getPoint().getX();}

    //возвращение правой границы
    public double getRightDomainBorder() {return head.getPrev().getPoint().getX();}

    //возвращение у, если точка лежит в области определения функции
    public double getFunctionValue(double x) {
        double leftX = getLeftDomainBorder();
        double rightX = getRightDomainBorder();

        if (x >= leftX && x <= rightX) {

            FunctionNode curr = head.getNext();
            while (curr != head && curr.getNext() != head) {
                double x1 = curr.getPoint().getX();
                double x2 = curr.getNext().getPoint().getX();

                // совпадает ли х
                if (Math.abs(x - x1) < 1e-10)
                    return curr.getPoint().getY();
                if (Math.abs(x - x2) < 1e-10)
                    return curr.getNext().getPoint().getY();
                // попадает ли x в интервал
                if (x > x1 && x < x2) {
                    double y1 = curr.getPoint().getY();
                    double y2 = curr.getNext().getPoint().getY();
                    return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
                }
                // переходим к следующему
                curr = curr.getNext();
            }
        }
        return Double.NaN;
    }

    //возвращения количества точек
    public int getPointsCount() {
        return pointsCount;
    }

    //возвращение точки
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        double x = getNodeByIndex(index).getPoint().getX();
        double y = getNodeByIndex(index).getPoint().getY();
        return new FunctionPoint(x, y);
    }

    //изменение указанной точки на заданную
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if ((index > 0 && point.getX() < getNodeByIndex(index - 1).getPoint().getX()) || (index < pointsCount - 1 && point.getX() > getNodeByIndex(index + 1).getPoint().getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        getNodeByIndex(index).setPoint(new FunctionPoint(point));

    }

    //возвращение координаты х
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return getNodeByIndex(index).getPoint().getX();
    }

    //изменение значения х
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if ((index != 0 && x <= getNodeByIndex(index - 1).getPoint().getX()) ||
                (index != pointsCount - 1 && x >= getNodeByIndex(index + 1).getPoint().getX()))
            throw new InappropriateFunctionPointException("Неверный Х");

        getNodeByIndex(index).getPoint().setX(x);
    }

    //возвращения координаты у
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        return getNodeByIndex(index).getPoint().getY();
    }

    //изменения значения у
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        getNodeByIndex(index).getPoint().setY(y);
    }

    //удаление точки
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Не существует точки");
        if (pointsCount < 3)
            throw new FunctionPointIndexOutOfBoundsException("Недостаточное количество точек");

        deleteNodeByIndex(index);

    }

    //добавление точки
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index = 0;

        double newX = point.getX();

        // находим позицию для вставки точки
        FunctionNode curr = head.getNext();
        while (curr != head && newX > curr.getPoint().getX()) {
            curr = curr.getNext();
            index++;
        }

        if (curr != head && Math.abs(curr.getPoint().getX() - newX) < 1e-10)
            throw new InappropriateFunctionPointException("Точка с таким Х уже существует");

        addNodeByIndex(index, point);

    }



    // вывод массива точек
    public void printFunc() {
        FunctionNode curr = head.getNext();
        while (curr != head) {
            System.out.printf("(%.2f, %.2f) ", curr.getPoint().getX(), curr.getPoint().getY());
            curr = curr.getNext();
        }
        System.out.println();
    }
}
