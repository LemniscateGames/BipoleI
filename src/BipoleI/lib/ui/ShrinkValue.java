package BipoleI.lib.ui;

/** Works the same as StretchValue but shrinks the first value by all other values. **/
public class ShrinkValue extends Number {
    private Number[] numbers;
    public ShrinkValue(Number... numbers) {
        this.numbers = numbers;
    }

    @Override
    public int intValue() {
        int total = numbers[0].intValue();
        for (int i=1; i<numbers.length; i++){
            total -= numbers[i].intValue();
        }
        return total;
    }

    @Override
    public long longValue() {
        long total = numbers[0].longValue();
        for (int i=1; i<numbers.length; i++){
            total -= numbers[i].longValue();
        }
        return total;
    }

    @Override
    public float floatValue() {
        float total = numbers[0].floatValue();
        for (int i=1; i<numbers.length; i++){
            total -= numbers[i].floatValue();
        }
        return total;
    }

    @Override
    public double doubleValue() {
        double total = numbers[0].doubleValue();
        for (int i=1; i<numbers.length; i++){
            total -= numbers[i].doubleValue();
        }
        return total;
    }
}
