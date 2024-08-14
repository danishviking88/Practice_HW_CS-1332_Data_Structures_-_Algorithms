import org.junit.runner.OrderWith;

public class ArrayList<T> implements ArrayListInterface<T> {
    
    public T backingArray[];
    public int arrayCapacity;
    public int numOfElements;

    public ArrayList() {
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        this.arrayCapacity = INITIAL_CAPACITY;
        this.numOfElements = 0;
        
    }

    @Override
    public void addAtIndex(int index, T data) {
        // Exception throws if index is out of bounds or data is null.
        if (index < 0 || index > numOfElements) {
            throw new IndexOutOfBoundsException("Index is out of bounds, unable to comply.");  
        } else if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        }

        // If the size of the array is at capacity, make a new array twice as large and copy the data over.
        if (numOfElements == arrayCapacity){
            T tmpArray[] = (T[]) new Object[this.arrayCapacity*2];
            for (int i = 0; i < numOfElements; i++) {
                tmpArray[i] = backingArray[i];
            }
            backingArray = tmpArray;
        }

        for (int i = numOfElements; i > index; i--) {
                backingArray[i] = backingArray[i-1];
        }

        backingArray[index] = data;
        numOfElements++;
    }

    @Override
    public void addToFront(T data) {
        addAtIndex(0, data);
    }

    @Override 
    public void addToBack(T data)
    {
        addAtIndex(numOfElements, data);
    }

    @Override
    public T removeAtIndex(int index) {
        // Exception throws if index is out of bounds. 
        if (index < 0 || index >= numOfElements) {
            throw new IndexOutOfBoundsException("Index is negative, unable to comply.");
        } 
        // Removes data at index.
        T indexData = backingArray[index];
        for (int i = index; i < numOfElements-1; i++) {
            backingArray[i] = backingArray[i+1];
        }
        backingArray[numOfElements-1] = null;
        numOfElements--;
        return indexData;
    }

    @Override
    public T removeFromFront() {
        return removeAtIndex(0);
    }

    @Override
    public T removeFromBack() {
        return removeAtIndex(numOfElements-1);
    }

    @Override
    public T get(int index) {
        return backingArray[index];
    }

    @Override
    public void clear() {
        for (T e : backingArray) {
            e = null;
        }
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
        arrayCapacity = INITIAL_CAPACITY;
        numOfElements = 0;
    }

    @Override
    public boolean isEmpty() {
        if (numOfElements > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int size() {
        return numOfElements;
    }

    @Override
    public Object[] getBackingArray() {
        return backingArray;
    }
}

