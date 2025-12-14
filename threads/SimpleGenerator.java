package threads;

import functions.*;
import functions.basic.*;

public class SimpleGenerator implements Runnable {
    private Task task;//ссылка на задание
    private int countGenerate = 0; //счетчик заданий


    public SimpleGenerator(Task task){
        this.task= task;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                double base = Math.random() * 9 + 1;//основание логорифма
                Function log = new Log(base);//логорифмическая функция
                double leftBorder = Math.random() * 100;//левая граница
                double rightBorder = 100 + Math.random() * 100;//праваят граница
                double step = Math.random();//шаг

                synchronized (task) {
                    while (task.getFunc() != null)
                        task.wait();

                    // устанавливаем параметры
                    task.setFunc(log);
                    task.setLeftBorder(leftBorder);
                    task.setRightBorder(rightBorder);
                    task.setStep(step);
                    System.out.printf("Source %.2f %.2f %.2f\n", leftBorder, rightBorder, step);

                    countGenerate++;

                    task.notifyAll();
                }
                Thread.sleep(10);
            }

            System.out.println("Сгенерировано: " + countGenerate);
        }catch (InterruptedException e) {
            System.out.println("Генератор прерван");
        }
    }
}