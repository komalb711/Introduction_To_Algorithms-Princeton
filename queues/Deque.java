import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    
    private int n;          // size of the stack
    private Node headNode;     // head of stack
    
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }
    
    public Deque()                           // construct an empty deque
    {
        headNode = null;
        n = 0;
    }
    public boolean isEmpty()                 // is the deque empty?
    {
        return (n == 0 || headNode == null);
    }
    public int size()                        // return the number of items on the deque
    {
        return n;
    }
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new NullPointerException("Item cannnot be null");
        
        Node newHead = new Node();
        newHead.item = item;
        
        if (headNode == null)
        {
            newHead.next = newHead;
            newHead.prev = newHead;
            
        }
        else {
            newHead.next = headNode;
            Node tail = headNode.prev;
            newHead.prev = tail;
            tail.next = newHead;
            headNode.prev = newHead;
        }
        n++;
        
        headNode = newHead;
    }
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new NullPointerException("Item cannnot be null");
        
        Node newTail = new Node();
        newTail.item = item;
        
        if (headNode == null)
        {
            newTail.next = newTail;
            newTail.prev = newTail;
            headNode = newTail;
        }
        else {
            newTail.next = headNode;
            Node tail = headNode.prev;
            newTail.prev = tail;
            tail.next = newTail;
            headNode.prev = newTail;
        }
        n++;
    }
    public Item removeFirst()                // remove and return the item from the front
    {
        if (isEmpty()) 
            throw new NoSuchElementException("Deque underflow");
        
        Item item = headNode.item;
        Node newHead = headNode.next;
        Node tail = headNode.prev;
        
        if (newHead == headNode)
        {
            newHead = null;
        }
        else if (newHead == tail)
        {
            newHead.prev = newHead;
            newHead.next = newHead;
        }
        else
        {
            newHead.prev = tail;
            tail.next = newHead;
        }  
        headNode = newHead;
        n--;
        return item;
    }
    public Item removeLast()                 // remove and return the item from the end
    {
        if (isEmpty()) 
            throw new NoSuchElementException("Deque underflow");
        
        Node tail = headNode.prev;
        Node newTail = tail.prev;
        Item item = tail.item;
        
        if (tail == headNode)
        {
            headNode = null;
        }
        else if (newTail == headNode)
        {
            headNode.prev = headNode;
            headNode.next = headNode;
        }
        else
        {
            headNode.prev = newTail;
            newTail.next = headNode;
        }  
        n--;
        return item;
    }
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = headNode;
        private boolean headIterated;
        public boolean hasNext()  { 
            if (current == null)
                return false;
            return (!headIterated || current != headNode); 
        }
        public void remove()      { throw new UnsupportedOperationException();  }
        
        public Item next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            if (current == headNode)
                headIterated = true;
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    public static void main(String[] args)   // unit testing
    {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                deque.addFirst(item);
            else if (!deque.isEmpty())
                StdOut.print(deque.removeLast() + " ");
            else
                break;
            
            StdOut.print("Iterating the deque:");
            for (String x : deque) {
                StdOut.print(x + ' ');
            }
        }
    }
}
