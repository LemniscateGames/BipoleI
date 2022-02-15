package old.BipoleI.lib.ui;

/** A value that stores an array of Numbers and adds up their int values when this Number's intValue is retreived. **/
public class StretchValue extends Number {
    private Number[] numbers;
    public StretchValue(Number... numbers) {
        this.numbers = numbers;
    }

    @Override
    public int intValue() {
        int total = 0;
        for (Number num : numbers){
            total += num.intValue();
        }
        return total;
    }

    @Override
    public long longValue() {
        long total = 0;
        for (Number num : numbers){
            total += num.longValue();
        }
        return total;
    }

    @Override
    public float floatValue() {
        float total = 0;
        for (Number num : numbers){
            total += num.floatValue();
        }
        return total;
    }

    @Override
    public double doubleValue() {
        double total = 0;
        for (Number num : numbers){
            total += num.doubleValue();
        }
        return total;
    }
}
