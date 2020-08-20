public class Line {
    private Point one;
    private Point two;
    private float length;

    public Line(){
        one = new Point();
        two = new Point();
        length = 0;
    }
    public Line(Point one, Point two){
        this.one = one;
        this.two = two;
        this.length = (float) Line.lineLength(one, two);
    }
    public static double lineLength(Point one, Point two){
        return Math.sqrt(Math.pow((one.getX() - two.getX()), 2) + Math.pow((one.getY() - two.getY()), 2));;
    }
}
