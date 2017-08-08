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
         * Parent of the node
         */
        protected RedBlackNode<T> parent;

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

        if (root == null) {
            root = new RedBlackNode<>(value);
            root.parent = null;
        } else {

            RedBlackNode<T> node = root;
            RedBlackNode<T> previous = null;
            while (node != null) {
                if (value.compareTo(node.value) < 0) {
                    previous = node;
                    node = node.left;
                } else if (value.compareTo(node.value) > 0) {
                    previous = node;
                    node = node.right;
                } else {
                    throw new DuplicateValueException("Duplicate value: " + value);
                }
            }

            node = new RedBlackNode<>(value);
            node.parent = previous;
            if (value.compareTo(previous.value) < 0) {
                previous.left = node;
            } else {
                previous.right = node;
            }

            rebalanceInsert(node);

        }
        root.color = Color.BLACK;
    }

    @Override
    public void delete(T key) {

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

    private void delete(T key, RedBlackNode<T> node) {

        while ((node != null) && (node.value != key)) {
            if (key.compareTo(node.value) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (node == null) {
            return;
        }

        if ((node.left != null) && (node.right != null)) {

            RedBlackNode<T> successor = findMin(node.right);
            node.value = successor.value;
            delete(successor.value, node);

        } else {
            RedBlackNode<T> child = node.left != null ? node.left : node.right;

            // Deleted node is Red => just replace it with its child
            if (node.color == Color.RED) {
                if (node.parent.left == node) {
                    node.parent.left = child;
                } else {
                    node.parent.right = child;
                }

            } else {
                // Deleted node is black but has a red child
                if (child.color == Color.RED) {
                    child.color = Color.BLACK;
                    if (node.parent.left == node) {
                        node.parent.left = child;
                    } else {
                        node.parent.right = child;
                    }

                    // Deleted node is black and has a black child
                } else {

                }
            }

        }
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

    private void rebalanceInsert(RedBlackNode<T> node) {

        /**
         * First situation. The inserted node, its parent and its uncle are red.
         * This is corrected two levels up, so node variable represents the
         * grandparent of the inserted node. Split into two ifs for readability
         */
        if ((node.left != null)
                && (isRed(node.left.left) || isRed(node.left.right))
                && isRed(node.left)
                && isRed(node.right)) {
            node.left.color = Color.BLACK;
            node.right.color = Color.BLACK;
            node.color = Color.RED;
        } else if ((node.right != null)
                && (isRed(node.right.left) || isRed(node.right.right))
                && isRed(node.right)
                && isRed(node.left)) {
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
        if ((node.left != null)
                && isRed(node.left)
                && isRed(node.left.right)
                && !isRed(node.right)) {
            rotateLeft(node.left);
        } else if ((node.right != null)
                && isRed(node.right)
                && isRed(node.right.left)
                && !isRed(node.left)) {
            rotateRight(node.right);
        }

        /**
         * Third situation. The inserted node and its parent are red. Parents
         * sibling is black and the inserted node is the same child as the
         * parent node is the child of the grandparent.
         */
        if ((node.left != null)
                && isRed(node.left)
                && isRed(node.left.left)
                && !isRed(node.right)) {
            rotateRight(node);
            node.parent.right.color = Color.RED;
            node.parent.color = Color.BLACK;
        } else if ((node.right != null)
                && isRed(node.right)
                && isRed(node.right.right)
                && !isRed(node.left)) {
            rotateLeft(node);
            node.parent.left.color = Color.RED;
            node.parent.color = Color.BLACK;
        }

        if (node.parent != null) {
            rebalanceInsert(node.parent);
        }
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
    private void rotateLeft(RedBlackNode<T> node) {
        RedBlackNode<T> newRoot = node.right;
        RedBlackNode<T> parent = node.parent;

        if (node == root) {
            root = newRoot;
        }

        newRoot.parent = parent;

        if (parent != null) {
            if (parent.value.compareTo(newRoot.value) < 0) {
                parent.right = newRoot;
            } else {
                parent.left = newRoot;
            }
        }

        node.right = newRoot.left;
        if (newRoot.left != null) {
            newRoot.left.parent = node;
        }

        newRoot.left = node;
        node.parent = newRoot;

    }

    /**
     * Rotates tree to the right
     *
     * @param node the node where to rotate
     * @return new rotated tree
     */
    private void rotateRight(RedBlackNode<T> node) {
        RedBlackNode<T> newRoot = node.left;
        RedBlackNode<T> parent = node.parent;

        if (node == root) {
            root = newRoot;
        }

        newRoot.parent = parent;

        if (parent != null) {
            if (parent.value.compareTo(newRoot.value) < 0) {
                parent.right = newRoot;
            } else {
                parent.left = newRoot;
            }
        }

        node.left = newRoot.right;
        if (newRoot.right != null) {
            newRoot.right.parent = node;
        }

        newRoot.right = node;
        node.parent = newRoot;

    }

}
