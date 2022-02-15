package lib.display;

import java.awt.*;

public class ColorUtils {
    public static Color blendColors(Color base, Color blend, double percent){
        return new Color(
                Math.round(base.getRed() + (blend.getRed() - base.getRed())*percent),
                Math.round(base.getGreen() + (blend.getGreen() - base.getGreen())*percent),
                Math.round(base.getBlue() + (blend.getBlue() - base.getBlue())*percent)
        );
    }
}
