package lib;

import lib.display.shaperendering.ShapeOrtho3D;

import java.awt.*;

/** A unit. **/
public abstract class Unit extends ClaimedTile {
    /** Hit points until defeated. **/
    private int hp;
    /** Damage dealt when attacking. **/
    private int atk;
    /** Amount of milliseconds before this unit becomes ready. **/
    private int delay;

    public Unit(Team team, int hp, int atk, int delay, ShapeOrtho3D... shapes) {
        super(team, shapes);
        this.hp = hp;
        this.atk = atk;
        this.delay = delay;
    }
}
