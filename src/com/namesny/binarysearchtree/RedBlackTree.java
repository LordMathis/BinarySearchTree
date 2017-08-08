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
 * Implementation of Red-Black tree, a self-balancing binary search tree
 * http://en.wikipedia.org/wiki/Red%E2%80%93black_tree
 *
 * @author Matúš Námešný
 */
public class RedBlackTree<T extends Comparable<? super T>> implements BinarySearchTree<T> {

    /**
     * Tree root
     */
    protected RedBlackNode<T> root;

    public RedBlackTree() {
        root = null;
    }

    /**
     * This class represents a node of a Red-Black tree
     *
     * @param <T>
     */
    protected static class RedBlackNode<T extends Comparable<? super T>> {

        /**
         * Node value
         */
        protected T value;

        /**
         * Left child
         */
        protected RedBlackNode<T> left;

        /**
         * Right child
         */
        protected RedBlackNode<T> right;

        /**
         * Node color
         */
        protected Color color;

        /**
         * Creates one node
         *
         * @param value node value
         * @param left left child
         * @param right right child
         */
        public RedBlackNode(T value, RedBlackNode<T> left, RedBlackNode<T> right, Color color) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.color = color;
        }

        /**
         * Creates one node without children
         *
         * @param value
         */
        public RedBlackNode(T value) {
            this(value, null, null, Color.RED);
        }
    }

    /**
     * Represents the color of a node
     */
    protected static enum Color {
        RED, BLACK
    }

    @Override
    public void insert(T value) throws DuplicateValueException {
        root = insert(value, root);
    }

    @Override
    public void delete(T key) {
        root = delete(key, root);
    }

    @Override
    public T find(T key) {
        return find(key, root);
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public T findMin() {
        return findMin(root).value;
    }

    @Override
    public T findMax() {
        return findMax(root).value;
    }

    private RedBlackNode<T> insert(T value, RedBlackNode<T> node) throws DuplicateValueException {

        // We found the place where to insert the value
        if (node == null) {
            // Create new node with the value
            node = new RedBlackNode<>(value);

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

        node = rebalanceInsert(node);
        return node;
    }

    private RedBlackNode<T> delete(T value, RedBlackNode<T> node) {

        if (node == null) {
            return null;
        }

        if (node.value == value) {

            if ((node.left == null) && (node.right == null)) {
                node = null;
            } else if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            } else {
                RedBlackNode<T> successor = findMin(node.right);

                node.value = successor.value;
                node = delete(successor.value, node);
            }

        } else if (value.compareTo(node.value) < 0) {
            node = delete(value, node.left);

        } else {
            node = delete(value, node.right);
        }

        node = rebalanceDelete(node);
        return node;
    }

    private RedBlackNode<T> findMin(RedBlackNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    private RedBlackNode<T> findMax(RedBlackNode<T> node) {
        while (node.right != null) {
            node = node.right;
        }

        return node;
    }

    private T find(T key, RedBlackNode<T> node) {
        if (node == null) {
            return null;
        }

        if (node.value == key) {
            return node.value;
        } else if (key.compareTo(node.value) < 0) {
            return find(key, node.left);
        } else {
            return find(key, node.right);
        }
    }

    protected boolean isRed(RedBlackNode<T> node) {
        return (node != null) && (node.color == Color.RED);
    }

    private RedBlackNode<T> rebalanceInsert(RedBlackNode<T> node) {

        /**
         * First situation. The inserted node, its parent and its uncle are red.
         * This is corrected two levels up, so node variable represents the grandparent
         * of the inserted node. Split into two ifs for readability
         */
        if ((isRed(node.left.left) || isRed(node.left.right)) && isRed(node.left) && isRed(node.right)) {
            node.left.color = Color.BLACK;
            node.right.color = Color.BLACK;
            node.color = Color.RED;
        } else if ((isRed(node.right.left) || isRed(node.right.right)) && isRed(node.right) && isRed(node.right)) {
            node.right.color = Color.BLACK;
            node.left.color = Color.BLACK;
            node.color = Color.RED;
        }

        /**
         * Second situation. The inserted node and its parent are red. Parents
         * sibling is black and the inserted node is the opposite child than the
         * parent is the child of the grandparent. The node variable represents
         * grandfather of the inserted node. This does not fix the problem but
         * instead it transforms it into third situation
         */
        if (isRed(node.left) && isRed(node.left.right) && !isRed(node.right)) {
            node.left = rotateLeft(node.left);
        } else if (isRed(node.right) && isRed(node.right.left) && !isRed(node.left)) {
            node.right = rotateRight(node.right);
        }

        /**
         * Third situation. The inserted node and its parent are red. Parents
         * sibling is black and the inserted node is the same child as the parent
         * node is the child of the grandparent. 
         */
        if (isRed(node.left) && isRed(node.left.left) && !isRed(node.right)) {
            node = rotateRight(node);
        } else if (isRed(node.right) && isRed(node.right.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }

        return node;
    }
    
    private RedBlackNode<T> rebalanceDelete(RedBlackNode<T> node) {
        
        
        
        return node;
    }

    /**
     * Rotates tree to the left
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private RedBlackNode<T> rotateLeft(RedBlackNode<T> node) {
        RedBlackNode<T> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        //updateHeight(node);
        //updateHeight(newRoot);
        return newRoot;
    }

    /**
     * Rotates tree to the right
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private RedBlackNode<T> rotateRight(RedBlackNode<T> node) {
        RedBlackNode<T> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        //node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        //newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;
        return newRoot;
    }

    /**
     * Rotates tree first left the right
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private RedBlackNode<T> rotateLeftRight(RedBlackNode<T> node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    /**
     * Rotates tree first right then left
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private RedBlackNode<T> rotateRightLeft(RedBlackNode<T> node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }
}
