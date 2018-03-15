
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] siteGrid;
    private int vTop;
    private int vBottom;
    private int size;
    private WeightedQuickUnionUF mQF;
    
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Percolation:Percolation:Invalid grid size");
        size = n;
        vTop = 0;
        vBottom = size*size + 1; 
        
        siteGrid = new boolean[size][size];
        
        for (int i = 0, j = size-1; i < size && j >= 0; i++, j--) {
            siteGrid[i][j] = false;
        }
        mQF = new WeightedQuickUnionUF(size*size + 2);  // n*n + virtual top site +virtual bottom site
    }
    
    public void open(int row, int col) {
        if (!isValidRange(row, col)) {
            throw new IndexOutOfBoundsException("Percolation:Open:Invalid inputs"+ row+","+col);
        }
        if (isOpen(row, col))
            return;
        siteGrid[row-1][col-1] = true;
        
        if (row == 1) {
            mQF.union(getIndex(row, col), vTop);
        }
        
        if (row == size) {
             mQF.union(getIndex(row, col), vBottom);
        }
        
        if (col > 1 && isValidRange(row, col-1) && isOpen(row, col-1))
            mQF.union(getIndex(row, col-1), getIndex(row, col));
        
        if (col < size && isValidRange(row, col+1) && isOpen(row, col+1))
            mQF.union(getIndex(row, col+1), getIndex(row, col));
        
        if (row > 1 && isValidRange(row-1, col) && isOpen(row-1, col))
            mQF.union(getIndex(row-1, col), getIndex(row, col));
        
        if (row < size && isValidRange(row+1, col) && isOpen(row+1, col))
            mQF.union(getIndex(row+1, col), getIndex(row, col));
        
    }
    
    public boolean isOpen(int row, int col) {
        if (!isValidRange(row, col)) {
            throw new IndexOutOfBoundsException("Percolation:isOpen:Invalid inputs" + row + "," + col);
        }
        return siteGrid[row-1][col-1];
    }
    
    public boolean isFull(int row, int col) {
        if (!isValidRange(row, col)) {
            throw new IndexOutOfBoundsException("Percolation:isFull:Invalid inputs"+ row+","+col);
        }
        if (mQF.connected(vTop, getIndex(row, col)))
            return true;
        return false;
    }
    
   
    public boolean percolates() {
        if (mQF.connected(vTop, vBottom))
            return true;
        return false;
    }
        
    private boolean isValidRange(int row, int col) {
        if (row > 0 && row <= size && col > 0 && col <= size) {
            return true;
        }
        return false;
    }
    
    private int getIndex(int row, int col) {
        return (size*(row-1)+col);
    }
}
