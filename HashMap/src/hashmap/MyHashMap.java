package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author Xiaocheng Sun
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // it is an array of collection objects
    // where each bucket is an array of Collection<Node> objects
    private static final int INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private final double maxLoadFactor;
    private int size = 0;

    /**
     * Constructors
     */
    public MyHashMap() {
        this(INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor      maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        buckets = createTable(initialCapacity);
        this.maxLoadFactor = loadFactor;
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>(); //use linked list for hash table bucket
    }

    /**
     * Creates an array of Collection buckets where each of the bucket is
     * represented by a Collection of nodes.
     *
     * @param bucketSize
     */
    private Collection<Node>[] createTable(int bucketSize) {
        Collection<Node>[] table = new Collection[bucketSize];
        for (int i = 0; i < bucketSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    /**
     * Get the bucket index of the key
     *
     * @param key that needs to find where the bucket location is
     * @return the bucket index where the key is located
     */
    private int getBucketSpot(K key) {
        return getBucketIndex(key, buckets);
    }

    /**
     * Helper function for getIndex, calculate the bucket index of a given key.
     * Use floorMod to make sure the resulting index is within the range.
     *
     * @param key     to find the bucket spot
     * @param buckets an array of node collection
     * @return the array index of that bucket where the key should locate.
     */

    private int getBucketIndex(K key, Collection<Node>[] buckets) {
        int hashCode = key.hashCode();
        return Math.floorMod(hashCode, buckets.length); //make sure mode always fall in range
    }

    /**
     * Check if the buckets reach the maximum level of collision.
     *
     * @return true if collision is greater than the load factor; false otherwise.
     */
    private boolean reachMaxLoadFactor() {
        return (1.0 * size / buckets.length) > maxLoadFactor;
    }

    /**
     * Get the node from the bucket based on the given key value
     *
     * @param key of the node that we need to get
     * @return the Node of that specific key
     */
    private Node getNode(K key) {
        //find out the bucket spot where the key is located.
        int bucketIndex = getBucketSpot(key);
        //search that bucket spot and return the node.
        return getNodeFromBucket(key, bucketIndex);
    }

    /**
     * Helper function for getNode that finds the node from the specific bucket spot.
     * Use equals function to check if there is a key in that bucket spot.
     *
     * @param key         that needs to search
     * @param bucketIndex of the bucket spot that contains the key
     * @return the node of the given key
     */
    private Node getNodeFromBucket(K key, int bucketIndex) {
        //check if the key is in the node, return the node if found.
        for (Node node : buckets[bucketIndex]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        //return null if no such key exist.
        return null;
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketSpot(key);
        Node node = getNode(key);
        // if the key already exist, replace the old value and finished.
        if (node != null) {
            node.value = value;
            return;
        }
        // if no key found, add the new key
        node = new Node(key, value);
        buckets[bucketIndex].add(node);
        size += 1;
        // check if collision reach the load factor
        if (reachMaxLoadFactor()) {
            resize(buckets.length * 2);
        }
    }

    /**
     * Resize the current buckets and copy over the old value to the
     * new buckets.
     *
     * @param capacity of the new buckets table
     */
    private void resize(int capacity) {
        Collection<Node>[] newBuckets = createTable(capacity);
        //iterate the old array and copy the value to the new buckets.
        Iterator<Node> nodeIterator = new HashMapeIterator();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            int newBucketIndex = getBucketIndex(node.key, newBuckets);
            newBuckets[newBucketIndex].add(node);
        }
        //set the current buckets to the new buckets.
        buckets = newBuckets;
    }

    @Override
    public V get(K key) {
        //search nodes from the key
        Node node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = createTable(INITIAL_CAPACITY); //destroy the old buckets and create a new one.
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    @Override
    public V remove(K key) {
        //search the key, if nothing was found return null
        int bucketIndex = getBucketSpot(key);
        Node node = getNode(key);
        if (node == null) {
            return null;
        }
        //if found, remove the key
        buckets[bucketIndex].remove(node);
        size -= 1;
        return node.value;
    }

    @Override
    public Iterator<K> iterator() {
        return new KeyIterator();
    }

    /**
     * Key iterator class that traverse the nodes and returns iterator of key
     */
    private class KeyIterator implements Iterator<K> {

        Iterator<Node> nodeIterator = new HashMapeIterator();

        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        public K next() {
            return nodeIterator.next().key;
        }
    }

    /**
     * An iterator class that traverse all the nodes in the hash map.
     * First get the iterator of the outer array and then implement the iterator function
     * for traversing every node inside each bucket.
     * This class returns iterator of node.
     */
    private class HashMapeIterator implements Iterator<Node> {
        //convert array to stream iterator.
        private Iterator<Collection<Node>> bucketsIterator = Arrays.stream(buckets).iterator();
        //current bucket iterator used to check if the current bucket is iterable
        private Iterator<Node> currentBucketIterator;
        private int currNode = 0;

        public boolean hasNext() {

            return currNode < size;
        }

        public Node next() {
            // iterate the outside buckets
            // initialize the first bucket or move to next bucket if no more nodes in the current bucket
            if (currentBucketIterator == null || !currentBucketIterator.hasNext()) {
                //set the current bucket to the next bucket spot that is not empty
                Collection<Node> currentBucket = bucketsIterator.next();
                while (currentBucket.size() == 0) {
                    currentBucket = bucketsIterator.next();
                }
                //initialize the bucket iterator.
                currentBucketIterator = currentBucket.iterator();
            }
            //iterate the nodes inside current bucket
            currNode++;

            return currentBucketIterator.next();
        }
    }

}
