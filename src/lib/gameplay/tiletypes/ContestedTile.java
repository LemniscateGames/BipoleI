package lib.gameplay.tiletypes;

import lib.Map;
import lib.Team;
import lib.gameplay.tiletypes.ResponsiveTile;
import lib.panels.BattlePanel;
import lib.ui.ElementBox;
import lib.units.EmptyLand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ContestedTile extends ResponsiveTile {
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
        this.pointValue = 1;

        ActionListener onCaptureAction = evt -> onCapture();
        contestProgressTimer = new Timer(CAPTURE_TIME, onCaptureAction);
    }

    @Override
    public String name() {
        return "-";
    }

    @Override
    public String desc() {
        return "This tile is being contested.";
    }

    @Override
    public String header() {
        return name();
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
        contestProgressTimer.stop();
        map.placeTile(new EmptyLand(team), row, col);
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
        contestProgressTimer.restart();
    }

    @Override
    public void drawBelowGrid(Graphics g, double x, double y, double z) {
        Color lineColor = team.getColor(0, -0.5);
        int SHIFT_SPEED = 1000;
        double hz = z/2;
        double az = (z/10);
        if (az < 1) az = 1;
        long cycle = System.currentTimeMillis()%SHIFT_SPEED;
        int yshift = (int)((double)cycle/SHIFT_SPEED*az);

        g.setColor(lineColor);
        for (double yl = (y + yshift); yl < y + z; yl += az){
            if ((yl-y) < hz){
                g.drawLine((int)(x - (yl-y)*2), (int)yl, (int)(x + (yl-y)*2), (int)yl);
            } else {
                g.drawLine((int)(x - 2*z + (yl-y)*2), (int)yl, (int)(x + 2*z - (yl-y)*2), (int)yl);
            }
        }
    }

    @Override
    public Color getColor() {
        return team.getColor(getBrightness().doubleValue(), getSaturation().doubleValue());
    }

    @Override
    public Color getTileColor(){
        return team.getColor(getGridBrightness().doubleValue(), getGridBrightness().doubleValue());
    }

    @Override
    public Color getUnitColor() {
        return team.getUnitColor();
    }

    public Color getTileFillColor(){
        return team.getTileFillColor(getGridBrightness().doubleValue()*.3, getGridBrightness().doubleValue()*.3);
    }

    @Override
    public void drawUI(Graphics g, double x, double y, double z) {
        BattlePanel.drawBar(g, x, y, z,
                (double)(System.currentTimeMillis()-contestStartTime)/CAPTURE_TIME, team.getColor());

        BattlePanel.drawCenteredString(g,
                new Rectangle((int)(x-z), (int)y, (int)(z*2), (int)z),
                pointValue+"",
                ElementBox.GAME_FONT,
                team.getColor(0.2, 0.0));
    }

    @Override
    public void drawTileBase(Graphics g, double x, double y, double z) {

    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {

    }

    @Override
    public void onHover() {

    }

    @Override
    public void onUnhover() {

    }

    @Override
    public void onMouseHover() {

    }

    @Override
    public void onMouseUnhover() {

    }

    @Override
    public boolean hasBorder() {
        return false;
    }


}
