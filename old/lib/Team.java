package old.BipoleI.lib;

import old.BipoleI.lib.battlepanel.BattlePanel;
import old.BipoleI.lib.shop.Shop;

import java.awt.*;

public class Team {
    /** Team's internal ID. Usually 0 for allies, 1 for enemies. **/
    private final int id;

    /** The amount of points this team has. Used to buy units. **/
    private int points;

    private final Color color;
    private final Color unitColor;
    private final Color pointColor;

    private final Color brokeColor;

    private Shop shop;

    public Team(int id, Color color, Color unitColor, Color pointColor){
        this.id = id;
        this.points = 5;
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;

        this.brokeColor = BattlePanel.blendColors(pointColor, new Color(128, 128, 128), 0.5);

        shop = Shop.generateShop(this);
    }

    public Team(int id){
        this(id,
                id==0 ? new Color(41, 110, 231)
                        : new Color(226, 50, 56),
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

    public Color getColor(Number brightness) {
        return BattlePanel.blendColors(color, BattlePanel.LIGHT_COLOR, brightness);
    }
    public Color getColor(Number brightness, Number grayness) {
        return BattlePanel.blendColors(getColor(brightness), BattlePanel.GRAY_COLOR, grayness);
    }
    public Color getColor(){
        return getColor(0.0, 0.0);
    }

    // Yes brightness and grayness both control blackness, dont ask me why that makes sense
    public Color getUnitColor(Number brightness) {
        return BattlePanel.blendColors(unitColor, Color.BLACK, brightness);
    }
    public Color getUnitColor(Number brightness, Number grayness) {
        return BattlePanel.blendColors(getUnitColor(brightness), Color.BLACK, grayness);
    }
    public Color getUnitColor(){
        return getUnitColor(0.0, 0.0);
    }

    public Color getPointColor(){
        return pointColor;
    }

    public Color getBrokeColor() {
        return brokeColor;
    }

    public Shop getShop() {
        return shop;
    }
}
