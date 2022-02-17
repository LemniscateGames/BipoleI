package lib;

import java.awt.*;

/** A unit. **/
public abstract class Unit extends ClaimedTile {
    public Unit(Team team) {
        super(team);
    }

    /** Hit points until defeated. **/
    public abstract int getBaseHp();
    /** Damage dealt to units when attacking. **/
    public abstract int getBaseAtk();
    /** Milliseconds until this unit becomes ready. **/
    public abstract int getBaseDelay();

    @Override
    public void drawTileBase(Graphics g, double x, double y, double z) {

    }
}
