package BipoleI.lib;

import BipoleI.BattlePanel;
import BipoleI.lib.units.ClaimedTile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UnclaimedTile extends Tile {
    private Map map;
    private boolean beingContested;
    private Team currentContestingTeam;
    private Timer contestTimer;
    private long contestStartTime;
    private int contestPoints;
    private static final int CAPTURE_TIME = 6000;

    /** The map is needed so that this tile can be replaced on the map once it is captured. **/
    public UnclaimedTile(Map map){
        this.map = map;
    }

    /** Contest to capture this tile.
     * Costs 1 more than its current contested value to contest, like a bidding.
     * Takes 8s to capture, during which any opponent can contest this tile for their own. **/
    public void contest(Team team){
        if (team.getPoints() < contestPoints+1) return;
        if (currentContestingTeam == team) return;
        if (!map.isAdjacentOwnedTile(this, team)) return;

        System.out.printf("%s is contesting %s%n", team, this);

        beingContested = true;
        currentContestingTeam = team;
        contestPoints++;
        team.subtractPoints(contestPoints);
        if (contestTimer == null){
            ActionListener onCaptureAction = evt -> onCapture();
            contestTimer = new Timer(CAPTURE_TIME, onCaptureAction);
            contestTimer.start();
            contestStartTime = System.currentTimeMillis();
        } else {
            contestTimer.restart();
        }
    }

    public void onCapture(){
        contestTimer.stop();
        // Go through the map and replace this with a new EmptyTile for the winning team
        map.replaceTile(this, new ClaimedTile(map, currentContestingTeam));
    }

    @Override
    public void drawGridTile(Graphics g, int x, int y, int z) {
        if (beingContested){
            Color lineColor = currentContestingTeam.getColor(0, 0.5);
            int SHIFT_SPEED = 1000;
            int hz = z/2;
            int az = (z/10);
            if (az < 1) az = 1;
            long cycle = System.currentTimeMillis()%SHIFT_SPEED;
            int yshift = (int)((double)cycle/SHIFT_SPEED*az);

            g.setColor(lineColor);
            for (int yl = y + yshift; yl < y + z; yl += az){
                if ((yl-y) < hz){
                    g.drawLine(x - (yl-y)*2, yl, x + (yl-y)*2, yl);
                } else {
                    g.drawLine(x - 2*z + (yl-y)*2, yl, x + 2*z - (yl-y)*2, yl);
                }
            }
        }
    }
    @Override
    public void draw(Graphics g, int x, int y, int z) {

    }
    @Override
    public void drawUI(Graphics g, int x, int y, int z) {
        if (beingContested){
            BattlePanel.drawBar(g, x, y, z,
                    (double)(System.currentTimeMillis()-contestStartTime)/CAPTURE_TIME, currentContestingTeam.getColor());

            BattlePanel.drawCenteredString(g, new Rectangle(x-z, y, z*2, z),
                    contestPoints+"", BattlePanel.GAME_FONT, currentContestingTeam.getColor(0.2));
        }
    }
    @Override
    public boolean canAct() {
        return false;
    }
}
