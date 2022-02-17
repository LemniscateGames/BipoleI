package lib.misc;

public class RowColPoint {
    public final int row, col;

    public RowColPoint(int row, int col){
        this.row = row;
        this.col = col;
    }

    public String toString(){
        return String.format("{r=%d, c=%d}", row, col);
    }
}
