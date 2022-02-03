package BipoleI.lib.timingFunctions;

import BipoleI.lib.TimingFunction;

public class LinearTiming implements TimingFunction {
    public double valueAtTime(double t) {
        return t;
    }
}
