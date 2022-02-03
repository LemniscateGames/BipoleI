package BipoleI.lib;

import BipoleI.BattlePanel;

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

    private final Color grayedColor;
    private final Color brightGrayedColor;

    private final Color pointColor;

    private Shop shop;

    public Team(int id, Color color, Color unitColor, Color pointColor){
        this.id = id;
        this.points = 0;
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;

        brightColor = color.brighter();
        brightUnitColor = unitColor.brighter();

        grayedColor = BattlePanel.blendColors(BattlePanel.GRAY_COLOR, color, 0.4);
        brightGrayedColor = BattlePanel.blendColors(BattlePanel.GRAY_COLOR, brightColor, 0.4);

        shop = Shop.generateShop(this);
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

    public Shop getShop() {
        return shop;
    }
}
