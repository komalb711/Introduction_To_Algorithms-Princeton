import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    
    private List<LineSegment> lineSegment = new ArrayList<LineSegment>();
    
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new NullPointerException("Points list can't be null");
        
        checkDuplicatePoints(points);
        
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        
        Arrays.sort(pointsCopy);
        
        for (int i = 0; i < pointsCopy.length; i++)
        {
            for (int j = i+1; j < pointsCopy.length; j++)
            {
                for (int k = j+1; k < pointsCopy.length; k++)
                {
                    double ijSlope = pointsCopy[i].slopeTo(pointsCopy[j]);
                    double ikSlope = pointsCopy[i].slopeTo(pointsCopy[k]);
                    if (ijSlope != ikSlope)
                    {
                        continue;
                    }
                    for (int l = k+1; l < pointsCopy.length; l++)
                    {
                        double ilSlope = pointsCopy[i].slopeTo(pointsCopy[l]);
                        if (ijSlope == ilSlope)
                        {
                            lineSegment.add(new LineSegment(pointsCopy[i], pointsCopy[l]));
                        }
                    }
                }
            }   
        }
        
//        mLineSegments = removeSubSegmentsFromList(mLineSegments);
        
//        lineSegment = new LineSegment[mLineSegments.size()];
//        lineSegment = mLineSegments.toArray(lineSegment);
        
    }
    
//    public ArrayList<LineSegment> removeSubSegmentsFromList(ArrayList<LineSegment> lineSegmentList)
//    {
//        ArrayList<LineSegment> updatedList = new ArrayList<LineSegment>();
//        /*remove subsegments from the line segment list*/   
//        for(int i = 0; i < lineSegmentList.size(); i++)
//        {
//            LineSegment line = lineSegmentList.get(i);
//            int lineSlope = line.p.slopeTo(line.q);
//            double maxDist = line.findSegmentLength();
//            int maxDistIndex = i;
//            boolean sameSlopeLinesPresent = false;
//            boolean betterOptionFound = false;
//            for( int j = i+1; j < lineSegmentList.size(); j++)
//            {
//                LineSegment lne = lineSegmentList.get(j);
//                int slope = lne.p.slopeTo(line.q);
//                if (slope == lineSlope)
//                {
//                    sameSlopeLinesPresent = true;
//                    double dist = lne.findSegmentLength();
//                    if (dist > maxDist)
//                    {
//                        maxDist = dist;
//                        maxDistIndex = j;
//                        betterOptionFound = true;
//                    }
//                }
//            }
//            if (betterOptionFound)
//            {       
//                updatedList.add(lineSegmentList.get(maxDistIndex));
//            }
//            else if (!sameSlopeLinesPresent) 
//            {
//                updatedList.add(lineSegmentList.get(i));
//            }
//        }
//        return updatedList;
//    }
//    public double findSegmentLength(LineSegment line)
//    {
//        double length = line.p.findDistanceBetween(line.q);
//        return length;
//    }
    
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