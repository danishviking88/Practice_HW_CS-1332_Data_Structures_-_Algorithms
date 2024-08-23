
import java.util.NoSuchElementException;

public class LinkedQueue<T> implements QueueInterface<T> {

    // Do not add new instance variables.
    private LinkedNode<T> head;
    private LinkedNode<T> tail;
    private int size;

    
    public void LinkedQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }


    /**
     * Dequeue from the front of the queue.
     *
     * This method should be implemented in O(1) time.
     *
     * @return the data from the front of the queue
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    @Override
    public T dequeue() {

        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty, unable to comply.");
        }

        T data = head.getData();
        head = head.getNext();
        size--;
        return data;
    }

    /**
     * Add the given data to the queue.
     *
     * This method should be implemented in (if array-backed, amortized) O(1)
     * time.
     *
     * @param data the data to add
     * @throws IllegalArgumentException if data is null
     */
    @Override
    public void enqueue(T data) {

        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        }

        LinkedNode<T> newNode = new LinkedNode<T>(data);

        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
        size++;
    }


    @Override
    public boolean isEmpty() {
        // DO NOT MODIFY THIS METHOD!
        return size == 0;
    }

    @Override
    public int size() {
         // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
      * Returns the head of this queue.
      * Normally, you would not do this, but we need it for grading your work.
      *
      * DO NOT USE THIS METHOD IN YOUR CODE.
      *
      * @return the head node
      */
    public LinkedNode<T> getHead() {
         // DO NOT MODIFY THIS METHOD!
        return head;
    }

    /**
      * Returns the tail of this queue.
      * Normally, you would not do this, but we need it for grading your work.
      *
      * DO NOT USE THIS METHOD IN YOUR CODE.
      *
      * @return the tail node
      */
    public LinkedNode<T> getTail() {
         // DO NOT MODIFY THIS METHOD!
        return tail;
    }
}
