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
    // CONFIG
    private final boolean CAMERA_FOLLOW_CURSOR = false;
    private final boolean EASE_CURSOR = true;
    private final int CURSOR_SPEED = 125;   // Number of milliseconds for cursor to move

    // CONSTANTS
    public static final Color GRAY_COLOR = new Color(80, 80, 80);
    public static final Color LIGHT_COLOR = new Color(255, 255, 235);
    public static final Color NEUTRAL_COLOR = new Color(200,200,200);
    public static final Color UI_FG_COLOR = new Color(220, 220, 220, 220);
    public static final Color UI_BG_COLOR = new Color(8,8,8, 200);
    public static final Color UI_BORDER_COLOR = new Color(220, 220, 220);
    public static final Color UI_BORDER_COLOR_GRAYED = blendColors(UI_BORDER_COLOR, GRAY_COLOR, 0.7);
    public static final Color CURSOR_COLOR = new Color(110, 195, 45);
    public static final Color READINESS_COLOR = new Color(255, 255, 255, 200);
    public static final Color BAR_BG_COLOR = new Color(30, 30, 30, 200);
    public static final Color BAR_BORDER_COLOR = new Color(8,8,8);

    public static final Font GAME_FONT = new Font("monospace", Font.PLAIN, 24);
    public static final Font GAME_FONT_SMALL = new Font("monospace", Font.PLAIN, 14);

    public enum CursorMode {MAP_CURSOR, SHOP_CURSOR}

    /** The current control mode. **/
    private CursorMode cursorMode = CursorMode.MAP_CURSOR;

    /** Currently selected map unit by column and row. **/
    private int cursorCol = 0;
    private int cursorRow = 0;

    /** Row and column of shop item selected. **/
    private int shopCursorRow = 0;
    private int shopCursorCol = 0;

    /** Current displayed X and Y coordinates of cursor. Can be in between tiles during cursor movement. **/
    private Number cursorViewCol = 0.0f;
    private Number cursorViewRow = 0.0f;

    /** Current camera X and Y position (relative to columns and rows, but can be in between) **/
    private Number viewCol = 3.5f;
    private Number viewRow = 3.5f;

    /** Current zoom amount (1 tile = 1 square's total bounding box size (not its actual side length)) **/
    private float zoom;
    /** zoom but in pixels. to save calculation. update whenever zoom is changed **/
    private int z;
    /** half of zoom in pixels. update whenever zoom is changed **/
    private int hz;

    /** The battle this panel is currently displaying **/
    private Battle battle;

    /** Screen updater timer that refreshes the screen constantly. **/
    private final Timer screenUpdater;

    /** List of floating number (popups) above tiles, for damage, point generation, etc. **/
    private final ArrayList<FloatingText> floatingTexts;

    public BattlePanel(){
        zoom = 64.0f;
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

        getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "space");
        getActionMap().put("space", new KeySpace());
        getInputMap().put(KeyStroke.getKeyStroke("Z"), "z");
        getActionMap().put("z", new KeyZ());

        // Screen refresher
        ActionListener updateScreen = evt -> repaint();
        screenUpdater = new Timer(20, updateScreen); screenUpdater.start();

        // initialize floating texts
        floatingTexts = new ArrayList<>();

        // camera init
        if (CAMERA_FOLLOW_CURSOR){
            viewCol = cursorCol;
            viewRow = cursorRow;
        }
    }

    // ==== MAIN RENDER METHOD
    // Constants
    private static final int SHOP_TOP_PAD = 48;
    private static final int SHOP_ITEM_MARGIN = 8;
    private static final Number SHOP_ITEM_WIDTH = 64;
    private static final Number SHOP_ITEM_HEIGHT = 72;
    private static final Number SHOP_WIDTH = (SHOP_ITEM_WIDTH.intValue() + 2*SHOP_ITEM_MARGIN)*2;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        // == Draw all tiles and their associated UI over their tiles
        drawMap(g);

        // == Draw screenspace UI (points, shop sidebar, etc)
        // Point counter box at top center of screen
        drawBox(g, new Rectangle(getWidth()/2 - 100, 0, 200, 50),
                battle.allies().getPoints() + " pts", GAME_FONT, battle.allies().getPointColor());

        // - Shop at top-right
        Shop shop = battle.allies().getShop();
        int shopRows = shop.getItems().size() - shop.getItems().size()/2; // Int division but rounded up
        int shopHeight = (SHOP_ITEM_HEIGHT.intValue() + 2*SHOP_ITEM_MARGIN)*shopRows + SHOP_TOP_PAD;
        drawBox(g, new Rectangle(getWidth() - SHOP_WIDTH.intValue(), 0, SHOP_WIDTH.intValue(), shopHeight));
        for (int i=shop.getItems().size()-1; i>=0; i--){
            Buyable item = shop.getItems().get(i);
            boolean canAfford = item.isBuyable(battle.allies());
            boolean itemSelected = cursorMode == CursorMode.SHOP_CURSOR && shopCursorCol == i%2 && shopCursorRow == i/2;

            int x = getWidth() - SHOP_WIDTH.intValue() + ((SHOP_ITEM_MARGIN*2 + SHOP_ITEM_WIDTH.intValue())*(i%2)) + SHOP_ITEM_MARGIN;
            int y = (SHOP_ITEM_MARGIN*2 + SHOP_ITEM_HEIGHT.intValue())*(i/2) + SHOP_TOP_PAD + SHOP_ITEM_MARGIN;
            Rectangle itemRect = new Rectangle(x, y, SHOP_ITEM_WIDTH.intValue(), SHOP_ITEM_HEIGHT.intValue());
            Color borderColor = itemSelected ?
                    (canAfford ? battle.allies().getPointColor() : battle.allies().getBrokeColor()) :
                    (canAfford ? UI_BORDER_COLOR : UI_BORDER_COLOR_GRAYED);
            drawBox(g, itemRect, UI_BG_COLOR, borderColor);

            // Draw the unit of the Buyable if it is a ShopUnit
            if (item instanceof ShopUnit){
                Unit unit = ((ShopUnit)item).getUnit();

                int x1 = x + SHOP_ITEM_WIDTH.intValue()/2;
                int y1 = y + SHOP_ITEM_WIDTH.intValue()/2 - 24;

                unit.setBrightness(canAfford ? 0.0 : 0.25);
                unit.setGrayness(canAfford ? 0.0 : 0.75);
                unit.draw(g, x1, y1, 60);
                Rectangle itemCostRect = new Rectangle(itemRect.x, itemRect.y+52, itemRect.width, itemRect.height-52);
                drawCenteredString(g, itemCostRect,item.getCost() + " pts", GAME_FONT_SMALL,
                        canAfford ? battle.allies().getPointColor() : battle.allies().getBrokeColor());
            }
        }
    }

    // RENDERING
    public static void drawBox(Graphics g, Rectangle rect, Color bg, Color border){
        g.setColor(bg);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(border);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
    public static void drawBox(Graphics g, Rectangle rect, Color bg){
        drawBox(g, rect, bg, UI_BORDER_COLOR);
    }
    public static void drawBox(Graphics g, Rectangle rect){
        drawBox(g, rect, UI_BG_COLOR, UI_BORDER_COLOR);
    }

    public static void drawBox(Graphics g, Rectangle rect, String text){
        drawBox(g, rect);
        drawCenteredString(g, rect, text, GAME_FONT);
    }
    public static void drawBox(Graphics g, Rectangle rect, String text, Font font){
        drawBox(g, rect);
        drawCenteredString(g, rect, text, font);
    }
    public static void drawBox(Graphics g, Rectangle rect, String text, Font font, Color textColor){
        drawBox(g, rect);
        drawCenteredString(g, rect, text, font, textColor);
    }

    public static void drawCenteredString(Graphics g, Rectangle rect, String text, Font font, Color textColor) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        if (textColor != null) g.setColor(textColor);
        g.drawString(text, x, y);
    }
    public static void drawCenteredString(Graphics g,  Rectangle rect, String text, Font font){
        drawCenteredString(g, rect, text, font, UI_FG_COLOR);
    }
    public static void drawCenteredString(Graphics g, Rectangle rect, String text){
        drawCenteredString(g, rect, text, GAME_FONT, UI_FG_COLOR);
    }

    public void drawMap(Graphics g){
        if (battle.getMap() == null) return;

        // Draw grid
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                drawTile(g, c, r);
            }
        }

        // Draw grid coloring for teams (before cursor, so it shows up on top)
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                Tile tile = battle.getMap().getTile(c, r);
                IntPoint pos = tileScreenPos(c, r);
                tile.drawGridTile(g, pos.getX(), pos.getY(), z);
            }
        }

        // Draw the cursor
        drawCursor(g);

        // Draw units (After cursor, so the cursor is displayed below units)
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                Tile tile = battle.getMap().getTile(c, r);
                IntPoint pos = tileScreenPos(c, r);
                tile.draw(g, pos.getX(), pos.getY(), z);
            }
        }

        // Draw UI for units (After unit is drawn, but before floating texts)
        for (int c=0; c<battle.getMap().numCols(); c++){
            for (int r=0; r<battle.getMap().numRows(); r++){
                Tile tile = battle.getMap().getTile(c, r);
                IntPoint pos = tileScreenPos(c, r);
                tile.drawUI(g, pos.getX(), pos.getY(), z);
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
        FloatPoint pos = tileScreenPos(cursorViewCol.floatValue(), cursorViewRow.floatValue());

        int fz = z/5;
        int tz = z/10;

        float r = 0.8f;
        g.drawPolygon(
                new int[]{(int)pos.getX(), (int)pos.getX()-z+fz, (int)pos.getX(), (int)pos.getX()+z-fz},
                new int[]{(int)pos.getY()+tz, (int)pos.getY()+hz, (int)pos.getY()+z-tz, (int)pos.getY()+hz},
                4
        );
    }

    public boolean isSelected(int c, int r){
        return c==cursorCol && r==cursorRow;
    }

    public static Color blendColors(Color c1, Color c2, Number percent){
        return new Color(
                c1.getRed() + (int)((c2.getRed()-c1.getRed())*percent.doubleValue()),
                c1.getGreen() + (int)((c2.getGreen()-c1.getGreen())*percent.doubleValue()),
                c1.getBlue() + (int)((c2.getBlue()-c1.getBlue())*percent.doubleValue())
        );
    }
    public static Color blendColors(Color c1, Color c2){
        return blendColors(c1, c2, 0.5);
    }

    // ================ UNIT OVERLAY STATS
    public static void drawBar(Graphics g, int x, int y, int z, double percent, Color fillColor){
        y += (int)(z*0.75);

        int width = z;
        int height = (int)(z*0.1);
        int hw = width/2;
        int hh = height/2;
        int barWidth = (int)(z*percent);

        g.setColor(BattlePanel.BAR_BG_COLOR);
        g.fillRect(x-hw, y-hh, width, height);

        g.setColor(fillColor);
        g.fillRect(x-hw, y-hh, barWidth, height);

        g.setColor(BattlePanel.BAR_BORDER_COLOR);
        g.drawRect(x-hw, y-hh, width, height);
    }
    public static void drawBar(Graphics g, int x, int y, int z, double percent){
        drawBar(g, x, y, z, percent, READINESS_COLOR);
    }

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
                (int)((c-viewCol.floatValue() + viewRow.floatValue())*zoom + getWidth()/2) - z*r,
                (int)((r-viewRow.floatValue() - viewCol.floatValue()/2 + viewRow.floatValue()/2)*zoom + getHeight()/2) + hz*c - hz*r
        );
    }
    /** Convert a tile position to its position on the screen. **/
    public FloatPoint tileScreenPos(float c, float r){
        return new FloatPoint(
                ((c-viewCol.floatValue() + viewRow.floatValue())*zoom + getWidth()/2.0f) - z*r,
                ((r-viewRow.floatValue() - viewCol.floatValue()/2 + viewRow.floatValue()/2)*zoom + getHeight()/2.0f) + hz*c - hz*r
        );
    }
    /** Convert a screen position to its position in tiles. **/
    public IntPoint screenTilePos(int c, int r){
        return new IntPoint();
    }


    // ================ SHAPE DRAWING
    public static void drawRectPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height,
                                     Number brightness, Number grayness, boolean hideBottomFace){
        int hz = z/2;
        int hw = width/2;
        int hl = length/2;
        int qw = width/4;
        int ql = length/4;

        y += hz; // center rectangle on tile

        int[] xPoints = new int[]{x+hw-hl, x-hw-hl, x-hw-hl, x-hw+hl, x+hw+hl, x+hw+hl};
        int[] yPoints = new int[]{y+qw+ql, y-qw+ql, y-qw+ql-height,
                y-qw-ql-height, y+qw-ql-height, y+qw-ql};

        g.setColor(team.getUnitColor(brightness, grayness));
        g.fillPolygon(xPoints, yPoints, 6);

        g.setColor(team.getColor(brightness, grayness));
        g.drawPolygon(xPoints, yPoints, 6);

        if (hideBottomFace){
            // Cut off bottom segments and redraw side segments
            g.setColor(team.getUnitColor(brightness, grayness));
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            g.drawLine(xPoints[0], yPoints[0], xPoints[5], yPoints[5]);
            g.setColor(team.getColor(brightness, grayness));
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
            g.drawLine(xPoints[4], yPoints[4], xPoints[5], yPoints[5]);
        }

        // Connect corners to center
        for (int i=0; i<6; i += 2){
            g.drawLine(x+hw-hl, y+qw+ql-height, xPoints[i], yPoints[i]);
        }
    }
    public static void drawRectPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height, Number brightness, Number grayness){
        drawRectPrism(g, x, y, z, team, width, length, height, brightness, grayness,false);
    }

    public static void drawWidthRect(Graphics g, int x, int y, int z, Team team,
                                     int width, int height, Number brightness, Number grayness){
        int hz = z/2;
        int hw = width/2;
        int qw = width/4;

        y += hz;

        int[] xPoints = {x-hw, x+hw, x+hw, x-hw};
        int[] yPoints = {y+qw, y-qw, y-qw-height, y+qw-height};

        g.setColor(team.getUnitColor(brightness, grayness));
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(team.getColor(brightness, grayness));
        g.drawPolygon(xPoints, yPoints, 4);
    }

    public static void drawLengthRect(Graphics g, int x, int y, int z, Team team, int length, int height, Number brightness, Number grayness){
        int hz = z/2;
        int hl = length/2;
        int ql = length/4;

        y += hz;

        int[] xPoints = {x-hl, x+hl, x+hl, x-hl};
        int[] yPoints = {y-ql, y+ql, y+ql-height, y-ql-height};

        g.setColor(team.getUnitColor(brightness, grayness));
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(team.getColor(brightness, grayness));
        g.drawPolygon(xPoints, yPoints, 4);
    }

    public static void drawTriangularPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height,
                                           Number brightness, Number grayness, boolean hideBottomFace){
        int hz = z/2;
        int hw = width/2;
        int hl = length/2;
        int qw = width/4;
        int ql = length/4;

        y += hz; // center rectangle on tile

        int[] xPoints = new int[]{x+hw-hl, x-hw-hl, x, x+hw+hl};
        int[] yPoints = new int[]{y+qw+ql, y-qw+ql, y-height, y+qw-ql};

        g.setColor(team.getUnitColor(brightness, grayness));
        g.fillPolygon(xPoints, yPoints, 4);

        g.setColor(team.getColor(brightness, grayness));
        g.drawPolygon(xPoints, yPoints, 4);

        if (hideBottomFace){
            // Cut off bottom segments and redraw top segments
            g.setColor(team.getUnitColor(brightness, grayness));
            g.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
            g.drawLine(xPoints[0], yPoints[0], xPoints[3], yPoints[3]);
            g.setColor(team.getColor(brightness, grayness));
            g.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
            g.drawLine(xPoints[3], yPoints[3], xPoints[2], yPoints[2]);
        }

        // Connect front corner to top corner
        g.drawLine(xPoints[0], yPoints[0], xPoints[2], yPoints[2]);
    }
    public static void drawTriangularPrism(Graphics g, int x, int y, int z, Team team,
                                     int width, int length, int height, Number brightness, Number grayness){
        drawTriangularPrism(g, x, y, z, team, width, length, height, brightness, grayness, false);
    }

    // CURSOR & CAMERA
    /** Update the cursor's display. Should be called after the cursor is moved (when cursorCol and/or cursorRow is changed). **/
    public void updateMapCursor(){
        if (EASE_CURSOR) {
            float col = cursorViewCol.floatValue();
            cursorViewCol = new AnimatedValue(TimingFunction.EASE, CURSOR_SPEED, col, cursorCol);
            float row = cursorViewRow.floatValue();
            cursorViewRow = new AnimatedValue(TimingFunction.EASE, CURSOR_SPEED, row, cursorRow);
        } else {
            cursorViewCol = cursorCol;
            cursorViewRow = cursorRow;
        }

        if (CAMERA_FOLLOW_CURSOR) {
            viewCol = cursorViewCol;
            viewRow = cursorViewRow;
        }

        // Brighten selected tile
        Tile selectedTile = battle.getMap().getTile(cursorCol, cursorRow);
        selectedTile.setBrightness(new AnimatedValue(TimingFunction.EASE, CURSOR_SPEED, selectedTile.getBrightness().doubleValue(), 0.25));

        if (selectedTile instanceof Unit) {
            Unit unit = (Unit)selectedTile;
            // Update labels
            System.out.println(unit.name());
        }
    }

    /** Call before changing the cursorCol and/or cursorRow so that the previously selected item can be un-brightened. **/
    public void preMapCursorMove(){
        // Un-brighten the tile that about to be unselected.
        Tile unselectedTile = battle.getMap().getTile(cursorCol, cursorRow);
        unselectedTile.setBrightness(new AnimatedValue(TimingFunction.EASE, CURSOR_SPEED, unselectedTile.getBrightness().doubleValue(), 0.0));
    }

    public void moveCursor(int c, int r){
        if (cursorMode == CursorMode.MAP_CURSOR){
            if (cursorCol+c >= 0 && cursorCol+c < battle.getMap().numCols()
                    && cursorRow+r >= 0 && cursorRow+r < battle.getMap().numCols()){
                preMapCursorMove();
                cursorCol += c;
                cursorRow += r;
                updateMapCursor();
            }
        } else if (cursorMode == CursorMode.SHOP_CURSOR) {
            int shopItemCount = battle.allies().getShop().getItems().size();
            int shopRows = shopItemCount - shopItemCount/2;

            // this is definitely not efficient and im not going to annotate it, sorry future jack
            shopCursorRow += r;
            if (shopCursorRow < 0){
                shopCursorRow = shopRows - 1;
                if (shopCursorCol == 1 && shopItemCount%2 == 1){
                    shopCursorRow -= 1;
                }
            } else if (shopCursorRow >= shopRows){
                shopCursorRow = 0;
            }
            if (shopCursorRow == shopRows-1 && shopCursorCol == 1 && shopItemCount%2 == 1){
                shopCursorRow = 0;
            }

            shopCursorCol += c;
            if (shopCursorCol < 0) {
                if (shopCursorRow != shopRows-1 || shopItemCount%2 == 0){
                    shopCursorCol = 1;
                } else {
                    shopCursorCol = 0;
                }
            }
            if (shopCursorCol > 1 || (shopCursorCol == 1 && shopCursorRow == shopRows-1 && shopItemCount%2 == 1)){
                shopCursorCol = 0;
            }
        }
    }

    public void setMapCursorPosition(int c, int r){
        if (c >= 0 && c < battle.getMap().numCols()
                && r >= 0 && r < battle.getMap().numCols()){
            preMapCursorMove();
            cursorCol = c;
            cursorRow = r;
            updateMapCursor();
        }
    }

    public void setZoom(float val){
        zoom = val;
        z = (int)zoom;
        hz = z/2;
    }

    public Tile getSelectedTile(){
        return battle.getMap().getTile(cursorCol, cursorRow);
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

    public class KeySpace extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cursorMode == CursorMode.MAP_CURSOR){
                cursorMode = CursorMode.SHOP_CURSOR;
            } else {
                cursorMode = CursorMode.MAP_CURSOR;
            }
        }
    }

    public class KeyZ extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("z pressed");
            Tile tile = getSelectedTile();
            if (tile instanceof EmptyTile){
                ((EmptyTile) tile).contest(battle.allies());
            }
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

