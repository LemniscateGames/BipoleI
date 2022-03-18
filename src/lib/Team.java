package lib;

import lib.gameplay.tiletypes.ResponsiveTile;
import lib.shop.ShopItem;
import lib.ui.ElementBox;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;
import static lib.display.ColorUtils.makeTransparent;

public class Team {
    // Core colors
    private Color color, unitColor, pointColor;

    // Colors generated during initialization that are related to core colors
    private Color tileFillColor, fadedPointColor, darkPointColor, darkFadedPointColor;

    /** The amount of points this team has. **/
    private int points = 5;

    public Team(Color color, Color unitColor, Color pointColor) {
        this.color = color;
        this.unitColor = unitColor;
        this.pointColor = pointColor;

        tileFillColor = makeTransparent(color, 0.3);
        fadedPointColor = blendColors(pointColor, Color.GRAY, 0.5);
        darkPointColor = blendColors(pointColor, ElementBox.UI_BORDER_COLOR, 0.5);
        darkFadedPointColor = blendColors(darkPointColor, ElementBox.UI_BORDER_COLOR_LIGHTER, 0.5);
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

    public Color getColor(double brightness, double saturation, double dimness){
        return generateColor(brightness, saturation, dimness, color);
    }
    public Color getColor(double brightness, double saturation){
        return generateColor(brightness, saturation, 0, color);
    }

    public Color getTileFillColor(double brightness, double saturation, double dimness) {
        return generateColor(brightness, saturation, dimness, tileFillColor);
    }
    public Color getTileFillColor(double brightness, double saturation) {
        return generateColor(brightness, saturation, 0, tileFillColor);
    }

    private Color generateColor(double brightness, double saturation, double dimness, Color color) {
        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

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

        if (dimness > 0){
            newColor = blendColors(newColor, ResponsiveTile.DIM_COLOR, dimness);
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

    public Color getFadedPointColor() {
        return fadedPointColor;
    }

    public Color getDarkPointColor() {
        return darkPointColor;
    }

    public Color getDarkFadedPointColor() {
        return darkFadedPointColor;
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
