package lib.display;

import java.awt.*;

public class ColorUtils {
    public static Color blendColors(Color base, Color blend, double percent){
        return new Color(
                (int)(base.getRed() + (blend.getRed() - base.getRed())*percent),
                (int)(base.getGreen() + (blend.getGreen() - base.getGreen())*percent),
                (int)(base.getBlue() + (blend.getBlue() - base.getBlue())*percent),
                (int)(base.getAlpha() + (blend.getAlpha() - base.getAlpha())*percent)
        );
    }

    public static Color makeTransparent(Color base, double percent){
        return new Color(
                base.getRed(),
                base.getGreen(),
                base.getBlue(),
                (int)(base.getAlpha() * percent)
        );
    }
}
