package BipoleI.lib;

public enum Alignment { TOP_LEFT(0, 0), TOP(1, 0), TOP_RIGHT(2, 0),
    LEFT(0, 1), CENTER(1, 1), RIGHT(2, 1),
    BOTTOM_LEFT(0, 2), BOTTOM(1, 2), BOTTOM_RIGHT(2, 2);

    public final int x;
    public final int y;

    Alignment(int x, int y) {
        this.x = x;
        this.y = y;
    }
}