package lib.display;

import lib.Battle;
import lib.Map;
import lib.Team;
import lib.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattlePanel extends JPanel {
    /** The battle this BattlePanel is displaying. **/
    private Battle battle;

    /** The team that the player is on. **/
    private Team team;

    // CONSTANTS
    public static final double ROW_X_OFFSET = -1.0;
    public static final double ROW_Y_OFFSET = 0.5;
    public static final double COL_X_OFFSET = 1.0;
    public static final double COL_Y_OFFSET = 0.5;
    public static final double HEIGHT_Y_OFFSET = -1.15;
    public static final Color GRID_COLOR = new Color(240, 240, 240);

    // VARIABLES
    // Camera
    private Number cameraRowPos = 4.0;
    private Number cameraColPos = 4.0;
    private Number zoom = 64.0;

    // Cursor
    private int cursorRow = 0;
    private int cursorCol = 0;
    private Number cursorCameraRow = 0;
    private Number cursorCameraCol = 0;

    // Timers
    private final Timer screenRefreshTimer;

    // Constructor
    public BattlePanel(Battle battle) {
        this.battle = battle;
        this.team = battle.getTeam(0);

        // Initialize screen refresh timer
        ActionListener updateScreen = evt -> repaint();
        screenRefreshTimer = new Timer(20, updateScreen);
        screenRefreshTimer.start();

        // Controls
        getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        getActionMap().put("up", new CursorUp());
        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        getActionMap().put("down", new CursorDown());
        getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        getActionMap().put("left", new CursorLeft());
        getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        getActionMap().put("right", new CursorRight());

        // other
        setBackground(new Color(16,16,16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Map map = battle.getMap();
        int numRows = map.numRows();
        int numCols = map.numCols();
        g.setColor(GRID_COLOR);

        // ==== Draw white map grid
        // Draw west and north side of all tiles
        for (int r=0; r<numRows; r++){
            for (int c=0; c<numCols; c++){
                // Draw west side of tile
                g.drawLine(getScreenIntX(r, c), getScreenIntY(r, c),
                        getScreenIntX(r+1, c), getScreenIntY(r+1, c));

                // Draw north side of tile
                g.drawLine(getScreenIntX(r, c), getScreenIntY(r, c),
                        getScreenIntX(r, c+1), getScreenIntY(r, c+1));
            }
        }

        // Draw east borders of easternmost tiles
        for (int r=0; r<numRows; r++){
            g.drawLine(getScreenIntX(r, numCols), getScreenIntY(r, numCols),
                    getScreenIntX(r+1, numCols), getScreenIntY(r+1, numCols));
        }

        // Draw south borders of southernmost tiles
        for (int c=0; c<numRows; c++){
            g.drawLine(getScreenIntX(numRows, c), getScreenIntY(numRows, c),
                    getScreenIntX(numRows, c+1), getScreenIntY(numRows, c+1));
        }

        // ==== Draw all tile bases
        for (int r=0; r<numRows; r++){
            for (int c=0; c<numCols; c++){
                Tile tile = map.getTile(r, c);
                if (tile != null){
                    tile.drawTileBase(g, getScreenX(r, c), getScreenY(r, c), zoom.doubleValue());
                }
            }
        }

        // Draw cursor
        drawInsetTile(g,
                getScreenX(cursorCameraRow.doubleValue(), cursorCameraCol.doubleValue()),
                getScreenY(cursorCameraRow.doubleValue(), cursorCameraCol.doubleValue()),
                zoom.doubleValue(), 0.1, team.getPointColor()
        );

        // ==== Draw all tile features
        for (int r=0; r<numRows; r++){
            for (int c=0; c<numCols; c++){
                Tile tile = map.getTile(r, c);
                if (tile != null){
                    tile.draw(g, getScreenX(r, c), getScreenY(r, c), zoom.doubleValue());
                }
            }
        }
    }

    // ======== DRAW FEATURES (statically)
    /** Draw a tile. **/
    public static void drawTile(Graphics g, double x, double y, double z, Color color, Color fillColor){
        int[] xPoints = {
                (int)x,
                (int)(x + ROW_X_OFFSET*z),
                (int)(x + ROW_X_OFFSET*z + COL_X_OFFSET*z),
                (int)(x + COL_X_OFFSET*z)
        };
        int[] yPoints = {
                (int)y,
                (int)(y + ROW_Y_OFFSET*z),
                (int)(y + ROW_Y_OFFSET*z + COL_Y_OFFSET*z),
                (int)(y + COL_Y_OFFSET*z)
        };

        g.setColor(fillColor);
        g.fillPolygon(xPoints, yPoints, 4);
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, 4);
    }

    /** Draw an inset tile. **/
    public static void drawInsetTile(Graphics g, double x, double y, double z, double inset, Color color, Color fillColor){
        int[] xPoints = {
                (int)(x + ROW_X_OFFSET*z*inset + COL_X_OFFSET*z*inset),
                (int)(x + ROW_X_OFFSET*z - ROW_X_OFFSET*z*inset + COL_X_OFFSET*z*inset),
                (int)(x + ROW_X_OFFSET*z + COL_X_OFFSET*z - ROW_X_OFFSET*z*inset - COL_X_OFFSET*z*inset),
                (int)(x + COL_X_OFFSET*z + ROW_X_OFFSET*z*inset - COL_X_OFFSET*z*inset)
        };
        int[] yPoints = {
                (int)(y + ROW_Y_OFFSET*z*inset + COL_Y_OFFSET*z*inset),
                (int)(y + ROW_Y_OFFSET*z + ROW_Y_OFFSET*z*inset - COL_Y_OFFSET*z*inset),
                (int)(y + ROW_Y_OFFSET*z + COL_Y_OFFSET*z - ROW_Y_OFFSET*z*inset - COL_Y_OFFSET*z*inset),
                (int)(y + COL_Y_OFFSET*z - ROW_Y_OFFSET*z*inset + COL_Y_OFFSET*z*inset)
        };

        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillPolygon(xPoints, yPoints, 4);
        }
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, 4);
    }
    public static void drawInsetTile(Graphics g, double x, double y, double z, double inset, Color color){
        drawInsetTile(g, x, y, z, inset, color, null);
    }

    // ======== POSITIONING
    /** Get the screen X position of a given row and col on the grid. **/
    public double getScreenX(double rowPos, double colPos){
        double relRow = rowPos - cameraRowPos.doubleValue();
        double relCol = colPos - cameraColPos.doubleValue();
        return (relRow*ROW_X_OFFSET + relCol*COL_X_OFFSET)*zoom.doubleValue() + getWidth()/2.0;
    }
    /** Get the screen Y position of a given row and col on the grid. **/
    public double getScreenY(double rowPos, double colPos, double height){
        double relRow = rowPos - cameraRowPos.doubleValue();
        double relCol = colPos - cameraColPos.doubleValue();
        return (relRow*ROW_Y_OFFSET + relCol*COL_Y_OFFSET + height*HEIGHT_Y_OFFSET)*zoom.doubleValue() + getHeight()/2.0;
    }
    public double getScreenY(double rowPos, double colPos){
        return getScreenY(rowPos, colPos, 0);
    }

    public int getScreenIntX(double rowPos, double colPos){
        return (int)getScreenX(rowPos, colPos);
    }
    public int getScreenIntY(double rowPos, double colPos){
        return (int)getScreenY(rowPos, colPos);
    }

    // ======== CONTROL LISTENERS
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

    // ======== CONTROL METHODS
    public void moveCursor(int c, int r){
        if (cursorCol+c >= 0 && cursorCol+c < battle.getMap().numCols()
                && cursorRow+r >= 0 && cursorRow+r < battle.getMap().numCols()){
            cursorCol += c;
            cursorRow += r;
        }
    }
}
