import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    
    private int mSize;
    private int mTrials;
    private double[] openSiteFraction;
    
    public PercolationStats(int n, int trials) {
         if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("PercolationStats:PercolationStats:Invalid grid size or trial count");
        mSize = n;
        mTrials = trials;
        openSiteFraction = new double[mTrials];
        for (int i = 0; i < mTrials; i++) {
            Percolation p = new Percolation(mSize);
            int openSitesCount = 0;
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, mSize+1);
                int col = StdRandom.uniform(1, mSize);
                if (!p.isOpen(row, col))
                {
                    p.open(row, col);
                    openSitesCount++;
                }
            }
            openSiteFraction[i] = (double) openSitesCount/(mSize*mSize);
        }
        
    }
    
    public double mean() {
        return StdStats.mean(openSiteFraction);
    }
    
    public double stddev() {
        return StdStats.stddev(openSiteFraction);
    }
    
    public double confidenceLo() {
        return this.mean() - ((1.96 * this.stddev()) / Math.sqrt(mTrials));
    }
    
    public double confidenceHi() {
        return this.mean() + ((1.96 * this.stddev()) / Math.sqrt(mTrials));
    }
    
    public static void main(String[] args) {
        
        int gridSize = Integer.parseInt(args[0]);
        int trialCount = Integer.parseInt(args[1]);
        
        PercolationStats pStats = new PercolationStats(gridSize, trialCount);
        
        StdOut.println("Mean = " + pStats.mean());
        StdOut.println("Stddev = " + pStats.stddev());
        StdOut.println("95% confidence interval = " + pStats.confidenceLo() + "," + pStats.confidenceHi());
    }
}