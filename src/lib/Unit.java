package lib;

/** A unit. **/
public abstract class Unit implements Tile {
    /** Hit points until defeated. **/
    public abstract int getBaseHp();
    /** Damage dealt to units when attacking. **/
    public abstract int getBaseAtk();
    /** Milliseconds until this unit becomes ready. **/
    public abstract int getBaseDelay();

    @Override
    public boolean hasBorder() {
        return true;
    }
}
