import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
    
    private static class Node {
        private Point2D point;
        private Node left, right;
        private boolean isVertical;
        
        public Node(Point2D val, Node lNode, Node rNode, boolean isVertical)
        {
            this.point = val;
            this.left = lNode;
            this.right = rNode;
            this.isVertical = isVertical;
        }
    }
    
    private Node root;
    private int size;
    private RectHV board;
    
    public KdTree()                               // construct an empty set of points 
    {
        root = null;
        size = 0;
        board = new RectHV(0, 0, 1.0, 1.0);
    }
    
    public boolean isEmpty()                      // is the set empty? 
    {
        if (root == null || size == 0)
            return true;
        return false;
    }
    
    public int size()                         // number of points in the set 
    {
        return size;
    }
    
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        validateInput(p, "Point cannot be null");
        root = put(root, p, root.isVertical);
    }
    
    private Node put(Node node, Point2D point, boolean isVertical)
    {
        if (node == null)
        {
            size++;
            node = new Node(point, null, null, isVertical);
            return node;
        }
        
        if (node.point.x() == point.x() && node.point.y() == point.y()) {
            node.point = point;
        } else if ((node.isVertical && node.point.x() > point.x()) || (!node.isVertical && node.point.x() > point.x())) {
            node.left = put(node.left, point, !isVertical);
        } else {
            node .right = put(node.right, point, !isVertical);
        }
        return node;
    }
    
    public boolean contains(Point2D point)            // does the set contain point p? 
    {
        Node node = root;
        
        while (node != null) {
            if (node.point.x() == point.x() && node.point.y() == point.y()) {
                return false;
            } else if ((node.isVertical && node.point.x() > point.x()) || (!node.isVertical && node.point.x() > point.x())) {
                node = node.left;
            } else {
                node = node.right;
            } 
        }
        return false;
    }
    
    public void draw()                         // draw all points to standard draw 
    {
        StdDraw.setScale(0, 1);
        draw(root, board);
    }
    
    private void draw(Node node, RectHV rect) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            new Point2D(node.point.x(), node.point.y()).draw();
            StdDraw.setPenRadius();
            
            if (node.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                new Point2D(node.point.x(), rect.ymin()).drawTo(new Point2D(node.point.x(), rect.ymax()));
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                new Point2D(rect.xmin(), node.point.y()).drawTo(new Point2D(rect.xmax(), node.point.y()));
            }
            draw(node.left, leftRect(rect, node));
            draw(node.right, rightRect(rect, node));
        }
    }
    
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
    {
        validateInput(rect, "Range rect cannot be null");
        
        Stack<Point2D> points = new Stack<Point2D>();
        get(root, board, rect, points);
        return points;
    }
    
   private void get(Node node, RectHV nodeRect, RectHV rect, Stack<Point2D> pointsIn) {
        if (node != null) {
            if (nodeRect.intersects(rect)) {
                if (rect.contains(node.point)) pointsIn.push(node.point);
                get(node.left, leftRect(nodeRect, node), rect, pointsIn);
                get(node.right, rightRect(nodeRect, node), rect, pointsIn);
            }   
        }
    }
    
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
    {
        validateInput(p, "Point cannot be null");
        if (root == null)
            return null;
        return nearestNeigh(root, board, p, null);
    }
    
    private Point2D nearestNeigh(Node node, RectHV rect, Point2D point, Point2D nearPoint) {
        Point2D nearestPoint = nearPoint;
        if (node != null) {      
            if (nearestPoint == null || nearestPoint.distanceSquaredTo(point) > rect.distanceSquaredTo(point)) {
                if (nearestPoint == null) {
                    nearestPoint = node.point;
                } else {
                    if (node.point.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point)) {
                        nearestPoint = node.point;
                    }
                }
                
                if (!node.isVertical) {
                    RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                    RectHV rightRect = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                    if (point.x() <= node.point.x()) {
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                    } else {
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                    }
                } else {
                    RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                    RectHV rightRect = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                    if (point.y() <= node.point.y()) {
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                    } else {
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                    }
                }
            }
        }
        return nearestPoint;
    }
    
     private RectHV leftRect(RectHV rect, Node p) {
        if (!p.isVertical) {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.point.y());
        }
        return new RectHV(rect.xmin(), rect.ymin(), p.point.x(), rect.ymax());
    }
    
    private RectHV rightRect(RectHV rect, Node p) {
        if (!p.isVertical) {
            return new RectHV(rect.xmin(), p.point.y(), rect.xmax(), rect.ymax());
        }
        return new RectHV(p.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
    }   
    
    private static void validateInput(final Object obj, final String errorMessage)
    {
        if (obj == null)
            throw new NullPointerException(errorMessage);
    }
    
    public static void main(String[] args)                  // unit testing of the methods (optional) 
    {
        
    }
}public class KdTree {
    private Node root;
    private int size;
    private RectHV board;
    
    private class Node {
        private Point2D key;
        private Point2D val;
        private Node left, right;
        private boolean isHorizontal = false;
        
        public Node(Point2D key, Point2D val, Node left, Node right, boolean isHorizontal) {
            this.key = key;
            this.val = val;
            this.left = left;
            this.right = right;
            this.isHorizontal = isHorizontal;
        }
    }
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
        board = new RectHV(0, 0, 1.0, 1.0);
    }
    
    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points in the set
    public int size() {
       return size;
    }
    
    // add the point to the set (if it is not already in the set)
    // refer to BST put function
    public void insert(Point2D p) {
        root = put(root, p, p, false);
    }
    
    private Node put(Node node, Point2D key, Point2D val, boolean isHorizontal) {
        if (node == null) {
            size++;
            return new Node(key, val, null, null, isHorizontal);
        }
        
        if (node.key.x() == key.x() && node.key.y() == key.y()) {
            node.val = val;
        } else if ((!node.isHorizontal && node.key.x() >= key.x()) || (node.isHorizontal && node.key.y() >= key.y())) {
            node.left = put(node.left, key, val, !isHorizontal);
        } else {
            node.right = put(node.right, key, val, !isHorizontal);
        }
        
        return node;
    }
    
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        Node node = root;
        while (node != null) {
            if (node.key.x() == p.x() && node.key.y() == p.y()) {
                return true;
            } else if ((!node.isHorizontal && node.key.x() >= p.x()) || (node.isHorizontal && node.key.y() >= p.y())) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }
    
    // draw all points to standard draw
    public void draw() {
        StdDraw.setScale(0, 1);
        draw(root, board);
    }
    
    private void draw(Node node, RectHV rect) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            new Point2D(node.key.x(), node.key.y()).draw();
            StdDraw.setPenRadius();
            
            if (!node.isHorizontal) {
                StdDraw.setPenColor(StdDraw.RED);
                new Point2D(node.key.x(), rect.ymin()).drawTo(new Point2D(node.key.x(), rect.ymax()));
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                new Point2D(rect.xmin(), node.key.y()).drawTo(new Point2D(rect.xmax(), node.key.y()));
            }
            draw(node.left, leftRect(rect, node));
            draw(node.right, rightRect(rect, node));
        }
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> points = new Stack<Point2D>();
        get(root, board, rect, points);
        return points;
    }
    
    private RectHV leftRect(RectHV rect, Node p) {
        if (p.isHorizontal) {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.key.y());
        }
        return new RectHV(rect.xmin(), rect.ymin(), p.key.x(), rect.ymax());
    }
    
    private RectHV rightRect(RectHV rect, Node p) {
        if (p.isHorizontal) {
            return new RectHV(rect.xmin(), p.key.y(), rect.xmax(), rect.ymax());
        }
        return new RectHV(p.key.x(), rect.ymin(), rect.xmax(), rect.ymax());
    }    
    
    private void get(Node node, RectHV nodeRect, RectHV rect, Stack<Point2D> pointsIn) {
        if (node != null) {
            if (nodeRect.intersects(rect)) {
                if (rect.contains(node.val)) pointsIn.push(node.val);
                get(node.left, leftRect(nodeRect, node), rect, pointsIn);
                get(node.right, rightRect(nodeRect, node), rect, pointsIn);
            }   
        }
    }
    
    private Point2D nearestNeigh(Node node, RectHV rect, Point2D point, Point2D nearPoint) {
        Point2D nearestPoint = nearPoint;
        if (node != null) {      
            if (nearestPoint == null || nearestPoint.distanceSquaredTo(point) > rect.distanceSquaredTo(point)) {
                if (nearestPoint == null) {
                    nearestPoint = node.key;
                } else {
                    if (node.key.distanceSquaredTo(point) < nearestPoint.distanceSquaredTo(point)) {
                        nearestPoint = node.key;
                    }
                }
                
                if (!node.isHorizontal) {
                    RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), node.key.x(), rect.ymax());
                    RectHV rightRect = new RectHV(node.key.x(), rect.ymin(), rect.xmax(), rect.ymax());
                    if (point.x() <= node.key.x()) {
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                    } else {
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                    }
                } else {
                    RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.key.y());
                    RectHV rightRect = new RectHV(rect.xmin(), node.key.y(), rect.xmax(), rect.ymax());
                    if (point.y() <= node.key.y()) {
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                    } else {
                        nearestPoint = nearestNeigh(node.right, rightRect, point, nearestPoint);
                        nearestPoint = nearestNeigh(node.left, leftRect, point, nearestPoint);
                    }
                }
            }
        }
        return nearestPoint;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return nearestNeigh(root, board, p, null);
    }
    
    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
                     
}