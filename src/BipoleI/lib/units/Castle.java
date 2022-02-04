package BipoleI.lib.units;

import BipoleI.BattlePanel;
import BipoleI.lib.Map;
import BipoleI.lib.Team;
import BipoleI.lib.Unit;

import java.awt.*;

/** The player's main unit. Immovable, and if destroyed the player loses. **/
public class Castle extends Unit {
    public String name() { return "Castle"; }

    public Castle(Map map, Team team) {
        super(map, team,0, 25, 0, 5000, false, true);
    }
    public Castle(){}

    @Override
    public void autoAct() {
        generatePoints(1);
    }

    @Override
    public void draw(Graphics g, int x, int y, int z) {
        // castle main box dimensions
        int width = (int)(z*0.7);
        int length = (int)(z*0.7);
        int height = (int)(z*0.3);

        // side tower spacing & location
        int s = (int)(z*0.5);   // offset of top stones center from center of castle
        int hs = (int)(z*0.25);  // half of stone offset
        int sx = x;
        int sy = y-height;

        // side tower dimensions
        int swidth = (int)(z*0.2);
        int slength = (int)(z*0.2);
        int sheight = (int)(z*0.15);

        // center tower dimensions
        int cwidth = (int)(z*0.2);
        int clength = (int)(z*0.2);
        int cheight = (int)(z*0.2);
        int ctheight = (int)(z*0.25);

        // door dimensions
        int dwidth = (int)(z*0.15);
        int dheight = (int)(z*0.2);

        BattlePanel.drawRectPrism(g, x, y, z, getTeam(), width, length, height, getBrightness(), getGrayness());
//        int[] xPoints = new int[]{sx, sx-s, sx, sx+s};
//        int[] yPoints = new int[]{sy+hs, sy, sy-hs, szy};

        int[] xPoints = new int[]{sx, sx-s, sx+s, sx};
        int[] yPoints = new int[]{sy-hs, sy, sy, sy+hs};

        // Backmost side tower, draw before center tower
        BattlePanel.drawRectPrism(g, xPoints[0], yPoints[0], z, getTeam(), swidth, slength, sheight, getBrightness(), getGrayness());

        // Center tower
        BattlePanel.drawRectPrism(g, sx, sy, z, getTeam(),
                cwidth, clength, cheight, getBrightness(), getGrayness(), false);
        // Triangle above center tower
        BattlePanel.drawTriangularPrism(g, sx, sy-cheight, z, getTeam(),
                cwidth, clength, ctheight, getBrightness(), getGrayness(), true);

        // Frontmost 3 side towers
        for (int i=1; i<4; i++){
            BattlePanel.drawRectPrism(g, xPoints[i], yPoints[i], z, getTeam(), swidth, slength, sheight, getBrightness(), getGrayness(), true);
        }

        // Doors on left and right side
        BattlePanel.drawWidthRect(g, x+width/2, y+length/4, z, getTeam(), dwidth, dheight, getBrightness(), getGrayness());
        BattlePanel.drawLengthRect(g, x-width/2, y+length/4, z, getTeam(), dwidth, dheight, getBrightness(), getGrayness());
    }
}
