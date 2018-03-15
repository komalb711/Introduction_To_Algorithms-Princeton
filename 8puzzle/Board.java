import java.util.*;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Board {
    
    private int boardSize;
    private int[][] tiles;
    private int[][] goal;
    
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    {
        boardSize = blocks.length;
        tiles = new int[boardSize][boardSize];
        goal = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                tiles[i][j] = blocks[i][j];
                goal[i][j] = (i * boardSize + j + 1) % (boardSize * boardSize);
            }
        }
    }
    
    public int dimension()                 // board dimension n
    {
        return boardSize;
    }
    
    public int hamming()                   // number of blocks out of place
    {
        int count = 0;
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (tiles[i][j] != 0 && tiles[i][j] != goal[i][j])
                {
                    count++;
                }           
            }
        }
        return count;
    }
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int distance = 0;
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (tiles[i][j] != 0)
                {
                    int row = (tiles[i][j] - 1) / boardSize;
                    int col = (tiles[i][j] - 1) % boardSize;
                    distance = distance + Math.abs(i-row);
                    distance = distance + Math.abs(j-col);
                }           
            }
        }
        return distance;
    }
    
    public boolean isGoal()                // is this board the goal board?
    {
        boolean status = true;
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (tiles[i][j] != goal[i][j])
                {
                    status = false;
                    break;
                }           
            }
        }
        return status;
    }
    
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        Board twinBoard = new Board(tiles);
        // Switch row 1 if there's an empty block in first blocks of row 0
        if (tiles[0][0] == 0 || tiles[0][1] == 0) {
            twinBoard.exchange(1, 0, 1, 1);
        }   
        // Switch row 0 if there's no empty block in first blocks of row 0
        else {
            twinBoard.exchange(0, 0, 0, 1);
        }
       return twinBoard;
    }
    
    public boolean equals(Object obj)        // does this board equal y?
    {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        
        Board that = (Board) obj;
        boolean status = true;
        
        int thatRowCount = that.getBlockArray().length;
        int thatColCount = that.getBlockArray()[0].length;
        
        if (thatRowCount != boardSize || thatColCount != boardSize)
            return false;
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (tiles[i][j] != that.getTileValueAt(i, j))
                {
                    status = false;
                    break;
                }           
            }
        }
        return status;
    }
    
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        LinkedList<Board> q = new LinkedList<Board>();
        
        Board copy;
        
        int[] loc = locationOfEmptyBlock();
        
        int row = loc[0];
        int col = loc[1];
        
        if (row < boardSize - 1)
        {
            copy = new Board(tiles);
            copy.exchange(row, col, row + 1, col);
            q.add(copy);
        }
        if (row > 0)
        {
            copy = new Board(tiles);
            copy.exchange(row, col, row - 1, col);
            q.add(copy);
        }
        if (col < boardSize - 1)
        {
            copy = new Board(tiles);
            copy.exchange(row, col, row, col + 1);
            q.add(copy);
        }
        if (col > 0)
        {
            copy = new Board(tiles);
            copy.exchange(row, col, row, col - 1);
            q.add(copy);
        }
      
        return q;
    }
    
    private int[] locationOfEmptyBlock()
    {
        int[] loc = new int[2];
        
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (tiles[i][j] == 0)
                {
                    loc[0] = i;
                    loc[1] = j;
                    break;
                }           
            }
        }
        return loc;
    }
    
    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(boardSize + "\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
        
    }
    
    private int getTileValueAt(int row, int col)
    {
        return tiles[row][col];
    }

    private int[][] getBlockArray()
    {
        return tiles;
    }
    
    private void exchange(int row1, int col1, int row2, int col2)
    {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }
    
    public static void main(String[] args) // unit tests (not graded)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
         StdOut.println("Manhattan Distance:" + initial.manhattan());
    }
}
