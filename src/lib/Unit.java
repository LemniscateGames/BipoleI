package lib;

import lib.display.AnimatedValue;
import lib.display.TimingFunction;
import lib.display.effects.Effect;
import lib.display.effects.TileShockwave;
import lib.display.shaperendering.ShapeOrtho3D;
import lib.panels.BattlePanel;
import lib.shop.Buyable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/** A unit. **/
public abstract class Unit extends ClaimedTile implements Buyable {
    // ------------ FIELDS
    // ======== Constants
    public static final Color READINESS_COLOR = new Color(240, 240, 240, 200);
    public static final double UNREADY_SATURATION = -0.4;

    // ======== Stats
    /** Value in points of this unit.
     * This is the base price of this unit in shops, unless the price is changed.
     * Can be sold for half this price multiplied by percentage of remaining HP.
     * When destroyed, the attacker gains points equal to 1/3 of this unit's value.
     * Value is increased by the cost of the upgrade when this unit is upgraded. **/
    private int value;
    /** Hit points until defeated. **/
    private int hp;
    /** Maximum hit points. HP cannot go above this. **/
    private int maxHp;
    /** Damage dealt when attacking. **/
    private int atk;
    /** Amount of milliseconds before this unit becomes ready. **/
    private int delay;

    // ======== Configuration
    /** If this unit can attack. **/
    private boolean canAttack = true;
    /** If this unit can be sold. Typically set at initialization with setBuyable(), only really used by castle as of now. **/
    private boolean isSellable = true;
    /** If this unit automatically acts when it is ready. **/
    private boolean autoAct = false;

    // ======== Timing/Readiness
    /** Time (in millis/system time) when this unit began becoming ready. Used in display and for readiness calculation if timers are not used. **/
    private long startTime;
    /** The timer that controls when this unit is ready when on internal timer mode. **/
    private Timer readyTimer;
    /** If this unit is ready to act or not. Set to true when the readiness timer expires. **/
    private boolean isReady;

    // ==== Effects
    private AnimatedValue placeAnimator;

    // ------------ CONSTRUCTORS
    public Unit(){
        initialize();
    }
    public Unit(Team team) {
        super(team);
        initialize();
    }

    // Basically a constructor
    public abstract Unit newUnit(Team team);

    // Stat setup
    /** Called when this Unit is constructed. Initializes necessary stats. **/
    public abstract void initialize();

    public void setValue(int value) {
        this.value = value;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    public void setAtk(int atk) {
        this.atk = atk;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }

    // ------------ METHODS
    // ======== Interaction
    @Override
    public void onPlace(Map map, int r, int c) {
        super.onPlace(map, r, c);
        if (canAttack || autoAct){
            isReady = false;
            setSaturation(UNREADY_SATURATION);

            ActionListener onReadyAction = evt -> onReady();
            readyTimer = new Timer(delay, onReadyAction);
            readyTimer.setRepeats(autoAct);
            startTime = System.currentTimeMillis();
            readyTimer.start();
        }
    }

    // Effects
    /** Place effect to be run when bought from the shop and placed. Looks kinda epic. **/
    public void placeEffect(BattlePanel panel){
        placeAnimator = new AnimatedValue(TimingFunction.EASE_OUT_VERY_FAST, 1000);

        panel.addEffect(new TileShockwave(panel, getRow(), getColumn(), getTeam(),
                placeAnimator, 0.5));
    }

    // ======== Timing/Readiness
    /** Runs when this unit becomes ready. **/
    public void onReady(){
        if (autoAct){
            autoAct();
            startTime = System.currentTimeMillis();
        } else {
            isReady = true;
            setSaturation(0.0);
        }
    }

    // ======== Actions
    /** If this fighter automatically acts, this is ran when auto-acting. Should be overridden by auto actors. **/
    public void autoAct(){

    }

    /** Generate points for this unit's team. Typically used on auto-acting tiles such as farmers and castles. **/
    public void generatePoints(int amount){
        getTeam().addPoints(amount);
    }

    // ======== UI
    @Override
    public void drawUI(Graphics g, double x, double y, double z) {
        if ((canAttack || autoAct) && !isReady){
            BattlePanel.drawBar(g, x, y, z, readinessPercent(), READINESS_COLOR);
        }
    }

    @Override
    public void draw(Graphics g, double x, double y, double z, boolean dim) {
//        setBrightness(dim ? -0.5 : 0.0);
        setSaturation(dim ? -0.5 : 0.0);
        draw(g,x,y,z);
    }

    // ======== Misc
    // Information
    /** Percentage of HP this unit has remaining. **/
    public double hpPercent(){
        return (double)hp/maxHp;
    }
    /** Get the sell value of this unit (value*HP%/2) **/
    public int sellValue(){
        return (int) Math.ceil(value * hpPercent() / 2);
    }
    /** Get the value gained by an enemy when they destroy this unit (value/3). **/
    public int destroyValue(){
        return (int) Math.ceil((double)value / 2);
    }
    /** Get the percentage that this unit is ready. Used in displaying. **/
    public double readinessPercent(){
        return Math.min((double)(System.currentTimeMillis() - startTime) / readyTimer.getDelay(), 1.0);
    }

    /** This unit's default cost if not specified. Needed for shop. **/
    @Override
    public int getCost() {
        return value;
    }

    // ======== Accessors
    public boolean isCanAttack() {
        return canAttack;
    }
    public boolean isSellable() {
        return isSellable;
    }
    public boolean isAutoAct() {
        return autoAct;
    }
    public void setSellable(boolean value){
        isSellable = value;
    }
    public void setCanAttack(boolean value){
        canAttack = value;
    }
    public void setAutoAct(boolean value){
        autoAct = value;
    }
}
