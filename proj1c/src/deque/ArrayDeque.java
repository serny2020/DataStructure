package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private static final double FACTOR = 0.25;
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /**
     * Resize function modify the size of underlying array and
     * copy all the elements in the original array to the new one starting at index 0.
     * The new nextFirst points to the end of backing array and the new nextLast is one
     * position pass the last element.
     *
     * @param capacity the size of backing array to be created
     */
    private void resize(int capacity) {
        T[] newArr = (T[]) new Object[capacity];
        //copy to new array starting at index 0
        for (int i = 0; i < size; i++) {
            newArr[i] = get(i);
        }
        items = newArr;
        nextFirst = items.length - 1; //nextFirst should be at the end of array
        nextLast = size; //nextLast should be at index pass the last element
    }

    /**
     * Convert between the index that client want and the actual index
     * of the backing array.
     *
     * @param index of the array that clients want to access
     * @return actualIndex in the backing array
     */
    private int transformer(int index) {
        int actualIndex = index % items.length;
        if (index < 0) {
            actualIndex = actualIndex + items.length;
        }
        return actualIndex;
    }

    public void addFirst(T x) {
        //check if array is filled
        if (nextFirst == nextLast) {
            resize(size * 2);
        }
        //implement a circular array
        items[nextFirst] = x;
        nextFirst = transformer(nextFirst - 1);
        size += 1;

    }

    public void addLast(T x) {
        //check if array is filled
        if (size == items.length) {
            resize(size * 2);
        }
        //implement a circular array
        items[nextLast] = x;
        nextLast = transformer(nextLast + 1);
        size += 1;
    }

    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            returnList.add(get(i));
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
        //Resizing down the number of elems in the array under 25% for arrays of
        //length 16 or more.
        if ((size <= FACTOR * items.length) && (size >= 4)) {
            resize(items.length / 2);
        }
        T firstItem = get(0);
        //remove item in the array
        int firstItemIndex = transformer(nextFirst + 1);
        items[firstItemIndex] = null;
        size -= 1;
        //adjust the nextFirst index
        nextFirst = transformer(nextFirst + 1);
        return firstItem;

    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        //Resizing down the number of elems in the array under 25% for arrays of
        //length 16 or more.
        if ((size <= FACTOR * items.length) && (items.length >= 16)) {
            resize(items.length / 2);
        }
        T lastItem = get(size - 1);
        //remove item in the array
        int lastItemIndex = transformer(nextLast - 1);
        items[lastItemIndex] = null;
        size -= 1;
        //adjust the nextLast index
        nextLast = transformer(nextLast - 1);
        return lastItem;
    }

    public T get(int index) {
        int actualIndex = (nextFirst + 1 + index) % items.length;
        return items[actualIndex];
    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int curPtr;

        public ArrayDequeIterator() {
            curPtr = 0;
        }

        public boolean hasNext() {
            return curPtr < size;
        }

        public T next() {
            T returnItem = get(curPtr); //accessing the current elem.
            curPtr += 1;
            return returnItem;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /**
     * @param x
     * @return ture if ArrayDeque has elem. x
     */
    private boolean contains(T x) {
        for (int i = 0; i < size; i++) {
            if (get(i).equals(x)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof ArrayDeque otherDeque) {
            if (this.size != otherDeque.size) {
                return false;
            }
            for (T x : this) {
                if (!otherDeque.contains(x)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder returnSB = new StringBuilder("[");
        for (int i = 0; i < size - 1; i += 1) {
            returnSB.append(get(i));
            returnSB.append(", ");
        }
        //append last elem without comma
        returnSB.append(get(size - 1));
        returnSB.append("]");
        return returnSB.toString();
    }
}
