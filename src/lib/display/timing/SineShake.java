package lib.display.timing;

import lib.display.TimingFunction;

public class SineShake implements TimingFunction {
    private final double speed;
    private final double fadeExp;

    public SineShake(double speed, double fadeExp){
        this.speed = speed;
        this.fadeExp = fadeExp;
    }

    public double valueAtTime(double t) {
        return Math.sin(speed * (1-t)) * Math.pow(t, fadeExp);
    }

    @Override
    public boolean endAtStart() {
        return true;
    }


}
