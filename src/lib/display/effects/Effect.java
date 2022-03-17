package lib.display.effects;

import lib.panels.BattlePanel;

import java.awt.*;

public abstract class Effect {
    private final BattlePanel panel;
    private final double row, col;
    private boolean drawInFront;

    public Effect(BattlePanel panel, double row, double col){
        this.panel = panel;
        this.row = row;
        this.col = col;
    }

    public void drawOnGrid(Graphics g){
        draw(g, panel.getScreenX(row, col), panel.getScreenY(row, col), panel.getZoom().doubleValue());
    }

    public abstract void draw(Graphics g, double x, double y, double z);

    // Is your effect running? Then you better go catch it!
    public abstract boolean isExpired();

    public void clear(){
        panel.getEffects().remove(this);
    }

    public BattlePanel getPanel() {
        return panel;
    }

    public double getRow() {
        return row;
    }

    public double getCol() {
        return col;
    }

    public boolean isDrawInFront() {
        return drawInFront;
    }

    public void setDrawInFront(boolean drawInFront) {
        this.drawInFront = drawInFront;
    }
}
