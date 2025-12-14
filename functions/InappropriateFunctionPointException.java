package functions;

public class InappropriateFunctionPointException extends Exception {
    //конструктор по умолчанию
    public InappropriateFunctionPointException(){
        //вызов конструктора родителя
        super();
    }
    //конструктор с сообщением
    public InappropriateFunctionPointException(String message){
        // передаем сообщение родителю
        super(message);
    }
}
