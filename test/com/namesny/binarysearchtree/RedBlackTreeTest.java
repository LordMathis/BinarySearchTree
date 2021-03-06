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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        if (node.left.value != null) {
            if (node.left.value.compareTo(node.value) > 0) {
                return false;
            } else {
                return isOrdered(node.left);
            }
        }

        if (node.right.value != null) {
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
            return instance.isRed(node) ? left : left + 1;
        } else {
            return -1;
        }
    }

    private boolean checkTwoRedNodes(RedBlackTree.RedBlackNode<Integer> node) {

        if (node == null) {
            return true;
        }

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

        System.out.println("Test case 1");
        insertMore(5, 3, 7, 1);
        assertTrue(isValidRedBlackTree(instance.root));

        instance.clear();

        System.out.println("Test case 2");
        insertMore(5, 1, 3);
        assertTrue(isValidRedBlackTree(instance.root));

        instance.clear();

        System.out.println("Test case 3");
        insertMore(5, 3, 1);
        assertTrue(isValidRedBlackTree(instance.root));

        instance.clear();

        int k = 50;
        int n = 5;
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            System.out.println("Test random input " + (i + 1));
            int numbers = k * (i + 1);
            for (int j = 0; j < numbers; j++) {
                instance.insert(random.nextInt());
            }
            assertTrue(isValidRedBlackTree(instance.root));
        }
    }

    /**
     * Test of delete method, of class RedBlackTree.
     */
    @Test
    public void testDelete() throws DuplicateValueException {
        System.out.println("Test delete");

        int maxInsert = 500;
        Random random = new Random();
        List<Integer> numbers = new ArrayList<>();

        for (int j = 0; j < maxInsert; j++) {
            int number = random.nextInt();
            numbers.add(number);
            instance.insert(number);
        }

        int maxDelete = 100;
        for (int i = 0; i < maxDelete; i++) {
            int index = random.nextInt(numbers.size());
            instance.delete(numbers.remove(index));
            if (i % 10 == 0) {
                assertTrue(isValidRedBlackTree(instance.root));
            }
        }

        assertTrue(isValidRedBlackTree(instance.root));
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
