package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ContestedTile implements Tile {
    private static final int CAPTURE_TIME = 6000;

    /** The map this tile is on. **/
    private Map map;
    private int row, col;
    /** The team currently contesting this tile. **/
    private Team team;
    /** The current point value on this tile. **/
    private int pointValue;
    /** The timer that controls the delay for contesting and claiming this tile. **/
    private Timer contestProgressTimer;
    /** Time in milliseconds when this tile began to be contested. **/
    private long contestStartTime;

    public ContestedTile(Team team) {
        this.team = team;
        this.pointValue = 0;

        ActionListener onCaptureAction = evt -> onCapture();
        contestProgressTimer = new Timer(CAPTURE_TIME, onCaptureAction);
    }

    @Override
    public void onPlace(Map map, int row, int col) {
        this.map = map;
        this.row = row;
        this.col = col;

        contestProgressTimer.start();
        contestStartTime = System.currentTimeMillis();
    }

    /** Runs when the contest delay expires and this tile is captured. **/
    public void onCapture(){

    }

    /** Have another team contest for this tile, paying the current point value + 1. **/
    public void contest(Team team){
        // check if tile is already being contested by the same team
        if (this.team == team) return;

        // check if there is an adjacent owned tile, return if not
        if (!map.hasAdjacentOwnedTile(team, row, col)) return;

        // subtracts points if possible and returns true, returns false and does not subtract if can't afford
        if (!team.subtractPoints(pointValue+1)) return;

        this.team = team;
        this.pointValue += 1;
    }

    @Override
    public void drawTileBase(Graphics g, double x, double y, double z) {

    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {

    }

    @Override
    public void drawUI(Graphics g, double x, double y, double z) {

    }

    @Override
    public void onHover() {

    }

    @Override
    public void onUnhover() {

    }

    @Override
    public boolean hasBorder() {
        return false;
    }
}
