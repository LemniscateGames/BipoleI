package lib.display.timing;

import lib.display.TimingFunction;
import lib.misc.DoublePoint;

import java.awt.*;
import java.io.Serializable;

public class CubicBezierTiming implements TimingFunction {
    //package graphicsEngine.math;

/**
 * a class that models a Cubic-Bezier curve
 * @author Adam Levi, Marina Skarbovsky
 */
    private static final long serialVersionUID = -5219859720055898005L;
    private final DoublePoint[] P;

    public CubicBezierTiming(double x1, double y1, double x2, double y2)
    {
        P = new DoublePoint[]{
                new DoublePoint(0,0),
                new DoublePoint(x1, y1),
                new DoublePoint(x2, y2),
                new DoublePoint(1, 1)
        };
    }


    /**
     * returns the point in 3d space that corresponds to the given value of t
     * @param t curve's parameter that should be in the range [0, 1.0]
     * @return  the point in 3d space that corresponds to the given value of t
     */
    public DoublePoint getValue(double t)
    {
        if (t > 1.0 || t < 0.0)
        {
            throw new IllegalArgumentException("The value of t is out of range: " + t + " .");
        }
        double one_minus_t = 1 - t;
        double x = 0, y = 0;
        DoublePoint[] terms = new DoublePoint[4];
        terms[0] = calcNewVector(one_minus_t * one_minus_t * one_minus_t, P[0]);
        terms[1] = calcNewVector(3 * one_minus_t * one_minus_t * t, P[1]);
        terms[2] = calcNewVector(3 * one_minus_t * t * t, P[2]);
        terms[3] = calcNewVector(t * t * t, P[3]);
        for (int i = 0 ; i < 4; i++)
        {
            x += terms[i].x;
            y += terms[i].y;
        }
        return new DoublePoint(x, y);
    }

    /**
     * calculates and returns a new vector that is base * scaler
     * @param scaler
     * @param base
     * @return
     */
    private DoublePoint calcNewVector(double scaler, DoublePoint base)
    {
        return new DoublePoint(base.x * scaler, base.y * scaler);
    }

    @Override
    public double valueAtTime(double percent) {
        return getValue(1-percent).y;
    }

    @Override
    public boolean endAtStart() {
        return false;
    }
}
