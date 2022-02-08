package BipoleI.lib;

// Same as point but uses ints.
public class IntPoint {
    private final int x, y;
    public IntPoint(int x, int y){
        this.x = x;
        this.y = y;
    }
    public IntPoint(){
        x=0; y=0;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
