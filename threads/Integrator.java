package threads;

import functions.Function;
import functions.Functions;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphoreGen;
    private Semaphore semaphoreInteg;

    public Integrator(Task task, Semaphore semGen, Semaphore semInteg){
        this.task = task;
        this.semaphoreGen = semGen;
        this.semaphoreInteg = semInteg;
    }

    @Override
    public  void run() {
        try {
            while (!isInterrupted()) {
                semaphoreInteg.acquire();

                try{
                    Function func = task.getFunc();
                    //границы интегрирования
                    double leftBorder = task.getLeftBorder();
                    double rightBorder = task.getRightBorder();
                    double step = task.getStep();//шаг

                    //вычисляем интеграл
                    double result = Functions.integrate(func, leftBorder, rightBorder, step);
                    System.out.printf("Result %.2f %.2f %.2f %.2f\n\n", leftBorder, rightBorder, step, result);

                }catch (Exception e){
                    System.out.println("Ошибка вычислений");
                }finally {
                    semaphoreGen.release();
                }
            }
        }catch (InterruptedException e){
            System.out.println("Работа интегратора прервана");
            Thread.currentThread().interrupt();
        }
    }
}
