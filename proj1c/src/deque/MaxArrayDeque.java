package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> compare;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        compare = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T max = this.get(0);
        for (T item : this) {
            if (compare.compare(max, item) < 0) {
                max = item;
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T max = this.get(0);
        for (T item : this) {
            if (c.compare(max, item) < 0) {
                max = item;
            }
        }
        return max;
    }
}
