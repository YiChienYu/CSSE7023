public class Point {
    private float X ;
    private float  Y;
    private String toS = null;

    public Point(){
        this.X = 0;
        this.Y = 0;
    }

    public Point(float X, float Y){
        this.X = X;
        this.Y = Y;
    }

    public float  getX(){
        return X;
    }
    public float  getY(){
        return Y;
    }

    public Point movePoint(float deltaX, float deltaY){
        return new Point(X+deltaX, Y+deltaY);
    }

    @Override
    public String toString() {
        if(toS == null){
            toS = String.format("(%s, %s)", X, Y);
        }
        return toS;
    }



}
