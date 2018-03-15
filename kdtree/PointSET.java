import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    
    private SET<Point2D> points;
    
    
    public PointSET()                               // construct an empty set of points 
    {
        points = new SET<Point2D>();
    }
    
    public boolean isEmpty()                      // is the set empty? 
    {
        return points.isEmpty();
    }
    
    public int size()                         // number of points in the set 
    {
        return points.size();
    }
    
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        validateInput(p, "Point cannot be null");
        points.add(p);
    }
    
    public boolean contains(Point2D p)            // does the set contain point p? 
    {
        validateInput(p, "Point cannot be null");
        return points.contains(p);
    }
    
    public void draw()                         // draw all points to standard draw 
    {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }
    
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
    {
        validateInput(rect, "Range rect cannot be null");
        
        List<Point2D> solution = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                solution.add(point);
            }
        }
        return solution;
    }
    
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
    {
        validateInput(p, "Point cannot be null");
        Point2D nearestPoint = null;
        for (Point2D point : points) {
            if (nearestPoint == null || point.distanceTo(p) < nearestPoint.distanceTo(p)) {
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    
    private static void validateInput(final Object obj, final String errorMessage)
    {
        if (obj == null)
            throw new NullPointerException(errorMessage);
    }
    
    public static void main(String[] args)                  // unit testing of the methods (optional) 
    {
        
    }
}