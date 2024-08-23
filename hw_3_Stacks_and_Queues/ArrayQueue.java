
import java.util.NoSuchElementException;


//   -----------------------------
//   | back |      |      | front|
//   |   0  |   1  |   2  |   3  |  ----> indexes always move forward and wrap around to index 0 when necessary
//   -----------------------------

public class ArrayQueue<T> implements QueueInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int front;
    private int back;
    private int size;

    public ArrayQueue() {
        this.backingArray = (T[]) new Object[INITIAL_CAPACITY];
        this.front = 0;
        this.back = 0;
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

        T data = backingArray[back];
        backingArray[back] = null;

        // If the array is not empty, then we need to update where the back is. If its empty, front and back should match. 
        if (!isEmpty()) {
            back = getNextIndexInCircularArray(back);
        } 
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

        // If the next index is the same as back, then we know that the array is full and needs resizing. Then re-calc nextIndex.
        if (size >= this.backingArray.length) {
            resizeArray();
        }

        int nextIndex = getNextIndexInCircularArray(front);
        backingArray[nextIndex] = data;
        front = nextIndex;
        size++;

        // If the size of the array is one, set the back to match the front.
        if (size == 1) {
            back = front;
        }
    }
    
    private void resizeArray() {
        // this needs to double the arraysize, then update front and back.
        T[] tmpArray = (T[]) new Object[backingArray.length * 2];

        int cursor = back;
        for (int i = 0; i < backingArray.length; i++) {
            tmpArray[i] = backingArray[cursor];
            cursor = getNextIndexInCircularArray(cursor);
        }
        backingArray = tmpArray;
        back = 0;
        front = size-1;
    }

    private int getNextIndexInCircularArray(int index) {
        if (size == 0) {
            return front;
        } else if (index == backingArray.length-1) {
            return 0;
        } else {
            return index+1;
        }
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
     * Returns the backing array of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the backing array
     */
    public Object[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }
}
