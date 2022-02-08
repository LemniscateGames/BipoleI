package BipoleI.lib.timingFunctions;

import BipoleI.lib.ui.TimingFunction;

public class LinearTiming implements TimingFunction {
    public double valueAtTime(double t) {
        return t;
    }
}
