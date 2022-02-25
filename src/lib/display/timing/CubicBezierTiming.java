package lib.display.timing;

import lib.display.TimingFunction;

public class CubicBezierTiming implements TimingFunction {
    private final double x1, y1, x2, y2;

    public CubicBezierTiming(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double valueAtTime(double t) {
        // Bezier curve formula:
        //P = (1−t)3P1 + 3(1−t)2tP2 +3(1−t)t2P3 + t3P4
        // first term is not needed as p1 is (0,0) so term will always be 0
//        return 3*Math.pow(t, 2)*t*y1 + 3*t*Math.pow((1-t), 2)*y2 + Math.pow(1-t,3);
        return
//                Math.pow(t, 3)*0.0 +
                3*Math.pow(t, 2)*(1-t)*y1
                + 3*t*Math.pow(1-t, 2)*y2
                + Math.pow(1-t, 3); // *1.0
    }
}
