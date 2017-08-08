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
    protected RedBlackNode root;

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
        public RedBlackNode(T value, RedBlackNode left, RedBlackNode right) {
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
    public void insert(T value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(T key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T find(T key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T findMin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T findMax() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
