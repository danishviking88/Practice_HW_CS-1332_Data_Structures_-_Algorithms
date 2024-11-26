import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Yue Gu
 * @userid ygu65
 * @GTID 903055355
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public AVL() {
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data collection is null, unable to comply");
        } else {
            for (T t : data) {
                if (t == null) {
                    throw new java.lang.IllegalArgumentException("Selected element in data is null, unable to comply.");
                } else {
                    add(t);
                }
            }
        }
    }

    /**
     * Add the data as a leaf to the AVL. Should traverse the tree to find the
     * appropriate location. If the data is already in the tree, then nothing
     * should be done (the duplicate shouldn't get added, and size should not be
     * incremented).
     *
     * Remember to recalculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    @Override 
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        } 

        // Add the data if it is not in the tree.
        if (!contains(data)) {
            addHelper(root, data);
        } 
    }

    // This adds the node to the tree, but does not do any other BR or height checks.  
    private AVLNode<T> addHelper(AVLNode<T> currentNode, T data) {
        if (currentNode == null && size > 0) {
            return null;
        }

        AVLNode<T> newNode = new AVLNode<T>(data);
        
        if (size == 0) {
            root = newNode;
        // If the data falls to the left, try to recursively search left or add the node if it is null.
        } else if (currentNode.getData().compareTo(data) > 0) {
            if (currentNode.getLeft() == null) {
                currentNode.setLeft(newNode);
            } else {
                return addHelper(currentNode.getLeft(), data);
            }
        // If the data falls to the right, try to recursively search right or add the node if it is null.
        } else if (currentNode.getData().compareTo(data) < 0) {
            if (currentNode.getRight() == null) {
                currentNode.setRight(newNode);
            } else {
                return addHelper(currentNode.getRight(), data);
            }
        } else {
            return null;
        }

        size++;
        return newNode;
    }

    private void updateHeightsAndBFs(AVLNode<T> currentNode, T endPointData) {
        if (!contains(endPointData)) {
            return;
        } 
        
        AVLNode<T> parentNode = currentNode;
        AVLNode<T> childNode;
        AVLNode<T> grandchildNode;

// *** HERE _______________________________________________________________
        if (currentNode.getBalanceFactor() > 1) {

            childNode = currentNode.getLeft();

            // Left-Left, need to rotate right.
            if (childNode.getBalanceFactor() > 1) {
                grandchildNode = childNode.getLeft();
            // Left-Right, need to rotate left then right.
            } else {
                grandchildNode = childNode.getRight();
            }

        } else if (currentNode.getBalanceFactor() < -1) {

            childNode = currentNode.getRight();

            // Right-Left, need to rotate right then left.
            if (childNode.getBalanceFactor() > 1 ) {
                grandchildNode = childNode.getLeft();
            // Right-Right, need to rotate left.
            } else {
                grandchildNode = childNode.getRight();
            }
        }
    }

    // Creates a dummy node and sets the height to -1. This is used to help re-calculate heights.
    private AVLNode<T> createDummyNode() {
        AVLNode<T> dummyNode = new AVLNode<T>(null);
        dummyNode.setHeight(-1);
        return dummyNode;
    }

    private void rotateRight() {

    }

    /* private void performRotations(AVLNode<T> topNode) {

        AVLNode<T> middleNode;
        AVLNode<T> bottomNode;

        AVLNode<T> newTopNodeChild = new AVLNode<T>(null);

        // Left-Left rotate setup
        if (topNode.getLeft() != null && topNode.getLeft().getLeft() != null) {
            middleNode = topNode.getLeft();
            bottomNode = middleNode.getLeft();
            topNode.setRight(newTopNodeChild);
        // Right-Right rotate setup
        } else if (topNode.getRight() != null && topNode.getRight().getRight() != null) {
            middleNode = topNode.getRight();
            bottomNode = middleNode.getRight();
            topNode.setLeft(newTopNodeChild);
        // Left-Right rotate setup
        } else if (topNode.getLeft() != null && topNode.getRight() != null) {
            middleNode = topNode.getLeft();
            bottomNode = middleNode.getRight();

            AVLNode<T> tmpNode = new AVLNode<T>(null);
            bottomNode.setRight(tmpNode);
            
            performRotations(middleNode);
            performRotations(topNode);
            return;            
        // Right-Left rotate setup
        } else {
            middleNode = topNode.getRight();
            bottomNode = middleNode.getLeft();

            AVLNode<T> tmpNode = new AVLNode<T>(null);
            bottomNode.setLeft(tmpNode);

            performRotations(middleNode);
            performRotations(topNode);
            return;            
        }

        copyNodeToLocation(topNode, newTopNodeChild);
        copyNodeToLocation(middleNode, topNode);
        copyNodeToLocation(bottomNode, middleNode);

        middleNode.setLeft(null);
        middleNode.setRight(null);

        // upon returning, I will need to update the heights. 
        // If you think about it, topNode is the only one that has a height needing changing, so it should be the focal point if height updating. 
    }

    */

    private void copyNodeToLocation (AVLNode<T> nodeToCopy, AVLNode<T> nodeAtLocation) {
        nodeAtLocation.setData(nodeToCopy.getData());
        nodeAtLocation.setHeight(nodeToCopy.getHeight());
        nodeAtLocation.setBalanceFactor(nodeToCopy.getBalanceFactor());
    }
    
    /**
     * Removes the data from the tree.  There are 3 cases to consider:
     * 1: the data is a leaf.  In this case, simply remove it.
     * 2: the data has one child.  In this case, simply replace the node with
     * the child node.
     * 3: the data has 2 children.  There are generally two approaches:
     * replacing the data with either the largest element in the left subtree
     * (commonly called the predecessor), or replacing it with the smallest
     * element in the right subtree (commonly called the successor). For this
     * assignment, use the predecessor.
     *
     * Remember to recalculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not in the tree
     * @param data data to remove from the tree
     * @return the data removed from the tree.  Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     */
    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        } else if (!contains(data)) {
            throw new NoSuchElementException("Data is not found in the tree, unable to comply");
        }

        return data;
    }

    /**
     * Returns the data in the tree matching the parameter passed in.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data data to get in the AVL tree
     * @return the data in the tree equal to the parameter.  Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        } else if (!contains(data)) {
            throw new NoSuchElementException("Data is not found in the tree, unable to comply.");
        }

        AVLNode<T> dataNode = searchForNode(root, data);
        return dataNode.getData();
    }

    /**
     * Returns whether or not the parameter is contained within the tree.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @param data data to find in the AVL tree
     * @return whether or not the parameter is contained within the tree
     */
    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        }

        AVLNode<T> node = searchForNode(root, data);

        if (node != null) {
            return true;
        }

        return false;
    
    }

    private AVLNode<T> searchForNode(AVLNode<T> currentNode, T data) {
        if (currentNode == null) {
            return null;
        }

        // If the data falls to the left, try to recursively search left.
        if (currentNode.getData().compareTo(data) > 0) {
            if (currentNode.getLeft() != null) {
                return searchForNode(currentNode.getLeft(), data);
            } else {
                return null;
            }
        // If the data falls to the right, try to recursively search right.
        } else if (currentNode.getData().compareTo(data) < 0) {
            if (currentNode.getRight() != null) {
                return searchForNode(currentNode.getRight(), data);
            } else {
                return null;
            }
        // Execute else statement if data is same, meaning you have successfully found the correct node.        
        } else {
            return currentNode;
        }
    }

    /**
     * Get the number of elements in the tree.
     *
     * @return the number of elements in the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Get the preorder traversal of the tree.
     *
     * @return a preorder traversal of the tree, or an empty list
     */
    @Override
    public List<T> preorder() {
        ArrayList<T> traversalList = new ArrayList<T>();
        
        preorderTraversalHelper(root, traversalList);

        return traversalList;
    }

    /**
     * Recursive function to help traverse the preorder method.
     *
     * @return void
     */
    private void preorderTraversalHelper(AVLNode<T> node, ArrayList<T> traversalList) {
        if (node != null) {
            traversalList.add(node.getData());
            preorderTraversalHelper(node.getLeft(), traversalList);
            preorderTraversalHelper(node.getRight(), traversalList);  
        }
    }

    /**
     * Get the postorder traversal of the tree.
     *
     * @return a postorder traversal of the tree, or an empty list
     */
    @Override
    public List<T> postorder() {
        ArrayList<T> traversalList = new ArrayList<T>();
        postorderTraversalHelper(root, traversalList);

        return traversalList;
    }

    /**
     * Recursive function to help traverse the postorder method.
     *
     * @return void
     */
    private void postorderTraversalHelper(AVLNode<T> node, ArrayList<T> traversalList) {
        if (node != null) {
            postorderTraversalHelper(node.getLeft(), traversalList);
            postorderTraversalHelper(node.getRight(), traversalList);
            traversalList.add(node.getData());
        }
    }

    /**
     * Get the inorder traversal of the tree.
     *
     * @return an inorder traversal of the tree, or an empty list
     */
    @Override
    public List<T> inorder() {
        ArrayList<T> traversalList = new ArrayList<T>();
        
        inorderTraversalHelper(root, traversalList);

        return traversalList;
    }

    /**
     * Recursive function to help traverse the inorder method.
     *
     * @return void
     */
    private void inorderTraversalHelper(AVLNode<T> node, ArrayList<T> traversalList) {
        if (node != null) {
            inorderTraversalHelper(node.getLeft(), traversalList);
            traversalList.add(node.getData());
            inorderTraversalHelper(node.getRight(), traversalList);
        }
    }

    /**
     * Get the level order traversal of the tree.
     *
     * @return a level order traversal of the tree, or an empty list
     */
    @Override
    public List<T> levelorder() {
        // Traversal list is the actual list that is every element in the tree.
        ArrayList<T> traversalList = new ArrayList<T>();
        // Traversal queue is a helper to navigate the tree.
        Queue<AVLNode<T>> traversalQueue = new LinkedList<AVLNode<T>>();
        
        if (root == null) {
            return traversalList;
        }

        AVLNode<T> cursor = root;
        do {
            traversalList.add(cursor.getData());

            if (cursor.getLeft() != null) {
                traversalQueue.add(cursor.getLeft());
            }

            if (cursor.getRight() != null) {
                traversalQueue.add(cursor.getRight());
            }

            cursor = traversalQueue.poll();

        } while (cursor != null);

        return traversalList;
    }

    /**
     * Creates a list of all leaf nodes present in the tree in
     * descending order.
     *
     * Should run in O(n).
     *
     * @return a list of all leaf nodes in descending order
     */
    @Override
    public List<T> listLeavesDescending() {

        ArrayList<T> descendingList = new ArrayList<T>();

        descendingListHelper(root, descendingList);

        return descendingList;
    }

    private void descendingListHelper(AVLNode<T> currentNode, ArrayList<T> descendingList) {
        if (currentNode.getLeft() == null && currentNode.getRight() == null) {
            descendingList.add(0, currentNode.getData());
        } else {
            descendingListHelper(currentNode.getLeft(), descendingList);
            descendingListHelper(currentNode.getRight(), descendingList);
        }
    }

    /**
     * Clear the tree.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Return the height of the root of the tree.
     * 
     * This method does not need to traverse the entire tree.
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    @Override
    public int height() {
        return root.getHeight();
    }
    
    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     * DO NOT USE IT IN YOUR CODE
     * DO NOT CHANGE THIS METHOD
     *
     * @return the root of the tree
     */
    @Override
    public AVLNode<T> getRoot() {
        return root;
    }
}



//   https://github.com/ericyuegu/CS-1332-Data-Structures-and-Algorithms/tree/master

