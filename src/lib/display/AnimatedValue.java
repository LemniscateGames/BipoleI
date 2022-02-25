package lib.display;

/** A value that changes over time and eventually comes to rest.
 * Can be given different timing functions and durations.
 */
public class AnimatedValue extends Number {
    private final TimingFunction timingFunction;
    private final int duration;
    private final double start;
    private final double end;

    private boolean started;
    private boolean finished;
    private long endTime;

    public AnimatedValue(TimingFunction timingFunction, int duration, double start, double end, boolean startOnInit){
        this.timingFunction = timingFunction;
        this.duration = duration;
        this.start = start;
        this.end = end;

        started = false;
        finished = false;

        if (startOnInit) {
            start();
        }
    }

    public AnimatedValue(TimingFunction timingFunction, int duration, double start, double end){
        this(timingFunction, duration, start, end, true);
    }

    public AnimatedValue(int duration, double start, double end){
        this(TimingFunction.EASE, duration, start, end, true);
    }

    public void start(){
        endTime = System.currentTimeMillis() + duration;
        started = true;
    }

    public double getValue(){
        if (!started) return start;
        if (finished) return timingFunction.endAtStart() ? start : end;

        long time = System.currentTimeMillis();

        if (time >= endTime){
            finished = true;
            return timingFunction.endAtStart() ? start : end;
        }

        double t = (double)(endTime - time)/duration;
        return start + (end-start)*timingFunction.valueAtTime(t);
    }

    public boolean isAnimating(){
        return started && !finished;
    }

    @Override
    public int intValue() {
        return (int)getValue();
    }

    @Override
    public long longValue() {
        return (long)getValue();
    }

    @Override
    public float floatValue() {
        return (float)getValue();
    }

    @Override
    public double doubleValue() {
        return getValue();
    }
}
