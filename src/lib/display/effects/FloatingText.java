package lib.display.effects;

import lib.Team;
import lib.display.AnimatedValue;
import lib.display.TimingFunction;
import lib.panels.BattlePanel;
import lib.ui.ElementBox;

import java.awt.*;

public class FloatingText extends Effect {
    private final String text;
    private final Color color;
    private final AnimatedValue animator;

    public FloatingText(BattlePanel panel, double row, double col, String text, Color color, double height, int duration) {
        super(panel, row, col);
        this.text = text;
        this.color = color;

        setDrawInFront(true);

        animator = new AnimatedValue(TimingFunction.EASE_BETTER, duration, 0, height);
    }

    public FloatingText(BattlePanel panel, double row, double col, String text, Color color, double height){
        this(panel, row, col, text, color, height, 750);
    }

    public FloatingText(BattlePanel panel, double row, double col, String text, Color color){
        this(panel, row, col, text, color, 1.0);
    }

    public FloatingText(BattlePanel panel, double row, double col, String text, Team team){
        this(panel, row, col, text, team.getPointColor());
    }

    public FloatingText(BattlePanel panel, double row, double col, String text){
        this(panel, row, col, text, Color.WHITE);
    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {
        g.setColor(color);

        g.drawString(text, (int)(x - g.getFontMetrics().stringWidth(text)/2), (int)(y + z/2 - animator.doubleValue()*z));
    }

    @Override
    public boolean isExpired() {
        return animator.isFinished();
    }
}
