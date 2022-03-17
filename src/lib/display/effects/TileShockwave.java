package lib.display.effects;

import lib.Team;
import lib.display.AnimatedValue;
import lib.display.ColorUtils;
import lib.display.TimingFunction;
import lib.panels.BattlePanel;

import java.awt.*;

public class TileShockwave extends Effect {
    private final AnimatedValue animator;
    private final double radius;
    private final Color waveEndColor;

    public TileShockwave(BattlePanel panel, double row, double col, Team team,
                         AnimatedValue animator, double radius) {
        super(panel, row, col);
        this.animator = animator;
        this.radius = radius;

        waveEndColor = new Color(
                team.getColor().getRed(),
                team.getColor().getGreen(),
                team.getColor().getBlue(),
                0
        );
    }

    public TileShockwave(BattlePanel panel, double row, double col, Color waveEndColor,
                         AnimatedValue animator, double radius) {
        super(panel, row, col);
        this.animator = animator;
        this.radius = radius;
        this.waveEndColor = new Color(
                waveEndColor.getRed(),
                waveEndColor.getGreen(),
                waveEndColor.getBlue(),
                0
        );
    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {
        Color waveColor = ColorUtils.blendColors(Color.WHITE, waveEndColor, animator.doubleValue());

        BattlePanel.drawInsetTile(g, x, y, z,
                1.0 + animator.doubleValue()*radius,
                waveColor
                );
    }

    @Override
    public boolean isExpired() {
        return animator.isFinished();
    }
}
