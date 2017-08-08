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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matúš Námešný
 */
public class RedBlackTreeTest {

    private RedBlackTree<Integer> instance;

    public RedBlackTreeTest() {
    }

    @Before
    public void setUp() {
        instance = new RedBlackTree<>();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    private void insertMore(int... values) throws DuplicateValueException {
        for (int value : values) {
            instance.insert(value);
        }
    }

    private boolean isOrdered(RedBlackTree.RedBlackNode<Integer> node) {
        if (node.left != null) {
            if (node.left.value.compareTo(node.value) > 0) {
                return false;
            } else {
                return isOrdered(node.left);
            }
        }

        if (node.right != null) {
            if (node.right.value.compareTo(node.value) < 0) {
                return false;
            } else {
                return isOrdered(node.right);
            }
        }

        return true;
    }

    private boolean checkBlackHeight(RedBlackTree.RedBlackNode<Integer> node) {

        int left = checkBlackHeightWorker(instance.root.left);
        int right = checkBlackHeightWorker(instance.root.right);

        return (left == right) && (left != -1);
    }

    private int checkBlackHeightWorker(RedBlackTree.RedBlackNode<Integer> node) {
        if (node == null) {
            return 1;
        }

        int left = checkBlackHeightWorker(node.left);
        int right = checkBlackHeightWorker(node.right);
        if (left == right) {
            return left + 1;
        } else {
            return -1;
        }
    }

    private boolean checkTwoRedNodes(RedBlackTree.RedBlackNode<Integer> node) {
        if (instance.isRed(node) && (instance.isRed(node.left) || instance.isRed(node.right))) {
            return false;
        } else {
            return checkTwoRedNodes(node.left) && checkTwoRedNodes(node.right);
        }
    }

    private boolean isValidRedBlackTree(RedBlackTree.RedBlackNode<Integer> node) {
        return isOrdered(node) && checkBlackHeight(node) && checkTwoRedNodes(node);
    }

    /**
     * Test of insert method, of class RedBlackTree.
     */
    @Test
    public void testInsert() throws DuplicateValueException {
        System.out.println("Test insert");

        System.out.println("Test first situation");

        insertMore(5, 6, 3, 1);
        assertTrue(isValidRedBlackTree(instance.root));
        instance.clear();
        insertMore(5, 6, 4, 7);
        assertTrue(isValidRedBlackTree(instance.root));
        instance.clear();

        System.out.println("Test second situation");

        insertMore(5, 8, 3);
        instance.root.right.color = RedBlackTree.Color.BLACK;
        insertMore(4);
        assertTrue(isValidRedBlackTree(instance.root));
        instance.clear();

        insertMore(5, 8, 3);
        instance.root.left.color = RedBlackTree.Color.BLACK;
        insertMore(7);
        assertTrue(isValidRedBlackTree(instance.root));
        instance.clear();

        System.out.println("Test third situation");

        insertMore(5, 8, 3);
        instance.root.right.color = RedBlackTree.Color.BLACK;
        insertMore(1);
        assertTrue(isValidRedBlackTree(instance.root));
        instance.clear();

        insertMore(5, 8, 3);
        instance.root.left.color = RedBlackTree.Color.BLACK;
        insertMore(10);
        assertTrue(isValidRedBlackTree(instance.root));
        

    }

    /**
     * Test of delete method, of class RedBlackTree.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        //Object key = null;
        //RedBlackTree instance = new RedBlackTree();
        //instance.delete(key);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of find method, of class RedBlackTree.
     */
    @Test
    public void testFind() throws DuplicateValueException {
        System.out.println("Test find");

        insertMore(1, 2, 3, 4, 5);
        System.out.println("Insert values 1,2,3,4,5");

        assertEquals("Find 1", new Integer(1), instance.find(1));
        assertEquals("Find 2", new Integer(2), instance.find(2));
        assertEquals("Find 3", new Integer(3), instance.find(3));
        assertEquals("Find 4", new Integer(4), instance.find(4));
        assertEquals("Find 5", new Integer(5), instance.find(5));
        assertEquals("Attempt finding nonexistent value", null, instance.find(6));
    }

    /**
     * Test of findMin method, of class RedBlackTree.
     */
    @Test
    public void testFindMin() throws DuplicateValueException {
        System.out.println("Test findMin");

        insertMore(5, 6, 7, 8, 9);
        System.out.println("Insert values 5,6,7,8,9");

        assertEquals("Min should be 5", new Integer(5), instance.findMin());

        insertMore(1, 2, 3, 4, 10);
        System.out.println("Insert values 1,2,3,4,10");

        assertEquals("Min should be 1", new Integer(1), instance.findMin());

    }

    /**
     * Test of findMax method, of class RedBlackTree.
     */
    @Test
    public void testFindMax() throws DuplicateValueException {
        System.out.println("Test findMax");

        insertMore(5, 4, 3, 2, 1);
        System.out.println("Insert values 5,4,3,2,1");
        assertEquals("Max should be 5", new Integer(5), instance.findMax());

        insertMore(6, 7, 8, 9, 10);
        System.out.println("Insert values 6,7,8,9,10");
        assertEquals("Max should be 10", new Integer(10), instance.findMax());

    }

}
