package lib.panels;

import lib.*;
import lib.display.AnimatedValue;
import lib.misc.RowColPoint;
import lib.shop.Shop;
import lib.ui.PointCounterElementBox;
import lib.ui.ShopItemElementBox;
import lib.units.EmptyLand;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

import static lib.ui.ElementBox.Alignment.CENTER;

public class BattlePanel extends ElementPanel implements MouseInputListener, MouseMotionListener, MouseWheelListener {
    // ==== CONSTANTS
    // Cursor
    public static final boolean EASE_CURSOR = true;
    public static final int CURSOR_SPEED = 150;
    public static final double ZOOM_SCROLL_FACTOR = 0.8;
    public static final boolean EASE_CAMERA = true;
    public static final int CAMERA_SPEED = 250;
    public static final boolean CAMERA_FOLLOW_CURSOR = true;

    // Grid
    public static final double ROW_X_OFFSET = -1.0;
    public static final double ROW_Y_OFFSET = 0.5;
    public static final double COL_X_OFFSET = 1.0;
    public static final double COL_Y_OFFSET = 0.5;
    public static final double HEIGHT_Y_OFFSET = -1.15;
    public static final Color GRID_COLOR = new Color(240, 240, 240);

    // UI
    public static final Color BAR_BG_COLOR = new Color(30, 30, 30, 200);
    public static final Color BAR_BORDER_COLOR = new Color(8,8,8);

    // ==== VARIABLES
    // Camera
    private Number cameraRowPos = 4.0;
    private Number cameraColPos = 4.0;
    private Number zoom = 80.0;

    // Cursor
    private int mapCursorRow = 0;
    private int mapCursorCol = 0;
    private int shopCursorRow = 0;
    private int shopCursorCol = 0;
    private Number mapCursorDisplayRow = 0;
    private Number mapCursorDisplayCol = 0;
    private int mouseRow = 0;
    private int mouseCol = 0;
    private int mouseShopItem = 0;

    // Controls
    private Point clickPoint;
    private double clickCameraRowPos, clickCameraColPos;
    public enum ControlMode {
        MAP_CURSOR, SHOP_CURSOR
    }
    private ControlMode mode;

    // UI Elements
    private final PointCounterElementBox pointCounter;

    // Other Variables
    private final Battle battle;
    private final Team team;
    private final Shop shop;

    // ==== CONSTRUCTORS
    public BattlePanel(Battle battle) {
        this.battle = battle;
        this.team = battle.getTeam(0);

        // ---- Initialize screen refresh timer
        ActionListener updateScreen = evt -> super.repaint();
        // Timers
        Timer screenRefreshTimer = new Timer(20, updateScreen);
        screenRefreshTimer.setCoalesce(false);
        screenRefreshTimer.start();

        // point counter
        pointCounter = new PointCounterElementBox(0, 0, 120, 40);
        pointCounter.setAlignPanelX(true);
        pointCounter.setxAlign(CENTER);
        super.addElement(pointCounter);

        // Initialize shop
        shop = Shop.generateDefaultShop(this, team);
        shop.selectItem(0);

        // ---- Controls
        mode = ControlMode.MAP_CURSOR;

        // mouse
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
        super.addMouseWheelListener(this);

        // keyboard
        super.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up");
        super.getInputMap().put(KeyStroke.getKeyStroke("W"), "up");
        super.getActionMap().put("up", new CursorUpAction());
        super.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
        super.getInputMap().put(KeyStroke.getKeyStroke("S"), "down");
        super.getActionMap().put("down", new CursorDownAction());
        super.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
        super.getInputMap().put(KeyStroke.getKeyStroke("A"), "left");
        super.getActionMap().put("left", new CursorLeftAction());
        super.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
        super.getInputMap().put(KeyStroke.getKeyStroke("D"), "right");
        super.getActionMap().put("right", new CursorRightAction());

        super.getInputMap().put(KeyStroke.getKeyStroke("Z"), "interact");
        super.getActionMap().put("interact", new InteractAction());
        super.getInputMap().put(KeyStroke.getKeyStroke("X"), "cancel");
        super.getActionMap().put("cancel", new CancelAction());

        // ---- other
        super.setBackground(new Color(16,16,16));
    }

    // ==== MAIN DRAW
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the map and all its tiles and their UI.
        drawMap(g);

        // Update the point counter's text.
        pointCounter.setText(team.getPoints()+" pts");
    }

    /** Draw the whole map and all its tiles, big ol function so im separating it. **/
    public void drawMap(Graphics g){
        Map map = battle.getMap();
        int numRows = map.numRows();
        int numCols = map.numCols();

        // ==== Draw everything that goes below the grid
        for (int r=0; r<numRows; r++){
            for (int c=0; c<numCols; c++){
                Tile tile = map.getTile(r, c);
                if (tile != null){
                    tile.drawBelowGrid(g, getScreenX(r, c), getScreenY(r, c), zoom.doubleValue());
                }
            }
        }

        // ==== Draw white map grid
        // Draw west and north side of all tiles
        g.setColor(GRID_COLOR);
        for (int r=0; r<numRows; r++){
            for (int c=0; c<numCols; c++){
                // Draw west side of tile
                if (map.noBorderedTile(r, c) && map.noBorderedTile(r, c-1)){
                    g.drawLine(getScreenIntX(r, c), getScreenIntY(r, c),
                            getScreenIntX(r+1, c), getScreenIntY(r+1, c));
                }

                // Draw north side of tile
                if (map.noBorderedTile(r, c) && map.noBorderedTile(r-1, c)){
                    g.drawLine(getScreenIntX(r, c), getScreenIntY(r, c),
                            getScreenIntX(r, c+1), getScreenIntY(r, c+1));
                }

            }
        }

        // Draw east borders of easternmost tiles
        for (int r=0; r<numRows; r++){
            if (map.noBorderedTile(r, numCols-1)){
                g.drawLine(getScreenIntX(r, numCols), getScreenIntY(r, numCols),
                        getScreenIntX(r+1, numCols), getScreenIntY(r+1, numCols));
            }
        }

        // Draw south borders of southernmost tiles
        for (int c=0; c<numCols; c++){
            if (map.noBorderedTile(numRows-1, c)){
                g.drawLine(getScreenIntX(numRows, c), getScreenIntY(numRows, c),
                        getScreenIntX(numRows, c+1), getScreenIntY(numRows, c+1));
            }
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
                getScreenX(mapCursorDisplayRow.doubleValue(), mapCursorDisplayCol.doubleValue()),
                getScreenY(mapCursorDisplayRow.doubleValue(), mapCursorDisplayCol.doubleValue()),
                zoom.doubleValue(), 0.1, mode == ControlMode.MAP_CURSOR ? team.getPointColor() : team.getFadedPointColor()
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

        // ==== Draw UI for all tiles after all shapes & other non UI graphics are drawn
        for (int r=0; r<numRows; r++){
            for (int c=0; c<numCols; c++){
                Tile tile = map.getTile(r, c);
                if (tile != null){
                    tile.drawUI(g, getScreenX(r, c), getScreenY(r, c), zoom.doubleValue());
                }
            }
        }

        // Draw UI elementBoxes
        drawElements(g);
    }

    // ==== FEATURES (static)
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


    // =========== USER INTERFACE
    public static void drawBar(Graphics g, double x, double y, double z, double percent, Color fillColor){
        y += (int)(z*0.75);

        int width = (int)z;
        int height = (int)(z*0.1);
        int hw = width/2;
        int hh = height/2;
        int barWidth = (int)(z*percent);

        g.setColor(BAR_BG_COLOR);
        g.fillRect((int)x-hw, (int)y-hh, width, height);

        g.setColor(fillColor);
        g.fillRect((int)x-hw, (int)y-hh, barWidth, height);

        g.setColor(BAR_BORDER_COLOR);
        g.drawRect((int)x-hw, (int)y-hh, width, height);
    }

    // ==== INFORMATION
    // Positioning
    /** Get the screen X position of a given row and col on the grid. **/
    public double getScreenX(double rowPos, double colPos){
        double relRow = rowPos - cameraRowPos.doubleValue();
        double relCol = colPos - cameraColPos.doubleValue();
        return (relRow*ROW_X_OFFSET + relCol*COL_X_OFFSET)*zoom.doubleValue() + super.getWidth()/2.0;
    }
    /** Get the screen Y position of a given row and col on the grid. **/
    public double getScreenY(double rowPos, double colPos, double height){
        double relRow = rowPos - cameraRowPos.doubleValue();
        double relCol = colPos - cameraColPos.doubleValue();
        return (relRow*ROW_Y_OFFSET + relCol*COL_Y_OFFSET + height*HEIGHT_Y_OFFSET)*zoom.doubleValue() + super.getHeight()/2.0;
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


    /** Get the grid X position of a given X and Y screen coordinate. **/
    public RowColPoint getGridPos(int x, int y){
        double relX = (x - super.getWidth()/2.0) / zoom.doubleValue();
        double relY = (y - super.getHeight()/2.0) / zoom.doubleValue();

        double row = (relX/ROW_X_OFFSET + relY/ROW_Y_OFFSET)/2 + cameraRowPos.doubleValue();
        double col = (relX/COL_X_OFFSET + relY/COL_Y_OFFSET)/2 + cameraColPos.doubleValue();

        return new RowColPoint((int)row, (int)col);
    }

    public double getGridPosRow(int x, int y){
        double relX = (x - super.getWidth()/2.0) / zoom.doubleValue();
        double relY = (y - super.getHeight()/2.0) / zoom.doubleValue();
        return (relX/ROW_X_OFFSET + relY/ROW_Y_OFFSET)/2 + cameraRowPos.doubleValue();
    }
    public double getGridPosCol(int x, int y){
        double relX = (x - super.getWidth()/2.0) / zoom.doubleValue();
        double relY = (y - super.getHeight()/2.0) / zoom.doubleValue();
        return (relX/COL_X_OFFSET + relY/COL_Y_OFFSET)/2 + cameraColPos.doubleValue();
    }

    // Game data
    public Tile cursorTile(){
        return battle.getMap().getTile(mapCursorRow, mapCursorCol);
    }
    public Unit cursorUnit(){
        Tile tile = battle.getMap().getTile(mapCursorRow, mapCursorCol);
        if (tile instanceof Unit){
            return (Unit) tile;
        }
        return null;
    }


    // ==== CONTROL LISTENERS
    // mouse
    @Override
    public void mouseClicked(MouseEvent e) {
        // Shop click
        if (e.getX() > getWidth()-Shop.SHOP_WIDTH){
            changeModeTo(ControlMode.SHOP_CURSOR);
        }

        // Map click
        else {
            changeModeTo(ControlMode.MAP_CURSOR);
            if (e.getButton() == 1){
                RowColPoint clickGridPos = getGridPos(e.getX(), e.getY());
                setCursor(clickGridPos.row, clickGridPos.col);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickPoint = e.getPoint();
        clickCameraRowPos = cameraRowPos.doubleValue();
        clickCameraColPos = cameraColPos.doubleValue();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - clickPoint.x;
        int dy = e.getY() - clickPoint.y;
        cameraRowPos = clickCameraRowPos - (dx/ROW_X_OFFSET + dy/ROW_Y_OFFSET) / 2 / zoom.doubleValue();
        cameraColPos = clickCameraColPos - (dx/COL_X_OFFSET + dy/COL_Y_OFFSET) / 2 / zoom.doubleValue();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Shop
        if (e.getX() >= getWidth()-Shop.SHOP_WIDTH) {
            Tile unhoverTile = battle.getMap().getTile(mouseRow, mouseCol);
            if (unhoverTile != null) unhoverTile.onMouseUnhover();
            mouseRow = -1; mouseCol = -1;

            int newMouseShopItem = -1;
            for (int i=0; i<shop.getItemElements().length; i++){
                ShopItemElementBox element = shop.getItemElements()[i];
                if(
                        element.getX() <= e.getX()
                                && element.getX()+element.getWidth() >= e.getX()
                                && element.getY() <= e.getY()
                                && element.getY()+element.getHeight() >= e.getY()
                ){
                    newMouseShopItem = i;
                    break;
                }
            }
            if (newMouseShopItem != -1 && newMouseShopItem != mouseShopItem){
                shop.unselectItem(mouseShopItem);
                mouseShopItem = newMouseShopItem;
                shopCursorRow = newMouseShopItem / Shop.COLS;
                shopCursorCol = newMouseShopItem % Shop.COLS;
                shop.selectItem(mouseShopItem);
            }
        }

        // Map
        else {
            RowColPoint mouseGridPos = getGridPos(e.getX(), e.getY());

            if (mouseRow != mouseGridPos.row || mouseCol != mouseGridPos.col){
                Tile unhoverTile = battle.getMap().getTile(mouseRow, mouseCol);
                if (unhoverTile != null) unhoverTile.onMouseUnhover();

                Tile hoverTile = battle.getMap().getTile(mouseGridPos.row, mouseGridPos.col);
                if (hoverTile != null) hoverTile.onMouseHover();

                mouseRow = mouseGridPos.row;
                mouseCol = mouseGridPos.col;
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double scale = Math.pow(ZOOM_SCROLL_FACTOR, e.getWheelRotation());
        zoom = zoom.doubleValue() * scale;

        RowColPoint mouseGridPos = getGridPos(super.getWidth() - e.getX(), super.getHeight() - e.getY());

        double rowDistance = mouseGridPos.row - cameraRowPos.doubleValue();
        double colDistance = mouseGridPos.col - cameraColPos.doubleValue();

        cameraRowPos = mouseGridPos.row - rowDistance*scale;
        cameraColPos = mouseGridPos.col - colDistance*scale;
    }

    // keyboard
    public class InteractAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            interact();
        }
    }
    public class CancelAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }

    public class CursorUpAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(-1, 0);
        }
    }
    public class CursorDownAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(1, 0);
        }
    }
    public class CursorLeftAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(0, -1);
        }
    }
    public class CursorRightAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e) {
            moveCursor(0, 1);
        }
    }

    public void changeModeTo(ControlMode newMode){
        if (mode == newMode) return;

        if (mode == ControlMode.SHOP_CURSOR) shop.unfocusElement();

        mode = newMode;

        if (mode == ControlMode.SHOP_CURSOR) shop.focusElement();
    }

    // ==== USER INTERACTION METHODS (called via controls)
    public void moveCursor(int row, int col){
        // Map cursor mode - move cursor in map
        if (mode == ControlMode.MAP_CURSOR){
            Map map = battle.getMap();
            if (mapCursorRow +row >= 0 && mapCursorRow +row < map.numRows()
                    && mapCursorCol +col >= 0 && mapCursorCol +col < map.numCols()){
                Tile tile = map.getTile(mapCursorRow, mapCursorCol);
                if (tile != null) tile.onUnhover();

                mapCursorRow += row;
                mapCursorCol += col;

                if (EASE_CURSOR){
                    mapCursorDisplayRow = new AnimatedValue(CURSOR_SPEED, mapCursorDisplayRow.doubleValue(), mapCursorRow);
                    mapCursorDisplayCol = new AnimatedValue(CURSOR_SPEED, mapCursorDisplayCol.doubleValue(), mapCursorCol);
                } else {
                    mapCursorDisplayRow = mapCursorDisplayRow.doubleValue() + row;
                    mapCursorDisplayCol = mapCursorDisplayCol.doubleValue() + col;
                }

                Tile newTile = map.getTile(mapCursorRow, mapCursorCol);
                if (newTile != null) newTile.onHover();

                moveCameraToCursor();
            }
        }

        else if (mode == ControlMode.SHOP_CURSOR){
            if (shopCursorRow + row >= 0 && shopCursorRow + row < Shop.ROWS
                    && shopCursorCol + col >= 0 && shopCursorCol + col < Shop.COLS) {
                shop.unselectItem(shopCursorRow*Shop.COLS + shopCursorCol);
                shopCursorRow += row;
                shopCursorCol += col;
                mouseShopItem = shopCursorRow*Shop.COLS + shopCursorCol;
                shop.selectItem(shopCursorRow*Shop.COLS + shopCursorCol);
            }
        }
    }

    public void setCursor(int row, int col){
        Map map = battle.getMap();
        if (row >= 0 && row < map.numRows()
                && col >= 0 && col < map.numCols()){
            Tile tile = map.getTile(mapCursorRow, mapCursorCol);
            if (tile != null) tile.onUnhover();

            mapCursorRow = row;
            mapCursorCol = col;

            if (EASE_CURSOR){
                mapCursorDisplayRow = new AnimatedValue(CURSOR_SPEED, mapCursorDisplayRow.doubleValue(), mapCursorRow);
                mapCursorDisplayCol = new AnimatedValue(CURSOR_SPEED, mapCursorDisplayCol.doubleValue(), mapCursorCol);
            } else {
                mapCursorDisplayRow = row;
                mapCursorDisplayCol = col;
            }

            Tile newTile = map.getTile(mapCursorRow, mapCursorCol);
            if (newTile != null) newTile.onHover();

            moveCameraToCursor();
        }

        else if (mode == ControlMode.SHOP_CURSOR){
            if (row >= 0 && row < Shop.ROWS
                    && col >= 0 && col < Shop.COLS) {
                shop.unselectItem(shopCursorRow*Shop.COLS + shopCursorCol);
                shopCursorRow = row;
                shopCursorCol = col;
                mouseShopItem = shopCursorRow*Shop.COLS + shopCursorCol;
                shop.selectItem(shopCursorRow*Shop.COLS + shopCursorCol);
            }
        }
    }

    public static final double FOLLOW_X_MARGIN = 2.0;
    public static final double FOLLOW_Y_MARGIN = 1.5;
    public void moveCameraToCursor(){
        if (CAMERA_FOLLOW_CURSOR) {
            int cameraX = getScreenIntX(cameraRowPos.doubleValue(), cameraColPos.doubleValue());
            int cameraY = getScreenIntY(cameraRowPos.doubleValue(), cameraColPos.doubleValue());
            int x = getScreenIntX(mapCursorRow +0.5, mapCursorCol +0.5);
            int y = getScreenIntY(mapCursorRow +0.5, mapCursorCol +0.5);
            int followXScreen = (int)(zoom.doubleValue()*FOLLOW_X_MARGIN);
            int followYSCreen = (int)(zoom.doubleValue()*FOLLOW_Y_MARGIN);

            boolean cameraMoved = false;

            if (x < followXScreen){
                cameraX += x - followXScreen; cameraMoved = true;
            } else if (x > super.getWidth()-followXScreen-Shop.SHOP_WIDTH){
                cameraX += x - super.getWidth()+followXScreen+Shop.SHOP_WIDTH; cameraMoved = true;
            }

            if (y < followYSCreen) {
                cameraY += y - followYSCreen; cameraMoved = true;
            } else if (y > super.getHeight()-followYSCreen) {
                cameraY += y - super.getHeight()+followYSCreen; cameraMoved = true;
            }

            if (cameraMoved) moveCameraToScreenPoint(cameraX, cameraY);
        }
    }

    public void moveCameraToScreenPoint(int x, int y){
        double row = getGridPosRow(x, y);
        double col = getGridPosCol(x, y);
        if (EASE_CAMERA){
            cameraRowPos = new AnimatedValue(CAMERA_SPEED, cameraRowPos.doubleValue(), row);
            cameraColPos = new AnimatedValue(CAMERA_SPEED, cameraColPos.doubleValue(), col);
        } else {
            cameraRowPos = row;
            cameraColPos = col;
        }
    }

    // Called when Z is pressed.
    public void interact(){
        if (mode == ControlMode.MAP_CURSOR){
            interactWithTile(mapCursorRow, mapCursorCol);
        }
        else if (mode == ControlMode.SHOP_CURSOR){
//            changeModeTo(ControlMode.MAP_CURSOR);
            pointCounter.shake();
        }
    }

    public void interactWithTile(int row, int col){
        Tile selectedTile = cursorTile();

        // If an empty tile is interacted with, start contesting it if there is an adjacent tile and it can be afforded
        if (selectedTile == null
                && battle.getMap().hasAdjacentOwnedTile(team, row, col)
                && team.subtractPoints(1)){
            battle.getMap().placeTile(new ContestedTile(team), row, col);

        }
        // Contest a tile that is already bing contested if a contest tile is interacted with
        else if (selectedTile instanceof ContestedTile){
            ContestedTile contestedTile = (ContestedTile) selectedTile;
            contestedTile.contest(team);
        }
        // If this is a claimed tile
        else if (selectedTile instanceof ClaimedTile){
            // If owned by player
            if (((ClaimedTile) selectedTile).getTeam() == team){
                // If this is empty land, open up the shop to build something
                if (selectedTile instanceof EmptyLand){
                    changeModeTo(ControlMode.SHOP_CURSOR);
                }
            }

        }

    }

    // Called when X is pressed.
    public void cancel(){
        if (mode == ControlMode.SHOP_CURSOR){
            changeModeTo(ControlMode.MAP_CURSOR);
        }
    }


    // ======== Utility
    public static void drawCenteredString(Graphics g, Rectangle rect, String text, Font font, Color textColor) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        if (textColor != null) g.setColor(textColor);
        g.drawString(text, x, y);
    }

    // ======== Accessors
    public ControlMode getMode() {
        return mode;
    }
}

//            cameraRowPos = new AnimatedValue(CAMERA_SPEED, cameraRowPos.doubleValue(), cursorRow);
//            cameraColPos = new AnimatedValue(CAMERA_SPEED, cameraColPos.doubleValue(), cursorCol);