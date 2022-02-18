package lib.display.shaperendering;

public class Point3D {
    public double r, c, h;

    public Point3D(double r, double c, double h){
        this.r = r;
        this.c = c;
        this.h = h;
    }

    public String toString(){
        return String.format("Point3D(%.02f, %.02f, %.02f", r, c, h);
    }
}
