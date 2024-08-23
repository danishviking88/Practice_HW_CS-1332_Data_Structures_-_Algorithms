
import java.util.NoSuchElementException;



public class ArrayStack<T> implements StackInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int size;

    public ArrayStack() {
        this.backingArray = (T[]) new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * Pop from the stack.
     *
     * Removes and returns the top-most element on the stack.
     * This method should be implemented in O(1) time.
     *
     * @return the data from the front of the stack
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty, unable to comply.");
        }

        T data = backingArray[size-1];
        backingArray[size-1] = null;
        size--;
        return data;
    }

    /**
     * Push the given data onto the stack.
     *
     * The given element becomes the top-most element of the stack.
     * This method should be implemented in (if array-backed, amortized) O(1)
     * time.
     *
     * @param data the data to add
     * @throws IllegalArgumentException if data is null
     */
    @Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        }

        if (size == backingArray.length) {
            resizeArray();
        }

        backingArray[size] = data;
        size++;
    }

    private void resizeArray() {
        T[] tmpArray = (T[]) new Object[backingArray.length * 2];
        for (int i = 0; i < size; i++) {
            tmpArray[i] = backingArray[i];
        }
        backingArray = tmpArray;
    }

    /**
     * Return true if this stack contains no elements, false otherwise.
     *
     * This method should be implemented in O(1) time.
     *
     * @return true if the stack is empty; false otherwise
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the size of the stack.
     *
     * This method should be implemented in O(1) time.
     *
     * @return number of items in the stack
     */
    @Override
    public int size() {
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
