package lib;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;
import static lib.display.ColorUtils.makeTransparent;

public class Team {
    // Core colors
    private Color color, unitColor, pointColor;

    // Colors generated during initialization that are related to core colors
    private Color tileFillColor;

    public Team(Color color, Color unitColor, Color pointColor) {
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;

        this.tileFillColor = makeTransparent(color, 0.3);
    }

    // Accessors
    public Color getColor() {
        return color;
    }

    public Color getUnitColor() {
        return unitColor;
    }

    public Color getPointColor() {
        return pointColor;
    }

    public Color getTileFillColor() {
        return tileFillColor;
    }

    // Utility
    public static Team quickTeam(Color color, Color pointColor){
        return new Team(
                color,
                blendColors(Color.BLACK, color, 0.1),
                pointColor
        );
    }

    public static Team defaultAllies(){
        return quickTeam(
                new Color(72, 132, 234),
                new Color(110,215,85)
        );
    }

    public static Team defaultEnemies(){
        return quickTeam(
                new Color(231, 75, 81),
                new Color(220,145,80)
        );
    }
}
