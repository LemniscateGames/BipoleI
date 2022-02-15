package lib.units;

import lib.Unit;

public class Soldier extends Unit {

    @Override public int getBaseHp() { return 5; }

    @Override public int getBaseAtk() { return 2; }

    @Override public int getBaseDelay() { return 10000; }
}
