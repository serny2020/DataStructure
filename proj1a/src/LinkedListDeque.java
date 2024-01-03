import java.util.ArrayList;
import java.util.List;

/*
 * Double  Deque implementation
 * Author: Xiaocheng Sun
 * Date:   Jan 2nd, 2024
 * Deque implementation with doubly linked list for cs61b 2023fall project1a
 * The topology of this doubly linked list is the circular SENTINEL.
 */
public class LinkedListDeque<T> implements Deque<T> {
    /*
     * Internal node
     */
    private class Node {

        public Node prev;
        public T item;
        public Node next;

        public Node(
                Node prev,
                T item, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

    }

    private int size = 0;
    private final Node SENTINEL;

    /**
     * Deque constructor that creates the deque instance
     */
    public LinkedListDeque() {
        SENTINEL = new Node(null, null, null);
        SENTINEL.next = SENTINEL;
        SENTINEL.prev = SENTINEL;
    }


    public void addFirst(T x) {
        Node first = new Node(SENTINEL, x, SENTINEL.next);
        SENTINEL.next.prev = first;
        SENTINEL.next = first;
        size += 1;
    }

    public void addLast(T x) {
        Node last = new Node(SENTINEL.prev, x, SENTINEL);
        SENTINEL.prev.next = last;
        SENTINEL.prev = last;
        size += 1;
    }

    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node curr = SENTINEL.next;
        for (int i = 0; i < size; i++) {
            returnList.add(curr.item);
            curr = curr.next;

        }
        return returnList;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T delete = SENTINEL.next.item; //Store the value and allow garbage collector to delete the object.
        SENTINEL.next = SENTINEL.next.next;
        SENTINEL.next.prev = SENTINEL;
        size -= 1;
        return delete;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T delete = SENTINEL.prev.item; //Store the value and allow garbage collector to delete the object.
        SENTINEL.prev.prev.next = SENTINEL;
        SENTINEL.prev = SENTINEL.prev.prev;
        size -= 1;
        return delete;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            Node curr = SENTINEL;
            for (int i = 0; i <= index; i++) {
                curr = curr.next;
            }
            return curr.item;
        }
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node curr = SENTINEL.next;
        if (index == 0) {
            return curr.item;
        }
        return getRecursive(index - 1);
    }
}
