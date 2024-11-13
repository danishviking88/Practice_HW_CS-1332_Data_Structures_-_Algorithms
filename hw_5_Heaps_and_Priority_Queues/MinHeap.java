
public class MinHeap<T extends Comparable<? super T>> implements HeapInterface<T> {

    private T[] backingArray;
    private int size;
    // Do not add any more instance variables. Do not change the declaration
    // of the instance variables above.

    /**
     * Creates a Heap with an initial capacity of {@code INITIAL_CAPACITY}
     * for the backing array.
     *
     * Use the constant field in the interface. Do not use magic numbers!
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Adds an item to the heap. If the backing array is full and you're trying
     * to add a new item, then double its capacity. The data passed in will not
     * be in the heap. Therefore, there will be no duplicates.
     *
     * @throws IllegalArgumentException if the item is null
     * @param item the item to be added to the heap
     */
    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null, unable to comply.");
        }

        // Check if resize of array is needed and perform resize if necessary.
        if (size == backingArray.length - 1) {
            resizeArray();
        }

        int index = size + 1;
        backingArray[index] = item;
        size++;

        reorderMovingUpwards(index);
    }

    /**
     * Removes and returns the first item of the heap. Null out all elements not
     * existing in the heap after this operation. Do not decrease the capacity
     * of the backing array.
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     * @return the item removed
     */
    @Override
    public T remove() {

        if (size <= 0) {
            throw new java.util.NoSuchElementException("Heap is empty, unable to comply.");
        }

        // Preserve the data for removed item.
        T removedItem = backingArray[1];
        // Swap the values at index 1 and the last index.
        swapValues(1, size);
        // Set the (now swapped) original value to null.
        backingArray[size] = null;
        size--;

        // Reorder moving downwards, starting at index 1 (the top of the heap).
        reorderMovingDownwards(1);
        return removedItem;
    }

    /**
     * Returns if the heap is empty or not.
     * @return a boolean representing if the heap is empty
     */
    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * Returns the size of the heap.
     * @return the size of the heap
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Clears the heap and returns array to starting capacity.
     */
    @Override
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Used for grading purposes only.
     *
     * DO NOT USE OR EDIT THIS METHOD!
     *
     * @return the backing array
     */
    @Override
    public Comparable[] getBackingArray() {
        return backingArray;
    }

    private void resizeArray() {
        T[] newBackingArray = (T[]) new Comparable[backingArray.length * 2];

        for (int i = 0; i < backingArray.length; i++) {
            newBackingArray[i] = backingArray[i];
        }

        backingArray = newBackingArray;
    }

    private void reorderMovingUpwards(int index) {

        if (index <= 1) {
            return;
        }

        int parentIndex = index / 2;
        T parentValue = backingArray[parentIndex];
        T indexValue = backingArray[index];

        // Compare index value to parent value and swap if necessary.
        if (indexValue.compareTo(parentValue) < 0) {

            swapValues(index, parentIndex);

            // Recursively check again
            reorderMovingUpwards(parentIndex);
        }
    }

    private void reorderMovingDownwards(int index) {
        // Assign these values for human readability.
        int leftChildIndex = 2 * index;
        int rightChildIndex = (2 * index) + 1;
        T leftValue;
        T rightValue;
        T indexValue = backingArray[index];

        // Check if the left and right children are valid indexes.
        if (leftChildIndex <= size && rightChildIndex <= size) {
            
            leftValue = backingArray[leftChildIndex];
            rightValue = backingArray[rightChildIndex];

            // If the indexValue is smaller than at least one of the children execute code, otherwise just exit.
            if (indexValue.compareTo(leftValue) > 0 || indexValue.compareTo(rightValue) > 0) {

                // If left child value is less than or equal to right, then swap and repeat.
                if (leftValue.compareTo(rightValue) < 0) {
                    swapValues(index, leftChildIndex);
                    reorderMovingDownwards(leftChildIndex);
                // Same as above, but on the right side.
                } else if (leftValue.compareTo(rightValue) > 0) {
                    swapValues(index, rightChildIndex);
                    reorderMovingDownwards(rightChildIndex);
                }
            }
        // Check if only the left child is a valid index.
        } else if (leftChildIndex <= size) {

            leftValue = backingArray[leftChildIndex];

            // If index value is less than the left value, swap.
            if (indexValue.compareTo(leftValue) > 0) {
                swapValues(index, leftChildIndex);
            }
        }
    }

    // Swaps values in the backingArray, using the typically unused 0 index as the tmp buffer (Why not? It's free real estate!)
    private void swapValues(int index1, int index2) {
        backingArray[0] = backingArray[index1];
        backingArray[index1] = backingArray[index2];
        backingArray[index2] = backingArray[0];
        backingArray[0] = null;
    }
}
