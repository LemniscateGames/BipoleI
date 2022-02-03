package BipoleI.lib;

import BipoleI.BattlePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Unit implements Tile {
    /** THis unit's display name. **/
    public abstract String name();

    /** The map that this unit is on. Used to get coordinates from the map 2D array,
     * which are not stored in this object for some reason. **/
    private final Map map;

    /** Team this tile belongs to. **/
    private final Team team;

    /** Cost in points to purchase this unit. **/
    private int cost;

    /** Hit points before destroyed. (Empty tiles have 1 hp.) **/
    private int hp;

    /** Amount of hit points subtracted from enemy when attacking. **/
    private int atk;

    /** Amount of milliseconds before this unit can act again after attacking/activating or being constructed. **/
    private int delay;

    /** Whether or not this unit can attack. **/
    private boolean canAttack;

    /** If true, this unit automatically acts when it is ready. **/
    private boolean autoAct;

    /** The BattlePanel that this Unit is on the map of.
     * If null, then some display stuff wont be shown but the unit can still exist without being in a panel.
     */
    private BattlePanel panel;

    /** The timer that controls when this unit is ready to act. **/
    private Timer readinessTimer;

    /** Milliseconds since this unit started to become ready. (Only really used in displaying) **/
    private long readyStartMillis;

    /** Whether or not this unit is currently ready to attack/act. Set to true when readinessTimer is up. **/
    private boolean ready;

    public Unit(Map map, Team team, int cost, int hp, int atk, int delay, boolean canAttack, boolean autoAct){
        this.map = map;
        this.team = team;
        this.cost = cost;
        this.hp = hp;
        this.atk = atk;
        this.delay = delay;
        this.canAttack = canAttack;
        this.autoAct = autoAct;
    }
    public Unit(Map map, Team team, int cost, int hp, int atk, int delay, boolean canAttack){
        this(map, team, cost, hp, atk, delay, canAttack, false);
    }
    public Unit(Map map, Team team, int cost, int hp, int atk, int delay){
        this(map, team, cost, hp, atk, delay, true, false);
    }
    public Unit(Map map, Team team, int cost, int hp, int atk){
        this(map, team, cost, hp, atk, 0, false, false);
    }
    public Unit(Map map, Team team, int cost, int hp){
        this(map, team, cost, hp, 0, 0, false, false);
    }
    public Unit(Map map, Team team) {
        this.team = team;
        this.map = map;
    }
    // this constructor is ONLY used for the shop items
    public Unit(){
        this(null, null);
    }

    // ================ TIMING
    /** Run when this unit is bought/placed. Starts the readiness timer. **/
    public void place(){
        // Start readiness timer if this unit can attack or auto act.
        if (canAttack || autoAct){
            ActionListener onReadyAction = evt -> onReady();
            readinessTimer = new Timer(delay, onReadyAction);
            readinessTimer.setRepeats(autoAct);
            readyStartMillis = System.currentTimeMillis();
            startReadinessTimer();
        }
    }

    /** Start the readiness timer to become ready. Ran when unit is placed and after unit attacks/acts. **/
    public void startReadinessTimer(){
        readinessTimer.start();
    }

    /** Runs when readinessTimer is up and this unit becomes ready. Changes the ready boolean to true,
     * or auto-acts if this unit is an auto-actor like a farmer.
     */
    public void onReady(){
        if (autoAct){
            autoAct();
            readyStartMillis = System.currentTimeMillis();
        } else {
            ready = true;
        }
    }

    /** If this unit has an auto-act, this is what is run when it auto acts. **/
    public void autoAct(){}

    /** Percentage that this unit is ready. **/
    public double readinessPercent(){
        if (readinessTimer == null) return 1.0;
        return (double)(System.currentTimeMillis() - readyStartMillis) / readinessTimer.getDelay();
    }

    // ================ RENDERING
    public abstract void draw(Graphics g, int x, int y, int z, boolean brighter, boolean grayed);

    public void drawUI(Graphics g, int x, int y, int z, boolean brighter, boolean grayed){
        y += (int)(z*0.75);
        if (readinessTimer != null){
            if (readinessTimer.isRunning()){
                int width = z;
                int height = (int)(z*0.1);
                int hw = width/2;
                int hh = height/2;
                int barWidth = (int)(z*readinessPercent());

                g.setColor(BattlePanel.BAR_BG_COLOR);
                g.fillRect(x-hw, y-hh, width, height);

                g.setColor(BattlePanel.READINESS_COLOR);
                g.fillRect(x-hw, y-hh, barWidth, height);

                g.setColor(BattlePanel.BAR_BORDER_COLOR);
                g.drawRect(x-hw, y-hh, width, height);
            }
        }
    }

    @Override
    public void drawGridTile(Graphics g, int x, int y, int z, boolean brighter, boolean grayed) {
        int hz = (int)((float)z/2);

        int[] xPoints = new int[]{x, x-z, x, x+z};
        int[] yPoints = new int[]{y, y+hz, y+z, y+hz};

        Color teamColor = team.getColor(brighter, false);
        Color transparentColor = new Color(
                teamColor.getRed(),
                teamColor.getGreen(),
                teamColor.getBlue(),
                64
        );

        if (brighter) {
            teamColor = teamColor.brighter();
            transparentColor = transparentColor.brighter();
        }

        g.setColor(transparentColor);
        g.fillPolygon(xPoints, yPoints, 4);
        g.setColor(teamColor);
        g.drawPolygon(xPoints, yPoints, 4);
    }

    public boolean isGrayed(){
        return (autoAct
                || readinessTimer != null
                && (System.currentTimeMillis() < readyStartMillis+readinessTimer.getDelay()));
    }

    // ================ GENERAL
    /** This tile generates points. Creates a popup if a BattlePanel panel is defined for this unit via setPanel(). **/
    public void generatePoints(int amount){
        team.addPoints(amount);
        floatingTextHere("+"+amount, team.getPointColor());
    }

    /** Get the X and Y coordinates of this unit on the map it is on. Null if not on map. **/
    public IntPoint getMapPosition(){
        for (int c=0; c<map.numCols(); c++){
            for (int r=0; r<map.numRows(); r++){
                if (map.getTile(c, r) == this){
                    return new IntPoint(c, r);
                }
            }
        }
        return null;
    }

    // ================ FLOATING TEXT
    /** Summon  a floating text on this unit. **/
    public void floatingTextHere(String text, Color color, int duration){
        if (panel == null) return;
        IntPoint pos = getMapPosition();
        if (pos == null) return;

        FloatingText floatingText = new FloatingText(pos.getX(), pos.getY(), text, color, duration);
        panel.addFloatingText(floatingText);
    }
    public void floatingTextHere(String text, Color color){
        floatingTextHere(text, color, 1000);
    }

    // ================ ACCESSORS & SETTERS

    public Map getMap() {
        return map;
    }

    public Team getTeam() {
        return team;
    }

    public int getCost() {
        return cost;
    }

    public int getHp() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public boolean isAutoAct() {
        return autoAct;
    }

    public BattlePanel getPanel() {
        return panel;
    }

    public void setPanel(BattlePanel panel) {
        this.panel = panel;
    }
}
