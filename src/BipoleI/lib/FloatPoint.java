package BipoleI.lib;

public class FloatPoint implements Coordinate {
    private float x, y;
    public FloatPoint(float x, float y){
        this.x = x;
        this.y = y;
    }
    public FloatPoint(){
        x=0; y=0;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
