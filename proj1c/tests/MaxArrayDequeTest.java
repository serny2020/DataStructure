import deque.MaxArrayDeque;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static com.google.common.truth.Truth.assertThat;

public class MaxArrayDequeTest {

    @Test
    public void integerTest() {
        Comparator<Integer> compareInt = new Comparator<>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };

        MaxArrayDeque<Integer> maxInt = new MaxArrayDeque<>(compareInt);

        for (int i = 0; i < 100; i++) {
            maxInt.addFirst(i);
        }

        assertThat(maxInt.max()).isEqualTo(99);
    }

    @Test
    public void stringTest() {
        Comparator<String> compareString = new Comparator<>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<String> maxString = new MaxArrayDeque<>(compareString);

        maxString.addFirst("apple");
        maxString.addFirst("banana");
        maxString.addFirst("orange");
        maxString.addFirst("pineapples");
        maxString.addFirst("strawberries");

        assertThat(maxString.max()).isEqualTo("strawberries");
    }

    @Test
    public void maxTest() {
        class Dog {
            private int size;
            private String name;

            public Dog(String n, int s) {
                name = n;
                size = s;
            }
        }
        Comparator<Dog> compareName = new Comparator<>() {
            @Override
            public int compare(Dog o1, Dog o2) {
                return o1.name.compareTo(o2.name);
            }
        };

        Dog d1 = new Dog("Elyse", 3);
        Dog d2 = new Dog("Sture", 9);
        Dog d3 = new Dog("Benjamin", 15);

        MaxArrayDeque<Dog> dogs = new MaxArrayDeque<>(compareName);
        dogs.addFirst(d1);
        dogs.addFirst(d2);
        dogs.addFirst(d3);

        Comparator<Dog> compareSize = new Comparator<>() {
            @Override
            public int compare(Dog o1, Dog o2) {
                return o1.size - o2.size;
            }
        };

        assertThat(dogs.max().name).isEqualTo("Sture");
        assertThat(dogs.max(compareSize).name).isEqualTo("Benjamin");
    }

}
