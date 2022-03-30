package lib;

import lib.display.AnimatedValue;
import lib.display.TimingFunction;
import lib.display.effects.FloatingText;
import lib.display.effects.TileShockwave;
import lib.gameplay.tiletypes.ClaimedTile;
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
    /** Amount of tiles away this unit can attack. **/
    private int range = 1;
    /** Amount of tiles this unit can move. **/
    private int speed = 1;

    // ======== Configuration
    /** If this unit can attack/move (act). **/
    private boolean actable = true;
    /** If this unit can be sold. Typically set at initialization with setBuyable(), only really used by castle as of now. **/
    private boolean sellable = true;
    /** If this unit automatically acts when it is ready. **/
    private boolean autoAct = false;
    /** If this unnit can only move on claimed tiles. **/
    private boolean claimedLandOnly = false;
    /** If this unit can move off allied land, this is the team of the land that it is on. **/
    private Team landTeam;

    // ======== Timing/Readiness
    /** Time (in millis/system time) when this unit began becoming ready. Used in display and for readiness calculation if timers are not used. **/
    private long startTime;
    /** The timer that controls when this unit is ready when on internal timer mode. **/
    private Timer readyTimer;
    /** If this unit is ready to act or not. Set to true when the readiness timer expires. **/
    private boolean ready;

    /** The panel that this unit was placed on. Saved when place effect is called, saved to send FloatingText effects to. **/
    private BattlePanel panel;

    // ==== Effects
    private AnimatedValue placeAnimator;

    // ------------ CONSTRUCTORS
    public Unit(){
        initialize();
    }
    public Unit(Team team) {
        super(team);
        setLandTeam(team);
        initialize();
    }

    // Basically a constructor
    public abstract Unit newUnit(Team team);

    // Stat setup
    /** Called when this Unit is constructed. Initializes necessary stats. **/
    public abstract void initialize();

    @Override
    public String header() {
        return String.format("%s %d/%d", name(), atk, hp);
    }

    // ------------ METHODS
    // ======== Interaction
    @Override
    public void onPlace(Map map, int r, int c) {
        super.onPlace(map, r, c);
        if (actable || autoAct){
            ready = false;
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
        this.panel = panel;
        placeAnimator = new AnimatedValue(TimingFunction.EASE_OUT_FAST, 1000);

        panel.addEffect(new TileShockwave(panel, getRow(), getColumn(), getTeam(),
                placeAnimator, 0.5));
    }

    // Effects
    /** Place effect to be run when bought from the shop and placed. Looks kinda epic. **/
    public void sellEffect(BattlePanel panel){
        this.panel = panel;
        placeAnimator = new AnimatedValue(TimingFunction.EASE_OUT_FAST, 1000);

        panel.addEffect(new TileShockwave(panel, getRow(), getColumn(), getTeam().getColor(),
                placeAnimator, 0.375));
    }

    // ======== Timing/Readiness
    /** Runs when this unit becomes ready. **/
    public void onReady(){
        if (autoAct){
            autoAct();
            startTime = System.currentTimeMillis();
        } else {
            ready = true;
            setSaturation(0.0);
        }
    }

    /** Restart the timer for becoming ready. **/
    public void restartActionCooldown(){
        ready = false;
        startTime = System.currentTimeMillis();
        readyTimer.restart();
    }

    public boolean canMove(){
        return isReady();
    }

    // ======== Actions
    /** If this fighter automatically acts, this is ran when auto-acting. Should be overridden by auto actors. **/
    public void autoAct(){

    }

    /** Generate points for this unit's team. Typically used on auto-acting tiles such as farmers and castles. **/
    public void generatePoints(int amount){
        getTeam().addPoints(amount);
        if (panel != null){
            panel.addEffect(new FloatingText(panel, getRow(), getColumn(),
                    "+"+amount, getTeam().getPointColor(), 0.5));
        }
    }

    // ======== UI
    @Override
    public void drawUI(Graphics g, double x, double y, double z) {
        if ((actable || autoAct) && !ready){
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
        return (double)hp / maxHp;
    }
    /** Get the sell value of this unit (value * HP% / 2) **/
    public int sellValue(){
        return (int) Math.ceil(value * hpPercent() / 2);
    }
    /** Get the value gained by an enemy when they destroy this unit (value / 2). **/
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

    /** Controllable if this unit's team is the same. **/
    @Override
    public boolean isControllable(Team team) {
        return team == getTeam();
    }

    // ======== Accessors
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.maxHp = hp;
        this.hp = hp;
    }
    public int getMaxHp() {
        return maxHp;
    }
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    public int getAtk() {
        return atk;
    }
    public void setAtk(int atk) {
        this.atk = atk;
    }
    public int getDelay() {
        return delay;
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getRange() {
        return range;
    }
    public void setRange(int range) {
        this.range = range;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public boolean isActable() {
        return actable;
    }
    public boolean isSellable() {
        return sellable;
    }
    public boolean isAutoAct() {
        return autoAct;
    }
    public void setSellable(boolean value){
        sellable = value;
    }
    public void setActable(boolean value){
        actable = value;
    }
    public void setAutoAct(boolean value){
        autoAct = value;
    }
    public boolean isClaimedLandOnly() {
        return claimedLandOnly;
    }
    public void setClaimedLandOnly(boolean claimedLandOnly) {
        this.claimedLandOnly = claimedLandOnly;
    }
    public Team getLandTeam() {
        return landTeam;
    }
    public void setLandTeam(Team landTeam) {
        this.landTeam = landTeam;
        setDisplayBaseTeam(landTeam);
    }

    public boolean isReady() {
        return ready;
    }
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public void setPanel(BattlePanel panel) {
        this.panel = panel;
    }
}
