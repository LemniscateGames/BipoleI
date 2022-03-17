package lib.units;

import lib.Team;
import lib.Unit;

public class EmptyLand extends Unit {
    public EmptyLand(Team team) {
        super(team);
    }

    @Override
    public String name() {
        return "-";
    }

    @Override
    public String desc() {
        return "";
    }

    @Override
    public Unit newUnit(Team team) {
        Unit unit = new EmptyLand(team);
        unit.initialize();
        return unit;
    }

    @Override
    public void initialize() {
        setValue(0);
        setHp(1);
        setAtk(0);

        setActable(false);
    }

    @Override
    public boolean isControllable(Team team) {
        return false;
    }
}
