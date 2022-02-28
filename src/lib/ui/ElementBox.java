package lib.ui;

import lib.panels.ElementPanel;

import java.awt.*;

/** A fixed position rectangle on the screen that can have centered text. **/
public class ElementBox {
    // Constants
    public static final Color UI_FG_COLOR = new Color(240, 240, 240);
    public static final Color UI_BG_COLOR = new Color(8,8,8, 200);
    public static final Color UI_BG_COLOR_TRANSPARENT = new Color(8,8,8, 128);
    public static final Color UI_BORDER_COLOR = new Color(80, 80, 80);
    public static final Color UI_BORDER_COLOR_LIGHTER = new Color(128, 128, 128);
    public static final Color UI_BORDER_COLOR_TRANSPARENT = new Color(80, 80, 80, 160);
    public static final Font GAME_FONT_SMALL = new Font("monospace", Font.PLAIN, 13);
    public static final Font GAME_FONT = new Font("monospace", Font.PLAIN, 18);
    public static final Font GAME_FONT_BIG = new Font("monospace", Font.PLAIN, 23);
    public enum Alignment {
        NONE, START, CENTER, END
    }

    // Fields
    private ElementPanel panel;
    private int width, height;
    private String text;
    private Color fg = UI_FG_COLOR;
    private Color bg = UI_BG_COLOR;
    private Color border = UI_BORDER_COLOR;
    private Font font = GAME_FONT;
    private Alignment xAlign = Alignment.START;
    private Alignment yAlign = Alignment.START;
    private Alignment xTextAlign = Alignment.CENTER;
    private Alignment yTextAlign = Alignment.CENTER;
    private boolean isTransparentBg;

    // not aligned
    private int x, y;

    // aligned
    private int left, right, top, bottom;
    private boolean alignPanelX, alignPanelY;
    private boolean fillPanelX, fillPanelY;
    private ElementBox relLeftElement, relRightElement, relTopElement, relBottomElement;

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

    /** Initialize this element's components on things that are dependent on the ElementPanel ti is instantiated into.
     * Should be overridden if neccesary. **/
    public void initialize(ElementPanel panel){
        setPanel(panel);
    }

    /** Anything that has to be initialized on this ElementBox before the super draw call is made should be overridden in this method. **/
    public void beforeDraw(){

    }

    public void draw(Graphics g){
        beforeDraw();

        if (!isTransparentBg){
            g.setColor(bg);
            g.fillRect(getX(), getY(), getWidth(), getHeight());

            g.setColor(border);
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }

        if (text != null){
            FontMetrics metrics = g.getFontMetrics(font);

            int strX, strY;
            switch(xTextAlign){
                case CENTER:
                    strX = getX() + (width - metrics.stringWidth(text)) / 2; break;
                case END:
                    strX = getX() + width - metrics.stringWidth(text); break;
                default:
                    strX = getX(); break;
            }
            switch(yTextAlign){
                case CENTER:
                    strY = getY() + (height - metrics.getHeight() + metrics.getAscent()) / 2; break;
                case END:
                    strY = getY() + height - metrics.getHeight() + metrics.getAscent(); break;
                default:
                    strY = getY() + metrics.getAscent(); break;
            }

            g.setColor(fg);
            g.setFont(font);
            g.drawString(text, strX, strY);
        }
    }

    // Positioning
    public int getX(){
        return alignedPosition(xAlign, x, getLeft(), getRight(), getWidth());
    }
    public int getY(){
        return alignedPosition(yAlign, y, getTop(), getBottom(), getHeight());
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
        return (relLeftElement==null)
                ? (alignPanelX ? 0 : left)
                : relLeftElement.getX() + left;
    }
    public int getRight() {
        return (relRightElement==null)
                ? (alignPanelX ? panel.getWidth() : right)
                : relRightElement.getX() + relRightElement.getWidth() - right;
    }
    public int getTop() {
        return (relTopElement==null)
                ? (alignPanelY ? 0 : top)
                : relTopElement.getY() + top;
    }
    public int getBottom() {
        return (relBottomElement==null)
                ? (alignPanelY ? panel.getHeight() : bottom)
                : relBottomElement.getY() + relBottomElement.getHeight() - bottom;
    }

    public int getWidth(){
        return fillPanelX ? panel.getWidth() : width;
    }
    public int getHeight(){
        return fillPanelY ? panel.getHeight() : height;
    }

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    public boolean isTransparentBg() {
        return isTransparentBg;
    }
    public void setTransparentBg(boolean transparentBg) {
        isTransparentBg = transparentBg;
    }

    // Accessors
    public ElementPanel getPanel() {
        return panel;
    }
    public void setPanel(ElementPanel panel) {
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
    public Alignment getxTextAlign() {
        return xTextAlign;
    }
    public void setxTextAlign(Alignment xTextAlign) {
        this.xTextAlign = xTextAlign;
    }
    public Alignment getyTextAlign() {
        return yTextAlign;
    }
    public void setyTextAlign(Alignment yTextAlign) {
        this.yTextAlign = yTextAlign;
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
    public boolean isAlignPanelX() {
        return alignPanelX;
    }
    public void setAlignPanelX(boolean alignPanelX) {
        this.alignPanelX = alignPanelX;
    }
    public boolean isAlignPanelY() {
        return alignPanelY;
    }
    public void setAlignPanelY(boolean alignPanelY) {
        this.alignPanelY = alignPanelY;
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
    public void setRelLeftElement(ElementBox relLeftElement) {
        this.relLeftElement = relLeftElement;
    }
    public void setRelRightElement(ElementBox relRightElement) {
        this.relRightElement = relRightElement;
    }
    public void setRelTopElement(ElementBox relTopElement) {
        this.relTopElement = relTopElement;
    }
    public void setRelBottomElement(ElementBox relBottomElement) {
        this.relBottomElement = relBottomElement;
    }

}
