package threads;

import functions.Function;
import functions.Functions;
import functions.basic.Log;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                Function func;
                double leftBorder, rightBorder, step;

                synchronized (task) {
                    while (task.getFunc() == null)
                        task.wait();



                    func = task.getFunc();
                    leftBorder = task.getLeftBorder();
                    rightBorder = task.getRightBorder();
                    step = task.getStep();

                    task.setFunc(null);
                    task.notifyAll();
                }

                double result = Functions.integrate(func, leftBorder, rightBorder, step);
                System.out.printf("Result %.2f %.2f %.2f %.2f\n\n",
                        leftBorder, rightBorder, step, result);

                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Итератор прерван");
        }
    }
}
