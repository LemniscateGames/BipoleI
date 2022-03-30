package lib;

import lib.panels.BattlePanel;
import lib.ui.TileInfoElementBox;

import java.awt.*;

/** Any tile that can be placed on a map. **/
public interface Tile {
    /** This tile's display name. **/
    String name();

    /** THis tile's description. **/
    String desc();

    /** The header to display in the tile info element box. **/
    String header();

    /** Anything to draw below the grid (such as contested tile diagonal lines). **/
    void drawBelowGrid(Graphics g, double x, double y, double z);

    /** Draw the base of this tile. Drawn below the cursor. **/
    void drawTileBase(Graphics g, double x, double y, double z);

    /** Draw this tile's features. Drawn above the cursor. **/
    void draw(Graphics g, double x, double y, double z);

    /** Draw the UI for this tile (readiness, hp, atk, etc). **/
    void drawUI(Graphics g, double x, double y, double z);

    /** Run when the cursor is hovered over tile. **/
    void onHover();

    /** Run when the cursor is moved off of hovering over this tile. **/
    void onUnhover();

    /** Run when the mouse is hovered over tile. **/
    void onMouseHover();

    /** Run when the mouse is moved off of hovering over this tile. **/
    void onMouseUnhover();

    /** Whether or not this tile draws a tile base (colored border). **/
    boolean hasBorder();

    /** Ran when this tile is placed on a map. **/
    void onPlace(Map map, int r, int c);

    /** Fill out the tole info element box section of the screen when cursored over. **/
    default void displayInfo(TileInfoElementBox element) {
        element.setDisplayedTile(this);
        element.getTitle().setText(header());
        element.getDesc().setText(desc());
    }

    /** Whether or not this tile is "controllable" by the team (under their control). **/
    default boolean isControllable(Team team){ return false; }

    /** Initialize when being added to a BattlePanel so that effects can be automatically called. **/
    default void setPanel(BattlePanel panel){};

    /** Whether or not to draw this tile's base before others. **/
    boolean isBaseDrawPriority();
}
