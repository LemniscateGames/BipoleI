package lib.misc;

import java.util.Objects;

public class RowColPoint {
    public final int row, col;

    public RowColPoint(int row, int col){
        this.row = row;
        this.col = col;
    }

    public String toString(){
        return String.format("{r=%d, c=%d}", row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RowColPoint that = (RowColPoint) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
