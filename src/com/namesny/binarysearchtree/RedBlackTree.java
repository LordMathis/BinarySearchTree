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
        public RedBlackNode(T value, RedBlackNode<T> left, RedBlackNode<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        /**
         * Creates one node without children
         *
         * @param value
         */
        public RedBlackNode(T value) {
            this(value, null, null);
        }
    }

    /**
     * Represents the color of a node
     */
    private static enum Color {
        RED, BLACK
    }

    @Override
    public void insert(T value) throws DuplicateValueException {
        insert(value, root);
    }

    @Override
    public void delete(T key) {
        delete(key, root);
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

        //node = rebalance(node);
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
        
        //node = rebalance(node);
        return node;
    }
    
    private RedBlackNode<T> findMin(RedBlackNode<T> node) {
        while(node.left != null) {
            node = node.left;
        }
        
        return node;
    }
    
    private RedBlackNode<T> findMax(RedBlackNode<T> node) {
        while(node.right != null) {
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
}
