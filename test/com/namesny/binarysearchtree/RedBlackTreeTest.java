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
            return left +1;
        } else {
            return  -1;
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
    public void testInsert() throws Exception {
        System.out.println("insert");
        Object value = null;
        RedBlackTree instance = new RedBlackTree();
        instance.insert(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class RedBlackTree.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        Object key = null;
        RedBlackTree instance = new RedBlackTree();
        instance.delete(key);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of find method, of class RedBlackTree.
     */
    @Test
    public void testFind() {
        System.out.println("find");
        Object key = null;
        RedBlackTree instance = new RedBlackTree();
        Object expResult = null;
        Object result = instance.find(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findMin method, of class RedBlackTree.
     */
    @Test
    public void testFindMin() {
        System.out.println("findMin");
        RedBlackTree instance = new RedBlackTree();
        Object expResult = null;
        Object result = instance.findMin();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findMax method, of class RedBlackTree.
     */
    @Test
    public void testFindMax() {
        System.out.println("findMax");
        RedBlackTree instance = new RedBlackTree();
        Object expResult = null;
        Object result = instance.findMax();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
