/*
 * The MIT License
 *
 * Copyright 2017 Matúš Námešný.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.namesny.binarysearchtree;

/**
 * Implementation of AVL tree a self-balancing binary search tree.
 * http://en.wikipedia.org/wiki/AVL_tree
 *
 * @author Matúš Námešný
 * @param <T> T can be any type that extends Comparable
 */
public class AVLTree<T extends Comparable<? super T>> implements BinarySearchTree<T> {

    /**
     * Tree root
     */
    protected AVLNode<T> root;

    public AVLTree() {
        root = null;
    }

    /**
     * This class represents a node of an AVL tree
     *
     * @param <T>
     */
    protected static class AVLNode<T extends Comparable<? super T>> {

        /**
         * Node value
         */
        protected T value;

        /**
         * Height of the node
         */
        protected int height;

        /**
         * Left child
         */
        protected AVLNode<T> left;

        /**
         * Right child
         */
        protected AVLNode<T> right;

        /**
         * Creates one node
         *
         * @param value the value of this node
         * @param left left child node
         * @param right right child node
         */
        public AVLNode(T value, AVLNode<T> left, AVLNode<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        /**
         * Creates one node with no children
         *
         * @param value the value of this node
         */
        public AVLNode(T value) {
            this(value, null, null);
        }
    }

    /**
     * Inserts the value to the tree and re-balances it if necessary
     *
     * @param value the value to insert
     * @throws DuplicateValueException
     */
    @Override
    public void insert(T value) throws DuplicateValueException {
        this.root = insert(value, root);
    }

    /**
     * Deletes the value from the tree
     *
     * @param value the value to be deleted
     */
    @Override
    public void delete(T value) {
        this.root = delete(value, root);
    }

    /**
     * Finds object equal to key in the tree
     * 
     * @param key
     * @return returns 
     */
    @Override
    public T find(T key) {
        return find(key, root);
    }

    /**
     * Clears the tree
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * True if the tree is empty
     * 
     * @return returns true if the tree is empty
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Finds minimal value in the tree
     * 
     * @return minimum 
     */
    @Override
    public T findMin() {
        return findMin(root).value;
    }
    
    /**
     * Finds maximal value in the tree
     * 
     * @return maximum 
     */
    @Override
    public T findMax() {
        return findMax(root).value;
    }

    /**
     * Finds minimum in the given subtree
     * 
     * @param node root of the subtree
     * @return object with minimal value 
     */
    private AVLNode<T> findMin(AVLNode<T> node) {
        // The leftmost node is the node with minimal value
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }
    
    /**
     * Finds maximum in the given subtree
     * 
     * @param node root of the subtree
     * @return object with maximal value
     */
    private AVLNode<T> findMax(AVLNode<T> node) {
        // The rightmost node is the node with maximal value
        while (node.right != null) {
            node = node.right;
        }
        
        return node;
    }

    /**
     * Helper method for inserting values into the tree
     *
     * @param value the value to insert
     * @param node the root of a subtree where the value will be inserted
     * @return the new tree with the value inserted
     * @throws DuplicateValueException
     */
    private AVLNode<T> insert(T value, AVLNode<T> node) throws DuplicateValueException {

        // We found the place where to insert the value
        if (node == null) {
            // Create new node with the value
            node = new AVLNode<>(value);

        } else if (value.compareTo(node.value) < 0) {
            // Insert into left subtree
            node.left = insert(value, node.left);

        } else if (value.compareTo(node.value) > 0) {
            // Insert into right subtree 
            node.right = insert(value, node.right);

        } else {
            // The tree already contains the value
            throw new DuplicateValueException("Duplicate value: " + value);
        }

        node = rebalance(node);
        return node;
    }

    /**
     * Helper method for deleting values from th tree
     *
     * @param value the value to delete
     * @param node the root of a subtree from which the value will be deleted
     * @return
     */
    private AVLNode<T> delete(T value, AVLNode<T> node) {

        /* If we didn't find the node that we wanted to delete
         * we actually succeeded as the node is already "deleted"
         */
        if (node == null) {
            return null;
        }

        // We found the node that we want to delete
        if (node.value == value) {
            if ((node.left == null) && (node.right == null)) {
                node = null;

            } else if (node.left == null) {
                node = node.right;

            } else if (node.right == null) {
                node = node.left;

            } else {
                AVLNode<T> successor = findMin(node.right);

                node.value = successor.value;
                node = delete(successor.value, node);
            }

            // we didn't find the node to delete yet
        } else if (value.compareTo(node.value) < 0) {
            // delete it from the left subtree
            node.left = delete(value, node.left);
            node = rebalance(node);
        } else {
            // delete it from the right subtree
            node.right = delete(value, node.right);
            node = rebalance(node);
        }

        return node;
    }

    /**
     * Helper method for finding values
     *
     * @param key the key to find
     * @param node the root of a subtree where we are searching
     * @return returns the value of node which equals to the key
     */
    private T find(T key, AVLNode<T> node) {

        if (node == null) {
            return null;
        }

        if (node.value == key) {
            return node.value;
        } else if (key.compareTo(node.value) < 0) {
            return (T) find(key, node.left);
        } else {
            return (T) find(key, node.right);
        }
    }

    /**
     * Updates the height of a node
     *
     * @param node node which height we need updating
     */
    private void updateHeight(AVLNode<T> node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    /**
     * Returns the height of a node
     *
     * @param node node which height we need
     * @return the height of a node
     */
    private int getHeight(AVLNode<T> node) {
        return node == null ? -1 : node.height;
    }

    /**
     * Checks the tree for balance and re-balances it if necessary
     *
     * @param root the root of a subtree to re-balance
     */
    private AVLNode<T> rebalance(AVLNode<T> node) {
        updateHeight(node);
        int balance = getHeight(node.right) - getHeight(node.left);

        if (balance == -2) {
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                node = rotateRight(node);
            } else {
                node = rotateLeftRight(node);
            }
        } else if (balance == 2) {
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                node = rotateLeft(node);
            } else {
                node = rotateRightLeft(node);
            }
        }

        return node;
    }

    /**
     * Rotates tree to the left
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private AVLNode<T> rotateLeft(AVLNode<T> node) {
        AVLNode<T> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Rotates tree to the right
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private AVLNode<T> rotateRight(AVLNode<T> node) {
        AVLNode<T> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;

        return newRoot;
    }

    /**
     * Rotates tree first left the right
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private AVLNode<T> rotateLeftRight(AVLNode<T> node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    /**
     * Rotates tree first right then left
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private AVLNode<T> rotateRightLeft(AVLNode<T> node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

}
