package lib.ui;

import lib.panels.BattlePanel;

import javax.swing.*;
import java.awt.*;

/** A fixed position rectangle on the screen that can have centered text. **/
public class ElementBox {
    // Constants
    public static final Color UI_FG_COLOR = new Color(240, 240, 240);
    public static final Color UI_BG_COLOR = new Color(8,8,8, 200);
    public static final Color UI_BORDER_COLOR = new Color(80, 80, 80);
    public static final Font GAME_FONT = new Font("monospace", Font.PLAIN, 18);
    public static final Font GAME_FONT_BIG = new Font("monospace", Font.PLAIN, 24);
    public enum Alignment {
        NONE, START, CENTER, END
    }

    // Fields
    private JPanel panel;
    private int width, height;
    private String text;
    private Color fg = UI_FG_COLOR;
    private Color bg = UI_BG_COLOR;
    private Color border = UI_BORDER_COLOR;
    private Font font = GAME_FONT;
    private Alignment xAlign;
    private Alignment yAlign;

    // not aligned
    private int x, y;

    // aligned
    private int left, right, top, bottom;
    private boolean fillPanelX, fillPanelY;

    public ElementBox(){

    }
    public ElementBox(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        xAlign = Alignment.NONE;
        yAlign = Alignment.NONE;
    }

    public void draw(Graphics g){
        g.setColor(bg);
        g.fillRect(xPos(), yPos(), width, height);

        g.setColor(border);
        g.drawRect(xPos(), yPos(), width, height);

        if (text != null){
            FontMetrics metrics = g.getFontMetrics(font);
            int strX = xPos() + (width - metrics.stringWidth(text)) / 2;
            int strY = yPos() + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

            g.setColor(fg);
            g.setFont(font);
            g.drawString(text, strX, strY);
        }
    }

    // Positioning
    public int xPos(){
        return alignedPosition(xAlign, x, getLeft(), getRight(), width);
    }
    public int yPos(){
        return alignedPosition(yAlign, y, getTop(), getBottom(), height);
    }
    private int alignedPosition(Alignment alignment, int pos, int start, int end, int size) {
        switch(alignment){
            case NONE:
                return pos;
            case START:
                return start;
            case CENTER:
                return (start + end - size)/2;
            case END:
                return end - size;
        }
        return 0;
    }

    public int getLeft() {
        return fillPanelX ? 0 : left;
    }
    public int getRight() {
        return fillPanelX ? panel.getWidth() : right;
    }
    public int getTop() {
        return fillPanelY ? 0 : top;
    }
    public int getBottom() {
        return fillPanelY ? panel.getHeight() : bottom;
    }

    // Accessors
    public JPanel getPanel() {
        return panel;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
    public Color getFg() {
        return fg;
    }
    public void setFg(Color fg) {
        this.fg = fg;
    }
    public Color getBg() {
        return bg;
    }
    public void setBg(Color bg) {
        this.bg = bg;
    }
    public Color getBorder() {
        return border;
    }
    public void setBorder(Color border) {
        this.border = border;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Font getFont() {
        return font;
    }
    public void setFont(Font font) {
        this.font = font;
    }
    public Alignment getxAlign() {
        return xAlign;
    }
    public void setxAlign(Alignment xAlign) {
        this.xAlign = xAlign;
    }
    public Alignment getyAlign() {
        return yAlign;
    }
    public void setyAlign(Alignment yAlign) {
        this.yAlign = yAlign;
    }
    public void setLeft(int left) {
        this.left = left;
    }
    public void setRight(int right) {
        this.right = right;
    }
    public void setTop(int top) {
        this.top = top;
    }
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }
    public boolean isFillPanelX() {
        return fillPanelX;
    }
    public void setFillPanelX(boolean fillPanelX) {
        this.fillPanelX = fillPanelX;
    }
    public boolean isFillPanelY() {
        return fillPanelY;
    }
    public void setFillPanelY(boolean fillPanelY) {
        this.fillPanelY = fillPanelY;
    }
}
