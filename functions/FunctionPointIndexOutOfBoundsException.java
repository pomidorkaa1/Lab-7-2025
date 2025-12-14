package functions;

//объявление класса исключений
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {

    //конструктор по умолчанию
    public FunctionPointIndexOutOfBoundsException() {
        super(); //вызов конструктора родительского класса
    }

    //конструктор с сообщением
    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message); //передаем сообщение в родительский класс
    }
}
