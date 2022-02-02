package BipoleI.lib;

import java.awt.*;

public class Team {
    /** Team's internal ID. Usually 0 for allies, 1 for enemies. **/
    private final int id;

    /** The amount of points this team has. Used to buy units. **/
    private int points;

    private final Color color;
    private final Color unitColor;

    private final Color brightColor;
    private final Color brightUnitColor;

    private static final Color grayColor = new Color(80, 80, 80);

    private final Color grayedColor;
    private final Color brightGrayedColor;

    private final Color pointColor;

    public Team(int id, Color color, Color unitColor, Color pointColor){
        this.id = id;
        this.points = 0;
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;

        brightColor = color.brighter();
        brightUnitColor = unitColor.brighter();

        grayedColor = blendColors(grayColor, color, 0.5);
        brightGrayedColor = blendColors(grayColor, brightColor, 0.5);
    }

    public Team(int id){
        this(id,
                id==0 ? new Color(60,120,225)
                        : new Color(210,65,70),
                id==0 ? new Color(12,16,24)
                        : new Color(24,12,16),
                id==0 ? new Color(110,215,85)
                        : new Color(220,145,80)
        );
    }

    public int getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points){
        this.points += points;
    }

    public void subtractPoints(int points){
        this.points -= points;
    }

    public Color blendColors(Color c1, Color c2, double percent){
        return new Color(
                c1.getRed() + (int)((c2.getRed()-c1.getRed())*percent),
                c1.getGreen() + (int)((c2.getGreen()-c1.getGreen())*percent),
                c1.getBlue() + (int)((c2.getBlue()-c1.getBlue())*percent)
        );
    }

    public Color getColor(boolean bright, boolean grayed) {
        if (grayed){
            return bright ? brightGrayedColor : grayedColor;
        } else {
            return bright ? brightColor : color;
        }
    }

    public Color getUnitColor(boolean bright) {
        return bright ? brightUnitColor : unitColor;
    }

    public Color getPointColor(){
        return pointColor;
    }
}
