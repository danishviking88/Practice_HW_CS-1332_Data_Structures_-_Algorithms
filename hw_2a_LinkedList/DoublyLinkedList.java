import javax.xml.parsers.FactoryConfigurationError;



public class DoublyLinkedList<T> implements LinkedListInterface<T> {
    

    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;
    private int size = 0;
    

    @Override
    public void addAtIndex(int index, T data) {
        // Throw exception if index is out of bounds. 
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds, unable to comply.");
        } else if (data == null) {
            throw new IllegalArgumentException("Data is equal to null, unable to comply.");
        }

        // Adding a node to the front of the list.
        if (index == 0) {
            addToFront(data);
        // Adding a node to the back of the list.
        } else if (index == size) {
            addToBack(data);
        // Adding a node at a specific index.
        } else {
            LinkedListNode<T> nodeAtIndex = getNodeAtIndex(index);
            LinkedListNode<T> previousNode = nodeAtIndex.getPrevious();

            LinkedListNode<T> newNode = new LinkedListNode<T>(data, previousNode, nodeAtIndex);
            previousNode.setNext(newNode);
            nodeAtIndex.setPrevious(newNode);
            size++;
        }
    }

    @Override
    public void addToFront(T data) {
        // If data is null, throw an exception.
        if (data == null) {
            throw new IllegalArgumentException("Data is equal to null, unable to comply.");
        }
        // Add element to the front.
        if (size == 0) {
            addToEmptyList(data);
        } else {
            LinkedListNode<T> node = new LinkedListNode<T>(data, null, head);
            head.setPrevious(node);
            head = node;
            size++;
        }
    }

    @Override
    public void addToBack(T data) {
        // If data is null, throw an exception.
        if (data == null) {
            throw new IllegalArgumentException("Data is equal to null, unable to comply.");
        }
        // Add element to the back.
        if (size == 0) {
            addToEmptyList(data);
        } else {
            LinkedListNode<T> node = new LinkedListNode<T>(data, tail, null);
            tail.setNext(node);
            tail = node;
            size++;
        }
    }

    @Override
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds, unable to comply.");
        }

        T removedData;
        if (index == 0) {
            removedData = removeFromFront();
        } else if (index == size-1) {
            removedData = removeFromBack();
        } else {
            LinkedListNode<T> cursor = getNodeAtIndex(index);
            removedData = cursor.getData();
            // Here, I build a link between the previous node and the next node.
            // Since this is not the head or tail, the garbage collector will take the 
            // memory back since there are no longer any references to the cursor item.
            LinkedListNode<T> previousNode = cursor.getPrevious();
            LinkedListNode<T> nextNode = cursor.getNext();
            previousNode.setNext(nextNode);
            nextNode.setPrevious(previousNode);
        } 
        size--;
        return removedData;
    }

    @Override
    public T removeFromFront() {
        if (size == 0) {
            return null;
        }
        
        T removedData = head.getData();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrevious(null);
        }
            size--;
            return removedData;
    }

    @Override
    public T removeFromBack() {
        if (size == 0) {
            return null;
        }
        
        T removedData = tail.getData();
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrevious();
            tail.setNext(null);
        }
            size--;
            return removedData;
    }

    @Override
    public boolean removeFirstOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is equal to null, unable to comply.");
        }
        // There is nothing in the LinkedList, return false as nothing was removed.
        if (size == 0) {
            return false;
        } else {
            // Make a cursor and set it to head.
            LinkedListNode<T> cursor = head;
            // Cycle through the LinkedList and check each data point for a match. 
            int indexCount = 0;
            while (cursor != null) {
                if (cursor.getData().equals(data)) {
                    removeAtIndex(indexCount);
                    size--;
                    return true;
                }
                cursor = cursor.getNext();
                indexCount++;
            }
            // Nothing was removed.
            return false;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 | index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds, unable to comply.");
        }

        LinkedListNode<T> cursor = getNodeAtIndex(index);
        return cursor.getData();
    }

    @Override
    public Object[] toArray() {

        Object[] arr = new Object[size];
        LinkedListNode<T> cursor = head;

        int i = 0;
        while(cursor != null) {
            arr[i] = cursor.getData();
            cursor = cursor.getNext();
            i++;
        }
        return arr;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public LinkedListNode<T> getHead() {
        return head;
    }

    @Override
    public LinkedListNode<T> getTail() {
        return tail;
    }

    private LinkedListNode<T> getNodeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds, unable to comply.");
        } else if (isEmpty()) {
            return null;
        }

        LinkedListNode<T> cursor = head;
        for (int i = 0; i < index; i++) {
            cursor = cursor.getNext();
        }
        return cursor;
    }

    private void addToEmptyList(T data) {
        LinkedListNode<T> node = new LinkedListNode<T>(data);
        head = node;
        tail = node;
        size++;
    }
}



