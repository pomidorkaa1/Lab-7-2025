package threads;

import functions.Function;

public class Task {
    private double leftBorder;//левая граница интегрирования
    private double rightBorder;//правая граница интегрирования
    private double step;//шаг дискретизации
    private Function function; //инетгрируемая функция
    private int count; //кол-во заданий

    public Task() {

    }

    public Task(Function func, double leftBorder, double rightBorder, double step, int count) {
        this.function = func;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
        this.step = step;
        this.count = count;
    }

    public void setFunc(Function func){this.function = func;}

    public Function getFunc(){return function;}

    public double getLeftBorder() {return leftBorder;}

    public void setLeftBorder(double leftBorder) {this.leftBorder = leftBorder;}

    public double getRightBorder() {return rightBorder;}

    public void setRightBorder(double rightBorder) {this.rightBorder = rightBorder;}

    public double getStep() {return step;}

    public void setStep(double step) {this.step = step;}

    public int getTaskCount() {return count;}

    public void setCount(int count) {this.count = count;}
}

