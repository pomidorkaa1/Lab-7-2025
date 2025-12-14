package threads;

import functions.Function;
import functions.basic.Log;

import java.util.concurrent.Semaphore;

public class Generator extends  Thread{
    private Task task;
    private Semaphore semaphoreGen;
    private Semaphore semaphoreInteg;

    public Generator(Task task, Semaphore semGen, Semaphore semInteg){
        this.task = task;
        this.semaphoreGen = semGen;
        this.semaphoreInteg = semInteg;
    }

    @Override
    public void run(){
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                if (Thread.currentThread().isInterrupted())
                    break;

                semaphoreGen.acquire();

                try{
                    double base = Math.random() * 9 + 1; //основание логорифма
                    Function log = new Log(base);
                    double leftBorder = Math.random() * 100;//левая граница интегрирования
                    double rightBorder = 100+ Math.random() * 100;//правая граница интегрирования
                    double step = Math.random();//шаг интегрирования

                    //устанавливаем параметры
                    task.setFunc(log);
                    task.setLeftBorder(leftBorder);
                    task.setRightBorder(rightBorder);
                    task.setStep(step);
                    System.out.printf("Source %.2f %.2f %.2f\n", leftBorder, rightBorder, step);


                } catch (Exception e) {
                    System.out.println("Ошибка создания задачи");
                }finally {
                    semaphoreInteg.release();
                }

            }
        }catch (InterruptedException e) {
            System.out.println("Работа генератора прервана");
            Thread.currentThread().interrupt();
        }
    }

}