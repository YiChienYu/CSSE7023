public class Point {
    private float X = 0;
    private float  Y = 0;

    public float  getX(){
        return X;
    }
    public float  getY(){
        return Y;
    }

    public void movePoint(float deltaX, float deltaY){
        this.X += deltaX;
        this.Y += deltaY;
    }
}
