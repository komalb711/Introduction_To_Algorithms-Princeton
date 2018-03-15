import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

public class FastCollinearPoints {
    private HashMap<Double, List<Point>> foundSegments = new HashMap<Double, List<Point>>();
    private List<LineSegment> lineSegment = new ArrayList<LineSegment>();
    
    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null)
            throw new NullPointerException("Points list can't be null");
        
        checkDuplicatePoints(points);
        
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        
        HashMap<Double, List<Point>> slopePointMap = new HashMap<Double, List<Point>>();
        
        for (int i = 0; i < pointsCopy.length; i++)
        {
//            Arrays.sort(pointsCopy, pointsCopy[i].slopeOrder());
            slopePointMap.clear();
            for (int j = 0; j < pointsCopy.length; j++)
            {
                double slope = pointsCopy[i].slopeTo(pointsCopy[j]);
                List<Point> list;
                if (slopePointMap.containsKey(slope))
                {
                    list = slopePointMap.get(slope);
                    list.add(pointsCopy[j]);
                }
                else
                {
                    list = new ArrayList<Point>();
                    list.add(pointsCopy[i]);
                    list.add(pointsCopy[j]);
                    slopePointMap.put(slope, list);
                }
            }
            
            for (double key : slopePointMap.keySet()) 
            {
                List<Point> list = slopePointMap.get(key);
                if (list.size() >= 4)
                {
                    addSegmentIfNew(key, list);
                }
            }   
        }
    }
    
    private void addSegmentIfNew(double slope, List<Point> list)
    {
        if (!foundSegments.containsKey(slope))
        {
            Collections.sort(list);
            Point startPoint = list.get(0);
            Point endPoint = list.get(list.size()-1);
            lineSegment.add(new LineSegment(startPoint, endPoint));
            foundSegments.put(slope, list);
        }
        else
        {
            List<Point> existingList = foundSegments.get(slope);
            boolean toAdd = true;
            for (Point point : list)
            {
                if (existingList.contains(point))
                {
                    toAdd = false;
                    break;
                }
            }
            if (toAdd)
            {
                Collections.sort(list);
                Point startPoint = list.get(0);
                Point endPoint = list.get(list.size()-1);
                lineSegment.add(new LineSegment(startPoint, endPoint));
                existingList.addAll(list);
            }    
        }
    }
    
    public int numberOfSegments()        // the number of line segments
    {
        if (lineSegment == null)
            return 0;
        return lineSegment.size();
    }
    
    public LineSegment[] segments()                // the line segments
    {
        return lineSegment.toArray(new LineSegment[lineSegment.size()]);
    }
    
    private void checkDuplicatePoints(Point[] points)
    {
        for (int i = 0; i < points.length; i++)
        {
            for (int j = i+1; j < points.length; j++)
            {
                if (points[i] == null || points[j] == null)
                {
                    throw new NullPointerException("Point can't be null");
                }
                if (points[i].compareTo(points[j]) == 0)
                {
                    throw new IllegalArgumentException("Duplicate Entries" + points[i].toString());
                }
            }
        }
    }
}