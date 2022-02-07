package BipoleI.lib;

import BipoleI.lib.battlepanel.BattlePanel;

import java.awt.*;
import java.util.ArrayList;

public class ElementBox {
    private final ElementBox parent;
    private final ArrayList<ElementBox> children;

    private Number width, height;

    private Number topMargin, bottomMargin, leftMargin, rightMargin;
    private boolean hasBorder;
    private Number topPad, bottomPad, leftPad, rightPad;

    private Color fgColor;
    private Color bgColor;
    private Color borderColor;
    private Font font;
    private String text;

    private Alignment alignment;
    private Alignment textAlign;

    // Only utilized during rendering.
    private int occupiedWidth;
    private int occupiedHeight;

    public ElementBox(ElementBox parent){
        this.parent = parent;
        this.children = new ArrayList<>();

        if (parent != null){
            parent.addChild(this);
            alignment = parent.getAlignment();
        } else {
            alignment = Alignment.TOP_LEFT;
        }

        // defaults that can be overridden with setters later on if needed.
        bgColor = BattlePanel.UI_BG_COLOR;
        borderColor = BattlePanel.UI_BORDER_COLOR;
        fgColor = BattlePanel.UI_FG_COLOR;
        font = BattlePanel.GAME_FONT_SMALL;

        width = 0; height = 0;
        leftMargin = 0; rightMargin = 0; topMargin = 0; bottomMargin = 0;
        leftPad = 0; rightPad = 0; topPad = 0; bottomPad = 0;
    }

    // ====================================================================================================
    // MAIN DRAW
    /** Draw this element on the screen. **/
    public void draw(Graphics g){
        int x = getX(); int y = getY();
        int width = getDisplayWidth(); int height = getDisplayHeight();

        g.setColor(bgColor);
        g.fillRect(x, y, width, height);
        g.setColor(borderColor);
        g.drawRect(x, y, width, height);

        if (text != null){
            FontMetrics metrics = g.getFontMetrics(font);
            int sx = x; int sy = y;
            switch (getTextAlignment().x){
                case 1: sx += (width - metrics.stringWidth(text)) / 2; break;
                case 2: sx += (width - metrics.stringWidth(text) - getRightPad()); break;
            }
            switch (getTextAlignment().y){
                case 1: sy += (height - metrics.getHeight()) / 2; break;
                case 2: sy += (height - metrics.getHeight() - getBottomPad()); break;
            }

            g.setFont(font);
            g.setColor(fgColor);
            g.drawString(text, sx, sy + metrics.getAscent());
        }

        occupiedWidth = 0;
        occupiedHeight = 0;
        for (ElementBox child : children) {
            child.draw(g);

            if (getAlignment().x == 1){
                occupiedHeight += child.getTotalHeight();
            } else {
                occupiedWidth += child.getTotalWidth();
            }
        }
    }

    // ====================================================================================================
    // CALCULATION METHODS
    public int getX(){
        switch(getAlignment().x){
            case 0: return parent.getX() + parent.getLeftPad() + parent.getOccupiedWidth() + getLeftMargin();
            case 1: return parent.getX() + parent.getLeftPad() + (parent.getInnerWidth() - parent.getTotalContainedWidth())/2 + parent.getOccupiedWidth() + getLeftMargin();
            case 2: return parent.getX() + parent.getTotalWidth() - parent.getRightPad() - parent.getOccupiedWidth() - getTotalWidth() + getLeftMargin();
        }
        return 0;
    }

    public int getY(){
        switch(getAlignment().y){
            case 0: return parent.getY() + parent.getTopPad() + parent.getOccupiedHeight() + getTopMargin();
            case 1: return parent.getY() + parent.getTopPad() + (parent.getInnerHeight() - parent.getTotalContainedHeight())/2 + parent.getOccupiedHeight() + getTopMargin();
            case 2: return parent.getY() + parent.getTotalHeight() - parent.getBottomPad() - parent.getOccupiedHeight() - getTotalHeight() + getTopMargin();
        }
        return 0;
    }

    /** Get this element's combined width, x padding and x margin. **/
    public int getOuterWidth(){
        return getWidth() + getLeftPad() + getRightPad() + getLeftMargin() + getRightMargin();
    }

    /** Get this element's combined height, y padding and y margin. **/
    public int getOuterHeight(){
        return getHeight() + getTopPad() + getBottomPad() + getTopMargin() + getBottomMargin();
    }

    /** Get the total width space inside this element. **/
    public int getInnerWidth(){
        return Math.max(getWidth(), getTotalContainedWidth());
    }

    /** Get the total width space inside this element. **/
    public int getInnerHeight(){
        return Math.max(getHeight(), getTotalContainedHeight());
    }

    /** Get the width inside this element's border. (width + xpad) **/
    public int getDisplayWidth(){
        return getWidth() + getLeftPad() + getRightPad();
    }

    /** Get the height inside this element's border. (height + ypad) **/
    public int getDisplayHeight(){
        return getHeight() + getTopPad() + getBottomPad();
    }


    /** Get the total combined width of all subchildren of this element. **/
    public int getTotalContainedWidth(){
        int total = 0;
        for (ElementBox child : children){
            total += child.getTotalWidth();
        }
        return total;
    }

    /** Get the total combined height of all subchildren of this element. **/
    public int getTotalContainedHeight(){
        int total = 0;
        for (ElementBox child : children){
            total += child.getTotalHeight();
        }
        return total;
    }

    /** Get the total combined full width, including subchildren that may be stretching this box wider. **/
    public int getTotalWidth(){
        return Math.max(getWidth(), getTotalContainedWidth()) + getLeftPad() + getRightPad() + getLeftMargin() + getRightMargin();
    }

    /** Get the total combined full height, including all subchildren that may be stretching this box taller. **/
    public int getTotalHeight(){
        return Math.max(getHeight(), getTotalContainedHeight()) + getTopPad() + getBottomPad() + getTopMargin() + getBottomMargin();
    }

    /** Get the alignment that this element should use (based on parent). **/
    public Alignment getAlignment(){
        return alignment;
    }

    /** Get text alignment. If textAlign is null (was not specified) then it uses this element's alignment. **/
    public Alignment getTextAlignment(){
        if (textAlign == null) return getAlignment();
        return textAlign;
    }

    // ====================================================================================================
    // INTERACTION METHODS
    public void setDimensions(Number width, Number height){
        setWidth(width);
        setHeight(height);
    }

    public void setMargin(Number left, Number right, Number top, Number bottom){
        leftMargin = left;
        rightMargin = right;
        topMargin = top;
        bottomMargin = bottom;
    }
    public void setMargin(Number wide, Number high){
        setMargin(wide, wide, high, high);
    }
    public void setMargin(Number val){
        setMargin(val, val, val, val);
    }

    public void setPad(Number left, Number right, Number top, Number bottom){
        leftPad = left;
        rightPad = right;
        topPad = top;
        bottomPad = bottom;
    }
    public void setPad(Number wide, Number high){
        setPad(wide, wide, high, high);
    }
    public void setPad(Number val){
        setPad(val, val, val, val);
    }

    // UTILITY
    public void addChild(ElementBox child){
        children.add(child);
    }

    public void printEverything(){
        System.out.println(this);
        System.out.printf("(%d, %d) %dx%d%n", getX(), getY(), getWidth(), getHeight());
        System.out.printf("margin: %d,%d,%d,%d%n", getLeftMargin(), getRightMargin(), getTopMargin(), getBottomMargin());
        System.out.printf("padding: %d,%d,%d,%d%n", getLeftPad(), getRightPad(), getTopPad(), getBottomPad());
        System.out.printf("alignment: %s    text alignment: %s%n", getAlignment(), getTextAlignment());
        System.out.println();
    }

    public Number outerWidth(Number width) {
        return new ShrinkValue(width, leftMargin, leftPad, rightPad, rightMargin);
    }

    public Number outerHeight(Number height) {
        return new ShrinkValue(height, topMargin, topPad, bottomPad, bottomMargin);
    }

    public void fillWidth(Number width){
        this.width = outerWidth(width);
    }

    public void fillHeight(Number height){
        this.height = outerHeight(height);
    }

    public StretchValue getWidthStretch(){
        return new StretchValue(width, leftPad, rightPad);
    }

    // ====================================================================================================
    // SETTERS
    public void setWidth(Number width) {
        this.width = width;
    }

    public void setHeight(Number height) {
        this.height = height;
    }

    public void setTopMargin(Number topMargin) {
        this.topMargin = topMargin;
    }

    public void setBottomMargin(Number bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public void setLeftMargin(Number leftMargin) {
        this.leftMargin = leftMargin;
    }

    public void setRightMargin(Number rightMargin) {
        this.rightMargin = rightMargin;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public void setTopPad(Number topPad) {
        this.topPad = topPad;
    }

    public void setBottomPad(Number bottomPad) {
        this.bottomPad = bottomPad;
    }

    public void setLeftPad(Number leftPad) {
        this.leftPad = leftPad;
    }

    public void setRightPad(Number rightPad) {
        this.rightPad = rightPad;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setTextAlignment(Alignment textAlign) {
        this.textAlign = textAlign;
    }

    // ====================================================================================================
    // ACCESSORS
    public int getWidth() {
        return width.intValue();
    }

    public int getHeight() {
        return height.intValue();
    }

    public int getTopPad() {
        return topPad.intValue();
    }

    public int getBottomPad() {
        return bottomPad.intValue();
    }

    public int getLeftPad() {
        return leftPad.intValue();
    }

    public int getRightPad() {
        return rightPad.intValue();
    }

    public int getTopMargin() {
        return topMargin.intValue();
    }

    public int getBottomMargin() {
        return bottomMargin.intValue();
    }

    public int getLeftMargin() {
        return leftMargin.intValue();
    }

    public int getRightMargin() {
        return rightMargin.intValue();
    }

    public int getOccupiedWidth() {
        return occupiedWidth;
    }

    public int getOccupiedHeight() {
        return occupiedHeight;
    }

    public Color getFgColor() {
        return fgColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    public ElementBox getParent() {
        return parent;
    }

    public ArrayList<ElementBox> getChildren() {
        return children;
    }

    public boolean hasBorder() {
        return hasBorder;
    }
}
