package lib;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;

public class Team {
    private Color color, unitColor, pointColor;

    public Team(Color color, Color unitColor, Color pointColor) {
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;
    }



    public static Team quickTeam(Color color, Color pointColor){
        return new Team(
                color,
                blendColors(Color.BLACK, color, 0.1),
                pointColor
        );
    }

    public static Team defaultAllies(){
        return quickTeam(
                new Color(41, 110, 231),
                new Color(110,215,85)
        );
    }

    public static Team defaultEnemies(){
        return quickTeam(
                new Color(226, 50, 56),
                new Color(220,145,80)
        );
    }
}
