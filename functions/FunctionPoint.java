package functions;

public class FunctionPoint {
    private double x;
    private double y;

    public FunctionPoint(double x, double y)
    {
        this.x = x;
        this.y= y;
    }

    public FunctionPoint(FunctionPoint point)
    {
        this.x = point.getX();
        this.y= point.getY();
    }

    public FunctionPoint()
    {
        this.x =0;
        this.y =0;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }

    @Override
    public String toString()
    {
        return String.format("(%.1f; %.1f)", this.x, this.y); //возвращение строки с координатами точки
    }

    @Override
    public boolean equals(Object o){
        if (this == o) //проверка что это тот же объект если ссылки равны
            return true;//возвращаем true если объыекты равны
        if (o == null || getClass() != o.getClass()) //проверка является ли объект точкой
            return false;//если классы разные то возвращаем false

        double epsilon = 1e-10;
        return Math.abs(this.x - ((FunctionPoint) o).getX()) < epsilon && //сравниваем координаты
                Math.abs(this.y - ((FunctionPoint) o).getY()) < epsilon;
    }

    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(this.x);//битовое значение х
        long yBits = Double.doubleToLongBits(this.y);//битовое значение у

        int xHash = (int) (xBits ^ (xBits >>> 32));
        int yHash = (int) (yBits ^ (yBits >>> 32));

        return xHash ^ yHash;
    }


    @Override
    public Object clone(){
        return new FunctionPoint(x, y);//создание новой точки с теми же координатами
    }
}
