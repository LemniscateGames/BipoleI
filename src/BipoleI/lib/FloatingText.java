package BipoleI.lib;

import BipoleI.BattlePanel;

import java.awt.*;

/** Container for a number that shows up when dealing damage, generating gold, etc. over a unit. **/
public class FloatingText {
    private final float viewCol;
    private final float viewRow;
    private final String displayText;
    private final Color displayColor;
    private final int totalDuration;
    private final long timeCreated;

    public FloatingText(float col, float row, String text, Color color, int duration){
        viewCol = col;
        viewRow = row;
        displayText = text;
        displayColor = color;
        totalDuration = duration;
        timeCreated = System.currentTimeMillis();
    }
    public FloatingText(float col, float row, String text, Color color){
        this(col, row, text, color, 1000);
    }

    public void draw(BattlePanel panel, Graphics g, int z){
        FloatPoint pos = panel.tileScreenPos(viewCol, viewRow);
        int floatDistance = (int)(32 * percentComplete());

        g.setColor(displayColor);
        g.drawString(displayText, (int)pos.getX(), (int)pos.getY() - floatDistance + z/2);
    }

    public double percentComplete(){
        return (double)(System.currentTimeMillis()-timeCreated)/totalDuration;
    }

    public boolean expired(){
        return System.currentTimeMillis() >= timeCreated+totalDuration;
    }
}
