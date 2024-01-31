import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * Lab7: BST-based implementation of Map
 * Author: Xiaocheng Sun
 * Date:   Jun 13th
 * <p>
 * A data structure that uses a binary search tree to store pairs of keys and values.
 * Any key must appear at most once in the dictionary, but values may appear multiple
 * times. Key operations are get(key), put(key, value), and contains(key) methods. The value
 * associated to a key is the value in the last call to put with that key.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    /**
     * Keys and values are stored in a BST of BSTNode objects.
     * This variable stores the first pair in BSTMap.
     */
    private BSTNode root;
    private int size = 0;

    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        public BSTNode(K k, V v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }


    /**
     * Helper function to insert key-value pair into this dictionary.
     * If key to insert is smaller than the root node, place it to the left;
     * If key is larger than the root node, place it to the right.
     * Replace existing value, if any.
     *
     * @param root  node
     * @param key   to be inserted
     * @param value to be inserted
     * @return a BSTNode object
     */
    private BSTNode insert(BSTNode root, K key, V value) {
        if (root == null) {
            return new BSTNode(key, value);
        }
        if (key.compareTo(root.key) < 0) {
            root.left = insert(root.left, key, value);
        } else if (key.compareTo(root.key) > 0) {
            root.right = insert(root.right, key, value);
        } else {
            root.value = value;
            size -= 1; //offset one if replacing.s
        }
        return root;
    }

    @Override
    public void put(K key, V value) {
        root = insert(root, key, value); //root points to the root of the tree
        size += 1;
    }

    /**
     * Helper function to search the dictionary for a given key.
     * Return null if no such key found.
     *
     * @param key to look for
     * @return value correspond to the key
     */
    private V getHelper(BSTNode root, K key) {
        if (root == null) {
            return null;
        }
        if (key.compareTo(root.key) < 0) {
            return getHelper(root.left, key); //return value from the child
        } else if (key.compareTo(root.key) > 0) {
            return getHelper(root.right, key); //return value from the child
        }
        return root.value;
    }

    @Override
    public V get(K key) {
        return getHelper(root, key);
    }

    /**
     * Helper function to find if a given key is in the dictionary.
     *
     * @param root BSTNode
     * @param key  to look for
     * @return true if the key exist; false otherwise.
     */
    private boolean containsKeyHelper(BSTNode root, K key) {
        if (root == null) {
            return false;
        } else if (key.compareTo(root.key) < 0) {
            return containsKeyHelper(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            return containsKeyHelper(root.right, key);
        }
        return true;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelper(root, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public Set<K> keySet() {
        TreeSet<K> set = new TreeSet<>(); //return set of keys in order
        addKeys(root, set);
        return set;
    }

    /**
     * Helper function to add keys to set recursively
     *
     * @param root
     * @param set
     */
    private void addKeys(BSTNode root, Set<K> set) {
        if (root == null) {
            return;
        }
        set.add(root.key);
        addKeys(root.left, set);
        addKeys(root.right, set);
    }

    @Override
    public V remove(K key) {
        V returnValue = get(key);
        root = removeHelper(root, key);
        size -= 1;
        return returnValue;
    }

    /**
     * Remove helper function to remove the key from the tree
     *
     * @param root of the tree or subtree
     * @param key  to be removed
     * @return a BSTNode object with the key to be removed
     */
    private BSTNode removeHelper(BSTNode root, K key) {
        if (root == null) {
            return null; //cannot find the node
        }
        //recursive case
        if (key.compareTo(root.key) < 0) {
            root.left = removeHelper(root.left, key);
        } else if (key.compareTo(root.key) > 0) {
            root.right = removeHelper(root.right, key);
        } //found the key
        else {
            //delete node with one or no child
            if (root.left == null) { //remove current node
                return root.right;
            }
            if (root.right == null) { //remove current node
                return root.left;
            } else {
                //delete node with two children
                root.left = swapSmallest(root, root.left);
            }

        }
        //return removed node for recursion
        return root;
    }

    /**
     * Helper function find the successor of the root and swap
     * the value with the root, if there is one.
     *
     * @param root      of tree or subtree
     * @param leftChild of the root
     * @return the left child of node to be removed
     */
    private BSTNode swapSmallest(BSTNode root, BSTNode leftChild) {
        if (leftChild.right == null) {
            root.key = leftChild.key;
            root.value = leftChild.value;
            return leftChild.left;
        }
        //go all the way to right
        leftChild.right = swapSmallest(root, leftChild.right);
        return leftChild;
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Call the recursive helper function to print out the key-value pair
     * of the tree.
     */
    public void printInOrder() {
        printInOrder(root);
    }

    /**
     * Traverse the tree in order
     *
     * @param node of the root
     */
    private void printInOrder(BSTNode node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.println(node.key + " -> " + node.value);
        printInOrder(node.right);
    }

}
