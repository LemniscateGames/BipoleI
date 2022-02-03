package BipoleI;

import BipoleI.lib.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Bipole I's camera uses 2d rendering but displays them in orthographic ways to simulate 3D.
// Maybe an actual 3d bipole game with a 3d engine from scratch later but too confusing for now.

public class BattlePanel extends JPanel {
    // CONSTANTS
    public static final Color NEUTRAL_COLOR = new Color(200,200,200);
    public static final Color UI_BG_COLOR = new Color(8,8,8, 191);
    public static final Color CURSOR_COLOR = new Color(110, 195, 45);
    public static final Color READINESS_COLOR = new Color(255, 255, 255, 200);
    public static final Color BAR_BACKGROUND_COLOR = new Color(30, 30, 30, 200);

    public static final Font GAME_FONT = new Font("monospace", Font.PLAIN, 24);

    /** Currently selected unit by column and row. **/
    private int cursorCol = 0;
    private int cursorRow = 0;

    /** Current displayed X and Y coordinates of cursor. Can be in between tiles during cursor movement. **/
    private float cursorViewCol = 0.0f;
    private float cursorViewRow = 0.0f;

    /** Current camera X and Y position (relative to columns and rows, but can be in between) **/
    private float viewCol = 3.5f;
    private float viewRow = 3.5f;

    /** Current zoom amount (1 tile = 1 square's total bounding box size (not its actual side length)) **/
    private float zoom;
    /** zoom but in pixels. to save calculation. update whenever zoom is changed **/
    private int z;
    /** half of zoom in pixels. update whenever zoom is changed **/
    private int hz;

    /** The battle this panel is currently displaying **/
    private Battle battle;

    /** Screen updater timer that refreshes the screen constantly. **/
    private Timer screenUpdater;

    /** List of floating number (popups) above tiles, for damage, point generation, etc. **/
    private ArrayList<FloatingText> floatingTexts;

    public BattlePanel(){
        zoom = 80.0f;
        z = (int)zoom;   // zoom size in pixels
        hz = (int)(zoom/2);   // half of zoom size in pixels

        getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        getActionMap().put("up", new CursorUp());
        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        getActionMap().put("down", new CursorDown());
        getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        getActionMap().put("left", new CursorLeft());
        getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        getActionMap().put("right", new CursorRight());

        // Screen refresher
        ActionListener updateScreen = evt -> repaint();
        screenUpdater = new Timer(20, updateScreen); screenUpdater.start();

        // initialize floating texts
        floatingTexts = new ArrayList<>();
    }

    // RENDERING
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // cross-hair (testing)
//        g.setColor(NEUTRAL_COLOR);
//        g.drawLine(getWidth()/2 - 4, getHeight()/2, getWidth()/2 + 4, getHeight()/2);
//        g.drawLine(getWidth()/2, getHeight()/2 - 4, getWidth()/2, getHeight()/2 + 4);

        drawMap(g);

        // Draw point counter box at top of screen
        Rectangle pointsBox = new Rectangle(getWidth()/2 - 100, 0, 200, 50);
        g.setColor(UI_BG_COLOR);
        g.fillRect(pointsBox.x, pointsBox.y, pointsBox.width, pointsBox.height);
        g.setColor(NEUTRAL_COLOR);
        g.drawRect(pointsBox.x, pointsBox.y, pointsBox.width, pointsBox.height);
        g.setColor(battle.allies().getPointColor());

        FontMetrics metrics = g.getFontMetrics(GAME_FONT);
        drawCenteredString(g, battle.allies().getPoints() + " pts", pointsBox);
    }

    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        g.drawString(text, x, y);
    }
    public void drawCenteredString(Graphics g, String text, Rectangle rect){
        drawCenteredString(g, text, rect, GAME_FONT);
    }

    public void drawMap(Graphics g){
        if (battle.getMap() == null) return;

        // Draw grid
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                drawTile(g, c, r);
            }
        }

        // Draw grid highlighting for teams (before cursor, so it shows up on top)
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                Tile tile = battle.getMap().getTile(c, r);
                if (tile != null){
                    IntPoint pos = tileScreenPos(c, r);
                    tile.drawGridTile(g, pos.getX(), pos.getY(), z, c==cursorCol && r==cursorRow, tile.isGrayed());
                }
            }
        }

        // Draw the cursor
        drawCursor(g);

        // Draw units (After cursor, so the cursor is displayed below units)
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                Tile tile = battle.getMap().getTile(c, r);
                if (tile != null){
                    IntPoint pos = tileScreenPos(c, r);
                    tile.draw(g, pos.getX(), pos.getY(), z, c==cursorCol && r==cursorRow, tile.isGrayed());
                }
            }
        }

        // Draw UI for units (After unit is drawn, but before floating texts)
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                Tile tile = battle.getMap().getTile(c, r);
                if (tile != null){
                    IntPoint pos = tileScreenPos(c, r);
                    tile.drawUI(g, pos.getX(), pos.getY(), z, c==cursorCol && r==cursorRow, tile.isGrayed());
                }
            }
        }

        // Draw floating texts (damage, gold generation, etc.)
        drawFloatingTexts(g);
    }

    public void drawTile(Graphics g, int c, int r){
        // epic fade
//        g.setColor(new Color(31+32*c, 0, 31+32*r));

        // normal
        g.setColor(NEUTRAL_COLOR);
        IntPoint pos = tileScreenPos(c, r);

        g.drawPolygon(
                new int[]{pos.getX(), pos.getX()-z, pos.getX(), pos.getX()+z},
                new int[]{pos.getY(), pos.getY()+hz, pos.getY()+z, pos.getY()+hz},
                4
        );
    }

    public void drawCursor(Graphics g){
        g.setColor(CURSOR_COLOR);
        FloatPoint pos = tileScreenPos(cursorViewCol, cursorViewRow);

        int fz = z/5;
        int tz = z/10;

        float r = 0.8f;
        g.drawPolygon(
                new int[]{(int)pos.getX(), (int)pos.getX()-z+fz, (int)pos.getX(), (int)pos.getX()+z-fz},
                new int[]{(int)pos.getY()+tz, (int)pos.getY()+hz, (int)pos.getY()+z-tz, (int)pos.getY()+hz},
                4
        );
    }

    // ================ UNIT OVERLAY STATS


    // ================ FLOATING TEXT
    public void addFloatingText(FloatingText floatingText){
        floatingTexts.add(floatingText);
    }

    /** Draw floating texts, and also delete expired ones. **/
    public void drawFloatingTexts(Graphics g){
        int i = 0;
        while (i<floatingTexts.size()){
            FloatingText floatingText = floatingTexts.get(i);
            if (floatingText.expired()){
                floatingTexts.remove(i);
            } else {
                floatingText.draw(this, g, z);
                i++;
            }
        }
    }

    // ================ POSITIONING
    /** Get the screen position of the upper-left square of the specified tile **/
    public IntPoint tileScreenPos(int c, int r){
        return new IntPoint(
                (int)((c-viewCol + viewRow)*zoom + getWidth()/2) - z*r,
                (int)((r-viewRow - viewCol/2 + viewRow/2)*zoom + getHeight()/2) + hz*c - hz*r
        );
    }
    public FloatPoint tileScreenPos(float c, float r){
        return new FloatPoint(
                (int)((c-viewCol + viewRow)*zoom + getWidth()/2) - z*r,
                (int)((r-viewRow - viewCol/2 + viewRow/2)*zoom + getHeight()/2) + hz*c - hz*r
        );
    }


    // ================ SHAPE DRAWING
    public static void drawRectPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height,
                                     boolean brighter, boolean grayed, boolean hideBottomFace){
        int hz = z/2;
        int hw = width/2;
        int hl = length/2;
        int qw = width/4;
        int ql = length/4;

        y += hz; // center rectangle on tile

        int[] xPoints = new int[]{x+hw-hl, x-hw-hl, x-hw-hl, x-hw+hl, x+hw+hl, x+hw+hl};
        int[] yPoints = new int[]{y+qw+ql, y-qw+ql, y-qw+ql-height,
                y-qw-ql-height, y+qw-ql-height, y+qw-ql};

        g.setColor(team.getUnitColor(brighter));
        g.fillPolygon(xPoints, yPoints, 6);

        g.setColor(team.getColor(brighter, grayed));
        g.drawPolygon(xPoints, yPoints, 6);

        if (hideBottomFace){
            // Cut off bottom segments and redraw side segments
            g.setColor(team.getUnitColor(brighter));
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            g.drawLine(xPoints[0], yPoints[0], xPoints[5], yPoints[5]);
            g.setColor(team.getColor(brighter, grayed));
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
            g.drawLine(xPoints[4], yPoints[4], xPoints[5], yPoints[5]);
        }

        // Connect corners to center
        for (int i=0; i<6; i += 2){
            g.drawLine(x+hw-hl, y+qw+ql-height, xPoints[i], yPoints[i]);
        }
    }
    public static void drawRectPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height, boolean brighter,  boolean grayed){
        drawRectPrism(g, x, y, z, team, width, length, height, brighter, grayed,false);
    }

    public static void drawWidthRect(Graphics g, int x, int y, int z, Team team,
                                     int width, int height, boolean brighter, boolean grayed){
        int hz = z/2;
        int hw = width/2;
        int qw = width/4;

        y += hz;

        int[] xPoints = {x-hw, x+hw, x+hw, x-hw};
        int[] yPoints = {y+qw, y-qw, y-qw-height, y+qw-height};

        g.setColor(team.getUnitColor(brighter));
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(team.getColor(brighter, grayed));
        g.drawPolygon(xPoints, yPoints, 4);
    }

    public static void drawLengthRect(Graphics g, int x, int y, int z, Team team, int length, int height, boolean brighter, boolean grayed){
        int hz = z/2;
        int hl = length/2;
        int ql = length/4;

        y += hz;

        int[] xPoints = {x-hl, x+hl, x+hl, x-hl};
        int[] yPoints = {y-ql, y+ql, y+ql-height, y-ql-height};

        g.setColor(team.getUnitColor(brighter));
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(team.getColor(brighter, grayed));
        g.drawPolygon(xPoints, yPoints, 4);
    }

    public static void drawTriangularPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height,
                                           boolean brighter, boolean grayed, boolean hideBottomFace){
        int hz = z/2;
        int hw = width/2;
        int hl = length/2;
        int qw = width/4;
        int ql = length/4;

        y += hz; // center rectangle on tile

        int[] xPoints = new int[]{x+hw-hl, x-hw-hl, x, x+hw+hl};
        int[] yPoints = new int[]{y+qw+ql, y-qw+ql, y-height, y+qw-ql};

        g.setColor(team.getUnitColor(brighter));
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(team.getColor(brighter, grayed));
        g.drawPolygon(xPoints, yPoints, 4);

        if (hideBottomFace){
            // Cut off bottom segments and redraw top segments
            g.setColor(team.getUnitColor(brighter));
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            g.drawLine(xPoints[0], yPoints[0], xPoints[3], yPoints[3]);
            g.setColor(team.getColor(brighter, grayed));
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
            g.drawLine(xPoints[3], yPoints[3], xPoints[2], yPoints[2]);
        }

        // Connect front corner to top corner
        g.drawLine(xPoints[0], yPoints[0], xPoints[2], yPoints[2]);
    }
    public static void drawTriangularPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height, boolean brighter, boolean grayed){
        drawTriangularPrism(g, x, y, z, team, width, length, height, brighter, grayed, false);
    }

    // CURSOR
    public void moveCursor(int c, int r){
        if (cursorCol+c >= 0 && cursorCol+c < battle.getMap().numCols()
                && cursorRow+r >= 0 && cursorRow+r < battle.getMap().numCols()){
            cursorCol += c;
            cursorRow += r;
            cursorViewCol += c;
            cursorViewRow += r;

            Tile selectedTile = battle.getMap().getTile(cursorCol, cursorRow);
            if (selectedTile instanceof Unit) {
                Unit unit = (Unit)selectedTile;
                // Update labels
                System.out.println(unit.name());
            }
        }


    }

    // CONTROLS
    public class CursorUp extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(0, -1);
        }
    }
    public class CursorDown extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(0, 1);
        }
    }
    public class CursorLeft extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(-1, 0);
        }
    }
    public class CursorRight extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(1, 0);
        }
    }

    // ================ ACCESSORS & SETTERS
    public void setBattle(Battle battle){
        this.battle = battle;
        battle.setPanel(this);
    }

    public int getCursorCol() {
        return cursorCol;
    }

    public int getCursorRow() {
        return cursorRow;
    }
}

