package lib.display.timing;

import lib.display.TimingFunction;

public class LinearTiming implements TimingFunction {
    public double valueAtTime(double t) {
        return 1-t;
    }
}
