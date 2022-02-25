package lib.display;

import lib.display.timing.CubicBezierTiming;
import lib.display.timing.LinearTiming;
import lib.display.timing.SineShake;


/** Take in a completion percentage from 0.0 to 1.0 and return a value from 0.0 to 1.0.
 * THe function should always approach 0.0 as t approaches 0.0 and approach 1.0as t approaches 1.0. **/
public interface TimingFunction {
    double valueAtTime(double percent);

    default boolean endAtStart(){
        return false;
    }

    // common TimingFunctions as constants
    TimingFunction LINEAR = new LinearTiming();
    TimingFunction EASE = new CubicBezierTiming(.25,.1,.25,1);
    TimingFunction EASE_OUT = new CubicBezierTiming(0,0,.15,1);
    TimingFunction EASE_OUT_FAST = new CubicBezierTiming(.1,.66,.32,.99);
}