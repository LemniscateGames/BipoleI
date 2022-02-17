package lib.units;

import lib.Team;
import lib.Unit;

import java.awt.*;

public class Soldier extends Unit {

    public Soldier(Team team) {
        super(team);
    }

    @Override public int getBaseHp() { return 5; }

    @Override public int getBaseAtk() { return 2; }

    @Override public int getBaseDelay() { return 10000; }

    @Override
    public void draw(Graphics g, double x, double y, double z) {

    }
}
