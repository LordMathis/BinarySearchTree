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
public class AVLTreeTest {

    private AVLTree<Integer> instance;

    public AVLTreeTest() {
    }

    private boolean isBalanced(AVLTree.AVLNode<Integer> node) {
        if (node == null) {
            return true;
        } else {
            int leftHeight = getHeight(node.left);
            int rightHeight = getHeight(node.right);

            boolean leftBalanced = isBalanced(node.left);
            boolean rightBalanced = isBalanced(node.right);

            return leftBalanced && rightBalanced && (Math.abs(leftHeight - rightHeight) < 2);
        }

    }

    private int getHeight(AVLTree.AVLNode<Integer> node) {
        if (node == null) {
            return 0;
        } else {
            return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        }
    }
    
    private boolean isOrdered(AVLTree.AVLNode<Integer> node) {
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
    
    private boolean isValidAVLTree(AVLTree.AVLNode<Integer> node) {
        return isBalanced(node) && isOrdered(node);
    }

    @Before
    public void setUp() {
        instance = new AVLTree<>();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of insert method, of class AVLTree.
     */
    @Test
    public void testInsert() throws DuplicateValueException {
        System.out.println("Test insert");

        System.out.println("Trigger left rotation");
        instance.insert(1);
        instance.insert(2);
        instance.insert(3);
        assertTrue(isValidAVLTree(instance.root));

        instance.clear();

        System.out.println("Trigger right rotation");
        instance.insert(3);
        instance.insert(2);
        instance.insert(1);
        assertTrue(isValidAVLTree(instance.root));

        instance.clear();

        System.out.println("Trigger left-right rotation");
        instance.insert(3);
        instance.insert(1);
        instance.insert(2);
        assertTrue(isValidAVLTree(instance.root));

        instance.clear();

        System.out.println("Trigger right-left rotation");
        instance.insert(2);
        instance.insert(3);
        instance.insert(1);
        assertTrue(isValidAVLTree(instance.root));
    }

    @Test(expected = DuplicateValueException.class)
    public void testDuplicateValueException() throws DuplicateValueException {
        System.out.println("Attempt inserting duplicate values");
        instance.insert(1);
        instance.insert(2);
        instance.insert(3);
        instance.insert(2);
    }

    /**
     * Test of delete method, of class AVLTree.
     */
    @Test
    public void testDelete() throws DuplicateValueException {
        System.out.println("Test delete: ");

        System.out.println("Trigger left rotation");
        instance.insert(2);
        instance.insert(3);
        instance.insert(1);
        instance.insert(4);
        instance.delete(1);
        assertTrue(isValidAVLTree(instance.root));

        instance.clear();

        System.out.println("Trigger right rotation");
        instance.insert(3);
        instance.insert(2);
        instance.insert(4);
        instance.insert(1);
        instance.delete(4);
        assertTrue(isValidAVLTree(instance.root));

        instance.clear();

        System.out.println("Trigger left-right rotation");
        instance.insert(2);
        instance.insert(4);
        instance.insert(1);
        instance.insert(3);
        instance.delete(1);
        assertTrue(isValidAVLTree(instance.root));

        instance.clear();

        System.out.println("Trigger right-left rotation");
        instance.insert(3);
        instance.insert(1);
        instance.insert(4);
        instance.insert(2);
        instance.delete(4);

        assertTrue(isValidAVLTree(instance.root));
    }

    /**
     * Test of find method, of class AVLTree.
     */
    @Test
    public void testFind() throws DuplicateValueException {
        System.out.println("Test find: ");

        int[] inserts = {1, 2, 3, 4, 5};
        for (int insert : inserts) {
            instance.insert(insert);
        }
        
        System.out.println("");

        assertEquals(new Integer(1), instance.find(1));
        assertEquals(new Integer(2), instance.find(2));
        assertEquals(new Integer(3), instance.find(3));
        assertEquals(new Integer(4), instance.find(4));
        assertEquals(new Integer(5), instance.find(5));
        assertEquals(null, instance.find(6));
    }

}
