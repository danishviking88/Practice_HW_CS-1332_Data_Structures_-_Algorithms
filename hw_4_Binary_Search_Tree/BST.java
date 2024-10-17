import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.prefs.BackingStoreException;

/**
 * Your implementation of a binary search tree.
 *
 * @author Yue Gu
 * @userid ygu65
 * @GTID 903055355
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> implements BSTInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty BST.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public BST() {
        this.root = null;
        size = 0;
    }

    /**
     * Initializes the BST with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        for (T t : data) {
            if (t == null) {
                throw new IllegalArgumentException("Data is null, unable to comply.");
            }
            add(t);
        }
    }


    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null, unable to comply.");
        }
        
        if (!contains(data)) {
            if (size > 0) {
                addHelper(root, data);
            } else {
                BSTNode<T> newNode = new BSTNode(data);
                root = newNode;
            }
            size++; 
        }
    }

    private void addHelper(BSTNode<T> currentNode, T data) {       
        // Execute this code if data is to the left of the current node.
        if (data.compareTo(currentNode.getData()) < 0) {
            if (currentNode.getLeft() != null) {
                addHelper(currentNode.getLeft(), data);
            } else {
                BSTNode<T> newNode = new BSTNode(data);
                currentNode.setLeft(newNode);
            }
        // Same, but if data is to the right of the current node.
        } else if (data.compareTo(currentNode.getData()) > 0) {
            if (currentNode.getRight() != null) {
                addHelper(currentNode.getRight(), data);
            } else {
                BSTNode<T> newNode = new BSTNode(data);
                currentNode.setRight(newNode);
            }
        }
    }

    private BSTNode<T> getParentNode(BSTNode<T> currentNode, BSTNode<T> childNode) {
        // If the current node is null, or the childNode is root (because then it would have no parent) return null.
        if (currentNode == null || childNode == root) {
            return null;
        }

        ArrayList<BSTNode<T>> nodeList = getNodeList(root);

        for (BSTNode<T> node : nodeList) {
            if (node.getLeft() != null && node.getLeft().getData().equals(childNode.getData())) {
                return node;
            } else if (node.getRight() != null && node.getRight().getData().equals(childNode.getData())) {
                return node;
            }
        }
        return null;
    }

    ArrayList<BSTNode<T>> getNodeList(BSTNode<T> root) {
        ArrayList<BSTNode<T>> nodeList = new ArrayList<BSTNode<T>>();
        nodeListHelper(root, nodeList);
        return nodeList;
    }

    void nodeListHelper(BSTNode<T> currentNode, ArrayList<BSTNode<T>> nodeList) {
        if (currentNode.getLeft() != null) {
            nodeList.add(currentNode);
            nodeListHelper(currentNode.getLeft(), nodeList);
        }

        if (currentNode.getRight() != null) {
            nodeList.add(currentNode);
            nodeListHelper(currentNode.getRight(), nodeList);
        }
    }

    private BSTNode<T> getNode(BSTNode<T> currentNode, T data) {   
        if (!contains(data)) {
            throw new NoSuchElementException("Data is not present in the Binary Search Tree, unable to comply.");
        }
        
        if (currentNode.getData().compareTo(data) < 0) {
            return getNode(currentNode.getRight(), data);
        } else if (currentNode.getData().compareTo(data) > 0) {
            return getNode(currentNode.getLeft(), data);
        } else {
            return currentNode;
        }
    }

        /**
         * Removes the data from the tree.  There are 3 cases to consider:
         *
         * 1: the data is a leaf.  In this case, simply remove it.
         * 2: the data has one child.  In this case, simply replace it with its
         * child.
         * 3: the data has 2 children.  There are generally two approaches:
         * replacing the data with either the largest element that is smaller than
         * the element being removed (commonly called the predecessor), or replacing
         * it with the smallest element that is larger than the element being
         * removed (commonly called the successor). For this assignment, use the
         * predecessor. You may use iteration to find/remove the predecessor AFTER
         * you have found the node to be removed recursively (but don't start back
         * at the root to do so). We recommend you find and remove the predecessor
         * recursively, but there is not penalty for doing so iteratively for
         * this assignment.
         *
         * Should have a running time of O(log n) for a balanced tree, and a worst
         * case of O(n).
         *
         * @throws IllegalArgumentException if the data is null
         * @throws java.util.NoSuchElementException if the data is not found
         * @param data the data to remove from the tree.
         * @return the data removed from the tree.  Do not return the same data
         * that was passed in.  Return the data that was stored in the tree.
         */
        @Override
        public T remove(T data) {
            if (data == null) {
                throw new IllegalArgumentException("Data is null, unable to comply.");
            } else if (!contains(data)) {
                throw new NoSuchElementException("Data not found in the BST, unable to comply.");
            }
            BSTNode<T> nodeToRemove = getNode(root, data);
            T removedData = nodeToRemove.getData();
            // If the BST only has one node, remove it and return the data.
            if (size <= 1) {
                size = 0;
                root = null;
                return removedData;
            } 

            BSTNode<T> nodeToRemoveParent = getParentNode(root, nodeToRemove);
            BSTNode<T> predecessor = null;
            // If there are two children nodes execute this if statement.
            if (nodeToRemove.getLeft() != null && nodeToRemove.getRight() != null) {
                predecessor = findPredecessor(nodeToRemove.getLeft());
                BSTNode<T> predecessorParent = getParentNode(root, predecessor);
                // Move right child of node to right side of predecessor.
                // *** may not run if null???? need to test.
                predecessor.setRight(nodeToRemove.getRight());

                // If the predecessorParent equals the nodeToRemove we want to skip this.
                if (predecessorParent != nodeToRemove) {
                    predecessorParent.setRight(predecessor.getLeft());
                    predecessor.setLeft(nodeToRemove.getLeft());
                }
            // If there is just one child node execute this if statement.
            } else if (nodeToRemove.getLeft() != null) {
                predecessor = nodeToRemove.getLeft();
            } else if (nodeToRemove.getRight() != null) {
                predecessor = nodeToRemove.getRight();
            }
            // If the nodeToRemoveParent is null, then you know that the nodeToRemove is root. Set predecessor to root.
            if (nodeToRemoveParent == null) {
                root = predecessor;
            // Determine if the nodeToRemove is on the left or right side of its parent, and link predecessor there.
            } else if (nodeToRemoveParent.getLeft() != null && nodeToRemoveParent.getLeft().getData().equals(nodeToRemove.getData())) {
                nodeToRemoveParent.setLeft(predecessor);
            } else {
                nodeToRemoveParent.setRight(predecessor);
            }
            nodeToRemove = null;
            size--;
            return removedData;
        }
    
        private BSTNode<T> findPredecessor(BSTNode<T> currentNode) {
            if (currentNode.getRight() == null) {
                return currentNode;
            } else {
                return findPredecessor(currentNode.getRight());
            }
        }

        /**
         * Returns the data in the tree matching the parameter passed in (think
         * carefully: should you use .equals or == ?).
         * Should have a running time of O(log n) for a balanced tree, and a worst
         * case of O(n).
         *
         * @throws IllegalArgumentException if the data is null
         * @throws java.util.NoSuchElementException if the data is not found
         * @param data the data to search for in the tree.
         * @return the data in the tree equal to the parameter. Do not return the
         * same data that was passed in.  Return the data that was stored in the
         * tree.
         */
        @Override
        public T get(T data) {
            BSTNode<T> node = getNode(root, data);
            return node.getData();
        }

        /**
         * Returns whether or not data equivalent to the given parameter
         * is contained within the tree.
         * Should have a running time of O(log n) for a balanced tree, and a worst
         * case of O(n).
         *
         * @throws IllegalArgumentException if the data is null
         * @param data the data to search for in the tree.
         * @return whether or not the parameter is contained within the tree.
         */
        @Override
        public boolean contains(T data) {
            if (data == null) {
                throw new IllegalArgumentException("Data is null, unable to comply.");
            }
            if (size == 0) {
                return false;
            }
            List<T> traversalList = preorder();
            for (T t : traversalList) {
                if (t.equals(data)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Should run in O(1).
         *
         * @return the number of elements in the tree
         */
        @Override
        public int size() {
            return size;
        }

        /**
         * Should run in O(n).
         *
         * @return a preorder traversal of the tree
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
        private void preorderTraversalHelper(BSTNode<T> node, ArrayList<T> traversalList) {
            if (node != null) {
                traversalList.add(node.getData());
                preorderTraversalHelper(node.getLeft(), traversalList);
                preorderTraversalHelper(node.getRight(), traversalList);  
            }
        }

        /**
         * Should run in O(n).
         *
         * @return a postorder traversal of the tree
         */
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
        private void postorderTraversalHelper(BSTNode<T> node, ArrayList<T> traversalList) {
            if (node != null) {
                postorderTraversalHelper(node.getLeft(), traversalList);
                postorderTraversalHelper(node.getRight(), traversalList);
                traversalList.add(node.getData());
            }
        }

        /**
         * Should run in O(n).
         *
         * @return an inorder traversal of the tree
         */
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
        private void inorderTraversalHelper(BSTNode<T> node, ArrayList<T> traversalList) {
            if (node != null) {
                inorderTraversalHelper(node.getLeft(), traversalList);
                traversalList.add(node.getData());
                inorderTraversalHelper(node.getRight(), traversalList);
            }
        }

        /**
         * Generate a level-order traversal of the tree.
         *
         * To do this, add the root node to a queue. Then, while the queue isn't
         * empty, remove one node, add its data to the list being returned, and add
         * its left and right child nodes to the queue. If what you just removed is
         * {@code null}, ignore it and continue with the rest of the nodes.
         *
         * Should run in O(n).
         *
         * @return a level order traversal of the tree
         */
        @Override
        public List<T> levelorder() {
            // Traversal list is the actual list that is every element in the tree.
            ArrayList<T> traversalList = new ArrayList<T>();
            // Traversal queue is a helper to navigate the tree.
            Queue<BSTNode<T>> traversalQueue = new LinkedList<BSTNode<T>>();
            
            if (root == null) {
                return traversalList;
            }

            BSTNode<T> cursor = root;
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
        public List<T> listLeavesDescending() {

            ArrayList<T> descendingList = new ArrayList<T>();

            descendingListHelper(root, descendingList);

            return descendingList;
        }

        private void descendingListHelper(BSTNode<T> currentNode, ArrayList<T> descendingList) {
            if (currentNode.getLeft() == null && currentNode.getRight() == null) {
                descendingList.add(0, currentNode.getData());
            } else {
                descendingListHelper(currentNode.getLeft(), descendingList);
                descendingListHelper(currentNode.getRight(), descendingList);
            }
        }

        /**
         * Clear the tree.  Should be O(1).
         */
        @Override
        public void clear() {
            root = null;
            size = 0;
        }

        /**
         * Calculate and return the height of the root of the tree.  A node's
         * height is defined as {@code max(left.height, right.height) + 1}. A leaf
         * node has a height of 0 and a null child should be -1.
         * Should be calculated in O(n).
         *
         * @return the height of the root of the tree, -1 if the tree is empty
         */
        @Override
        public int height() {
            int currentHeight = 0;
            ArrayList<Integer> heightsList = new ArrayList<Integer>();
            heightHelper(root, currentHeight, heightsList);

            int maxHeight = 0;
            if (heightsList.isEmpty()) {
                return -1;
            } else {
                for (Integer integer : heightsList) {
                    if (maxHeight < integer) {
                        maxHeight = integer;
                    }
                }
                return maxHeight-1;
            }
        }

        private void heightHelper(BSTNode<T> node, int currentHeight, ArrayList<Integer> heightsList) {
            if (node != null) {
                currentHeight++;
                if (node.getLeft() == null && node.getRight() == null) {
                    heightsList.add(currentHeight);
                } else {
                    heightHelper(node.getLeft(), currentHeight, heightsList);
                    heightHelper(node.getRight(), currentHeight, heightsList);
                }
            }
        }

        /**
         * THIS METHOD IS ONLY FOR TESTING PURPOSES.
         * DO NOT USE IT IN YOUR CODE
         * DO NOT CHANGE THIS METHOD
         *
         * @return the root of the tree
         */
        @Override
        public BSTNode<T> getRoot() {
            // DO NOT MODIFY THIS METHOD!
            return root;
        }
}
