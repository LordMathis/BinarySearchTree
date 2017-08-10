# BinarySearchTree

Java library with various binary search tree implementations. The tree classes are generic so they can be used with any object that implements interface Comparable. The implementations are well tested and are believed to be correct. If you find a problem, please open a new issue.

## Currently implemented

* [AVL Tree](BinarySearchTree/src/com/namesny/binarysearchtree/AVLTree.java)
* [Red-Black Tree](BinarySearchTree/src/com/namesny/binarysearchtree/RedBlackTree.java)

## Usage

### AVL Tree

```java
AVLTree<Integer> avlTree = new AVLTree<>();

avlTree.insert(20);
avlTree.insert(3);
avlTree.insert(18);
avlTree.insert(45);

System.out.println(avlTree.findMin()); // 3

avlTree.delete(3);

System.out.println(avlTree.findMin()); // 18

System.out.println(avlTree.findMax()); // 45

avlTree.find(20); // returns 20

avlTree.clear();
avlTree.isEmpty(); // True
```

### Red-Black Tree

```java
RedBlackTree<String> rbTree = new RedBlackTree<>();

rbTree.insert("echo");
rbTree.insert("alpha");
rbTree.insert("charlie");
rbTree.insert("bravo");
rbTree.insert("delta");

System.out.println(rbTree.findMin()); // "alpha"
System.out.println(rbTree.findMax()); // "echo"

rbTree.delete("echo");

System.out.println(rbTree.findMax()); // "delta"

rbTree.find("charlie"); // returns "charlie"

rbTree.clear();
rbTree.isEmpty(); // True
```






