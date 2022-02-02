package BipoleI.lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Unit implements Tile {
    /** THis unit's display name. **/
    public abstract String name();

    /** Team ID this tile belongs to. 0 = ally owned, 1+ - enemy team or teams, **/
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

    /** The timer that controls when this unit is ready to act. **/
    private Timer readinessTimer;

    /** Whether or not this unit is currently ready to attack/act. Set to true when readinessTimer is up. **/
    private boolean ready;

    public Unit(Team team, int cost, int hp, int atk, int delay, boolean canAttack, boolean autoAct){
        this.team = team;
        this.cost = cost;
        this.hp = hp;
        this.atk = atk;
        this.delay = delay;
        this.canAttack = canAttack;
        this.autoAct = autoAct;
    }
    public Unit(Team team, int cost, int hp, int atk, int delay, boolean canAttack){
        this(team, cost, hp, atk, delay, canAttack, false);
    }
    public Unit(Team team, int cost, int hp, int atk, int delay){
        this(team, cost, hp, atk, delay, true, false);
    }
    public Unit(Team team, int cost, int hp, int atk){
        this(team, cost, hp, atk, 0, false, false);
    }
    public Unit(Team team, int cost, int hp){
        this(team, cost, hp, 0, 0, false, false);
    }

    // General
    /** Run when this unit is bought/placed. Starts the readiness timer. **/
    public void place(){
        // Start readiness timer if this unit can attack or auto act.
        if (canAttack || autoAct){
            ActionListener onReadyAction = evt -> onReady();
            readinessTimer = new Timer(delay, onReadyAction);
            readinessTimer.setRepeats(autoAct);
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
        } else {
            ready = true;
        }
        System.out.println(this.name() + " is ready!");
    }

    /** If this unit has an auto-act, this is what is run when it auto acts. **/
    public void autoAct(){}

    // Display
    public abstract void draw(Graphics g, int x, int y, int z, boolean brighter);

    @Override
    public void drawGridTile(Graphics g, int x, int y, int z, boolean brighter) {
        int hz = (int)((float)z/2);

        int[] xPoints = new int[]{x, x-z, x, x+z};
        int[] yPoints = new int[]{y, y+hz, y+z, y+hz};

        Color teamColor = team.getColor(brighter);
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

    public Team getTeam() {
        return team;
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
}
