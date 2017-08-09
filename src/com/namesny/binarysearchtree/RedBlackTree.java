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
        root = new RedBlackNode<T>();
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
        public RedBlackNode(T value, RedBlackNode<T> left, RedBlackNode<T> right, Color color, RedBlackNode<T> parent) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.color = color;
            this.parent = parent;
        }

        /**
         * Creates one node without children
         *
         * @param value
         */
        public RedBlackNode(T value, RedBlackNode<T> parent) {
            this(value, new RedBlackNode<T>(), new RedBlackNode<T>(), Color.RED, parent);
        }

        public RedBlackNode() {
            this(null, null, null, Color.BLACK, null);
        }

    }

    /**
     * Represents the color of a node
     */
    protected static enum Color {
        RED, BLACK
    }

    /**
     * Inserts the value to the tree and re-balances it if necessary
     *
     * @param value value to insert
     * @throws DuplicateValueException
     * @throws IllegalArgumentException
     */
    @Override
    public void insert(T value) throws DuplicateValueException {

        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        if (root.value == null) {
            root = new RedBlackNode<>(value, null);
            root.left.parent = root;
            root.right.parent = root;
        } else {

            RedBlackNode<T> node = root;
            RedBlackNode<T> previous = null;
            while (node.value != null) {
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

            node = new RedBlackNode<>(value, previous);
            node.left.parent = node;
            node.right.parent = node;
            if (value.compareTo(previous.value) < 0) {
                previous.left = node;
            } else {
                previous.right = node;
            }

            rebalanceInsert(node);

        }
        root.color = Color.BLACK;
    }

    /**
     * Deletes an object equal to the key from the tree and re-balances it if
     * necessary
     *
     * @param key key to delete
     * @throws IllegalArgumentException
     */
    @Override
    public void delete(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        delete(key, root);
    }

    /**
     * Finds an object equal to key
     *
     * @param key
     * @return the object equal to key
     * @throws IllegalArgumentException
     */
    @Override
    public T find(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        return find(key, root);
    }

    /**
     * Clears the tree
     */
    @Override
    public void clear() {
        this.root = new RedBlackNode<>();
    }

    /**
     *
     * @return True if the tree is empty
     */
    @Override
    public boolean isEmpty() {
        return root.value == null;
    }

    /**
     * Finds minimum in the tree
     *
     * @return Object with minimal value
     */
    @Override
    public T findMin() {
        return findMin(root).value;
    }

    /**
     * Finds maximum in the tree
     *
     * @return Object with maximal value
     */
    @Override
    public T findMax() {
        return findMax(root).value;
    }

    /**
     * Helper method for deleting a node from the tree
     *
     * @param key the key to delete
     * @param node root of a subtree from which to delete the object
     */
    private void delete(T key, RedBlackNode<T> node) {

        // First we need to find the node we want to delete
        while ((node.value != null) && (node.value != key)) {
            if (key.compareTo(node.value) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        // if the node doesn't exist
        if (node.value == null) {
            return;
        }

        // The node has two children
        if ((node.left.value != null) && (node.right.value != null)) {

            RedBlackNode<T> successor = findMin(node.right);
            node.value = successor.value;
            delete(successor.value, node);

        } else {

            // The node has at most one internal child
            RedBlackNode<T> child = node.left.value != null ? node.left : node.right;

            // Deleted node is Red => just replace it with its child
            if (isRed(node)) {
                if (node.parent.left == node) {
                    node.parent.left = child;
                } else {
                    node.parent.right = child;
                }
                child.parent = node.parent;

            } else {
                // Deleted node is black but has a red child => recolor the child
                if (isRed(child)) {
                    child.color = Color.BLACK;

                    if (node.parent.left == node) {
                        node.parent.left = child;
                    } else {
                        node.parent.right = child;
                    }

                    child.parent = node.parent;

                } else {
                    // Deleted node is black and has a black child
                    if (node.parent.left == node) {
                        node.parent.left = child;
                    } else {
                        node.parent.right = child;
                    }

                    child.parent = node.parent;
                    rebalanceDelete(child);
                }
            }
        }
    }

    /**
     * Helper method for finding minimum
     *
     * @param node root of a subtree where to find minimum
     * @return the node with minimal value
     */
    private RedBlackNode<T> findMin(RedBlackNode<T> node) {

        // Minimum is in the leftmost node
        while (node.left.value != null) {
            node = node.left;
        }

        return node;
    }

    /**
     * Helper method for finding maximum
     *
     * @param node root of a subtree where to find minimum
     * @return the node with maximal value
     */
    private RedBlackNode<T> findMax(RedBlackNode<T> node) {

        // Maximum is in the rightmost node
        while (node.right.value != null) {
            node = node.right;
        }

        return node;
    }

    /**
     * Helper method for finding a node in the tree
     *
     * @param key the value of a node we are looking for
     * @param node root of a subtree where to look
     * @return the node with a value equal to key
     */
    private T find(T key, RedBlackNode<T> node) {

        // Traverse the tree until we find the value or an external node
        while ((node.value != null) && (node.value != key)) {
            if (key.compareTo(node.value) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return node.value;
    }

    /**
     * Determines the color of a node
     *
     * @param node the node which color we want to know
     * @return True if the node is red
     */
    protected boolean isRed(RedBlackNode<T> node) {
        return (node.value != null) && (node.color == Color.RED);
    }

    /**
     * Re-balances the tree after an insertion
     *
     * @param node root of a subtree that needs re-balancing
     */
    private void rebalanceInsert(RedBlackNode<T> node) {

        /**
         * First situation. The inserted node, its parent and its uncle are red.
         * This is corrected two levels up, so node variable represents the
         * grandparent of the inserted node. Split into two ifs for readability
         */
        if ((node.left.value != null)
                && (isRed(node.left.left) || isRed(node.left.right))
                && isRed(node.left)
                && isRed(node.right)) {
            node.left.color = Color.BLACK;
            node.right.color = Color.BLACK;
            node.color = Color.RED;
        } else if ((node.right.value != null)
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
        if ((node.left.value != null)
                && isRed(node.left)
                && isRed(node.left.right)
                && !isRed(node.right)) {
            rotateLeft(node.left);
        } else if ((node.right.value != null)
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
        if ((node.left.value != null)
                && isRed(node.left)
                && isRed(node.left.left)
                && !isRed(node.right)) {
            rotateRight(node);
            node.parent.right.color = Color.RED;
            node.parent.color = Color.BLACK;
        } else if ((node.right.value != null)
                && isRed(node.right)
                && isRed(node.right.right)
                && !isRed(node.left)) {
            rotateLeft(node);
            node.parent.left.color = Color.RED;
            node.parent.color = Color.BLACK;
        }

        // Rebalance parent
        if (node.parent != null) {
            rebalanceInsert(node.parent);
        }
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

    /**
     * Re-balances the tree after deleting a node.
     *
     * @param node double-black node that needs re-balancing
     */
    private void rebalanceDelete(RedBlackNode<T> node) {

        if (node == root) {
            return;
        }

        /*
         * If the double-black node is left child
         * The cases for the node being right child are symetrical
         */
        if (node.parent.left == node) {
            RedBlackNode<T> sibling = node.parent.right;

            // Case 1: The sibling of the double-black node is red
            if (isRed(sibling)) {
                node.parent.color = Color.RED;
                sibling.color = Color.BLACK;
                rotateLeft(node.parent);
                // We have transformed it into case 2,3 or 4
            }

            sibling = node.parent.right; // The sibling could have changed after the rotation in case 1
            // Case 2: The sibling is black and has two black children
            if (!isRed(sibling) && !isRed(sibling.left) && !isRed(sibling.right)) {
                sibling.color = Color.RED;
                if (isRed(node.parent)) {
                    // If the parent is red we recolor it black and we are done
                    node.parent.color = Color.BLACK;
                    return;
                } else {
                    // If the parent is black we make it double-black and move up a level to rebalance it
                    rebalanceDelete(node.parent);
                    return;
                }
            }

            sibling = node.parent.right; // The sibling could have changed
            // Case 3: The sibling is black, its left child is red and its right child is black
            if (!isRed(sibling) && isRed(sibling.left) && !isRed(sibling.right)) {
                sibling.left.color = Color.BLACK;
                sibling.color = Color.RED;
                rotateRight(sibling);
                // This is now case 4
            }

            sibling = node.parent.right; // The sibling could have changed
            // Case 4: The sibling is black, its left child is black and its right child is red
            if (!isRed(sibling) && !isRed(sibling.left) && isRed(sibling.right)) {
                sibling.right.color = Color.BLACK;
                if (isRed(node.parent)) {
                    node.parent.color = Color.BLACK;
                    sibling.color = Color.RED;
                }

                rotateLeft(node.parent);
            }
        } else {
            RedBlackNode<T> sibling = node.parent.left;

            // Case 1: The sibling of the double-black node is red
            if (isRed(sibling)) {
                node.parent.color = Color.RED;
                sibling.color = Color.BLACK;
                rotateRight(node.parent);
                // We have transformed it into case 2,3 or 4
            }

            sibling = node.parent.left; // The sibling could have changed after the rotation in case 1
            // Case 2: The sibling is black and has two black children
            if (!isRed(sibling) && !isRed(sibling.left) && !isRed(sibling.right)) {
                sibling.color = Color.RED;
                if (isRed(node.parent)) {
                    // If the parent is red we recolor it black and we are done
                    node.parent.color = Color.BLACK;
                    return;
                } else {
                    // If the parent is black we make it double-black and move up a level to rebalance it
                    rebalanceDelete(node.parent);
                    return;
                }
            }

            sibling = node.parent.left; // The sibling could have changed
            // Case 3: The sibling is black, its left child is black and its right child is red
            if (!isRed(sibling) && !isRed(sibling.left) && isRed(sibling.right)) {
                sibling.right.color = Color.BLACK;
                sibling.color = Color.RED;
                rotateLeft(sibling);
                // This is now case 4
            }

            sibling = node.parent.left; // The sibling could have changed
            // Case 4: The sibling is black, its left child is red and its right child is black
            if (!isRed(sibling) && isRed(sibling.left) && !isRed(sibling.right)) {
                sibling.left.color = Color.BLACK;
                if (isRed(node.parent)) {
                    node.parent.color = Color.BLACK;
                    sibling.color = Color.RED;
                }

                rotateRight(node.parent);
            }
        }
    }

}
