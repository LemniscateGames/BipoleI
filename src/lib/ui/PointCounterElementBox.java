package lib.ui;

import lib.display.AnimatedValue;
import lib.display.ColorUtils;
import lib.display.TimingFunction;
import lib.display.timing.SineShake;

import java.awt.*;

public class PointCounterElementBox extends ElementBox {
    public static final Color SHAKE_COLOR = new Color(190, 81, 74);
    public static final double SHAKE_SPEED = 20;
    public static final double SHAKE_DECAY = 2.5;
    public static final int SHAKE_TIME = 375;
    public static final double SHAKE_DISTANCE = 20;
    private Number cannotAffordFade = 0;
    private Number cannotAffordShake = 0;

    public PointCounterElementBox(int x, int y, int width, int height) {
        super(x, y, width, height);
        setFont(ElementBox.GAME_FONT_BIG);
    }

    /** Shake this element's text. Call when trying to buy an item that you can't afford. **/
    public void shake(){
        // Do not start a shake if already shaking
//        if (cannotAffordFade instanceof AnimatedValue && ((AnimatedValue) cannotAffordFade).isAnimating()) return;

        cannotAffordFade = new AnimatedValue(TimingFunction.EASE_OUT, SHAKE_TIME, 1.0, 0.0);
        cannotAffordShake = new AnimatedValue(new SineShake(SHAKE_SPEED, SHAKE_DECAY), SHAKE_TIME, 0.0, 1.0);
    }

    @Override
    public Color getFg() {
        if (cannotAffordFade.doubleValue() > 0.0){
            return ColorUtils.blendColors(super.getFg(), SHAKE_COLOR, cannotAffordFade.doubleValue());
        } else {
            return super.getFg();
        }
    }

    @Override
    public void draw(Graphics g) {
        beforeDraw();

        if (!isTransparentBg()){
            g.setColor(getBg());
            g.fillRect(getX(), getY(), getWidth(), getHeight());

            g.setColor(getBorder());
            g.drawRect(getX(), getY(), getWidth(), getHeight());
        }

        int textOffset;
        if (cannotAffordShake.doubleValue() != 0.0){
            textOffset = (int)(cannotAffordShake.doubleValue()*SHAKE_DISTANCE);
        } else {
            textOffset = 0;
        }

        if (getText() != null){
            FontMetrics metrics = g.getFontMetrics(getFont());
            int strX = getX() + (getWidth() - metrics.stringWidth(getText()[0])) / 2;
            int strY = getY() + ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

            g.setColor(getFg());
            g.setFont(getFont());
            g.drawString(getText()[0], strX + textOffset, strY);
        }
    }
}
