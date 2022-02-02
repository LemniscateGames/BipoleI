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

    public Team(int id, Color color, Color unitColor){
        this.id = id;
        this.points = 0;
        this.color = color;
        this.unitColor = unitColor;

        this.brightColor = color.brighter();
        this.brightUnitColor = unitColor.brighter();
    }

    public Team(int id){
        this(id,
                id==0 ? new Color(60,120,225)
                        : new Color(210,65,70),
                id==0 ? new Color(12,16,24)
                        : new Color(24,12,16));
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

    public Color getColor(boolean bright) {
        return bright ? brightColor : color;
    }

    public Color getUnitColor(boolean bright) {
        return bright ? brightUnitColor : unitColor;
    }
}
