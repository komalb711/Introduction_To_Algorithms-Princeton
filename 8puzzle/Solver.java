import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;

public class Solver {
    
    private MinPQ<SearchNode> initPQ, twinPQ;
    
    private SearchNode finalNode = null;
    private boolean solvable = false;
    
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        initPQ = new MinPQ<SearchNode>();
        twinPQ = new MinPQ<SearchNode>();
        
        initPQ.insert(new SearchNode(initial, null));
        twinPQ.insert(new SearchNode(initial.twin(), null));
        
        SearchNode initialNode, twinNode;
        
        while (true)
        {
            initialNode = initPQ.delMin();
            twinNode = twinPQ.delMin();
            
            if (initialNode.board.isGoal())
            {
                finalNode = initialNode;
                solvable = true;
                break;
            }    
            
            if (twinNode.board.isGoal())
            {
                finalNode = twinNode;
                solvable = false;
                break;
            }  
            
            for (Board board : initialNode.board.neighbors())
            {
                if (initialNode.prev == null || !board.equals(initialNode.prev.board))
                {
                    initPQ.insert(new SearchNode(board, initialNode));
                }
            }
            
            for (Board board : twinNode.board.neighbors())
            {
                if (twinNode.prev == null || !board.equals(twinNode.prev.board))
                {
                    twinPQ.insert(new SearchNode(board, twinNode));
                }
            }
            
        }
        
    }
    
    public boolean isSolvable()            // is the initial board solvable?
    {
        return solvable;
    }
    
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if (isSolvable() && finalNode != null)
            return finalNode.moves;
        return -1;
        
    }
    
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (isSolvable())
        {
            Stack<Board> solutionSteps = new Stack<Board>();
            SearchNode curr = finalNode;
            while (curr != null)
            {
                solutionSteps.push(curr.board);
                curr = curr.prev;
            }
            Stack<Board> finalSol = new Stack<Board>();

            while (!solutionSteps.empty())
            {
                finalSol.push(solutionSteps.pop());
            }
            return finalSol;
        }
        return null;
    }
    
    private class SearchNode implements Comparable<SearchNode>
    {
        public Board board;
        public int moves = 0;
        public SearchNode prev;
        
        public SearchNode(Board b, SearchNode p)
        {
            board = b;
            prev = p;
            if (prev == null)
                moves = 0;
            else
                moves = prev.moves + 1;
        }
        
        public int compareTo(SearchNode that)
        {
            int thisDistance = this.board.manhattan() + this.moves;
            int thatDistance = that.board.manhattan() + that.moves;
            
            return thisDistance - thatDistance;
            
        }
    }
    
    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        
    }
}
