package lib;

import lib.shop.ShopItem;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;
import static lib.display.ColorUtils.makeTransparent;

public class Team {
    // Core colors
    private Color color, unitColor, pointColor;

    // Colors generated during initialization that are related to core colors
    private Color tileFillColor;

    /** The amount of points this team has. **/
    private int points;

    public Team(Color color, Color unitColor, Color pointColor) {
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;

        this.tileFillColor = makeTransparent(color, 0.3);
    }

    // Interaction
    public void addPoints(int amount){
        points += amount;
    }
    public boolean subtractPoints(int amount){
        if (amount <= points){
            points -= amount;
            return true;
        } else {
            return false;
        }
    }

    public Color getColor(double brightness, double saturation){
        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue());

        if (brightness > 0){
            newColor = blendColors(newColor, Color.WHITE, brightness);
        } else if (brightness < 0){
            newColor = blendColors(newColor, Color.BLACK, -brightness);
        }

        if (saturation > 0){
            newColor = blendColors(newColor, Color.YELLOW, saturation);
        } else if (saturation < 0){
            newColor = blendColors(newColor, Color.DARK_GRAY, -saturation);
        }

        return newColor;
    }

    // Information
    public boolean canAfford(int points){
        return points <= this.points;
    }

    public boolean canAfford(ShopItem item){
        return item.getCost() <= points;
    }

    public boolean canBuy(ShopItem item){
        return canAfford(item);
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

    public int getPoints() {
        return points;
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
