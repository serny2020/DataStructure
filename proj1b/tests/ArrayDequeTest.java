import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    @DisplayName("ArrayDeque has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    // Below, you'll write your own tests for LinkedListDeque.
    @Test
    /* This test check if isEmpty function works correctly*/
    public void testIsEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.isEmpty()).isTrue();
    }

    @Test
    /* This test check if the size of an empty deque is 0*/
    public void testSizeZero() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.size()).isEqualTo(0);
    }

    @Test
    /* This test check the get function of deque*/
    public void testGet() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(1);
        lld1.addLast(2);
        lld1.addLast(3);
        assertThat(lld1.get(0)).isEqualTo(1);
        assertThat(lld1.get(-1)).isNull();
        assertThat(lld1.get(28723)).isNull();
    }

    @Test
    public void removeEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.removeFirst()).isNull();
        assertThat(lld1.removeLast()).isNull();
    }

    @Test
    public void removeFirstBasic() {
        Deque<Integer> lld1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.removeFirst(); // [0, 1]

        assertThat(lld1.toList()).containsExactly(0, 1).inOrder();
    }


    @Test
    public void removeFirstToTheEnd() {
        Deque<Integer> lld1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.removeFirst(); // [-1, 0, 1, 2]
        lld1.removeFirst(); // [0, 1, 2]
        lld1.removeFirst(); // [1, 2]
        lld1.removeFirst(); // [2]

        assertThat(lld1.toList()).containsExactly(2).inOrder();
    }

    @Test
    public void removeFirstToNull() {
        Deque<Integer> lld1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.removeFirst(); // [-1, 0, 1, 2]
        lld1.removeFirst(); // [0, 1, 2]
        lld1.removeFirst(); // [1, 2]
        lld1.removeFirst(); // [2]
        lld1.removeFirst(); // []

        assertThat(lld1.toList()).isEmpty();
    }


    @Test
    public void removeLastBasic() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.removeLast(); // [0]

        assertThat(lld1.toList()).containsExactly(0).inOrder();
    }

    @Test
    public void removeLastFromFront() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.removeLast(); // [-2, -1, 0, 1]
        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1).inOrder();
    }

    @Test
    public void removeLastToNull() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.removeLast();  // [-2, -1, 0, 1]
        lld1.removeLast(); // [-2, -1, 0]
        lld1.removeLast(); // [-2, -1]
        lld1.removeLast(); // [-2]
        lld1.removeLast(); // []

        assertThat(lld1.toList()).isEmpty();
    }

    @Test
    public void addToCapacity() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.addLast(0);    // [-2, -1, 0, 1, 2, 0]
        lld1.addLast(1);    // [-2, -1, 0, 1, 2, 0, 1]
        lld1.addFirst(-1);  // [-1, -2, -1, 0, 1, 2, 0, 1]

        assertThat(lld1.toList()).containsExactly(-1, -2, -1, 0, 1, 2, 0, 1).inOrder();
    }

    @Test
    public void expandForAddFirst() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.addLast(0);    // [-2, -1, 0, 1, 2, 0]
        lld1.addLast(1);    // [-2, -1, 0, 1, 2, 0, 1]
        lld1.addFirst(-1);  // [-1, -2, -1, 0, 1, 2, 0, 1]
        lld1.addFirst(0);  // [0, -1, -2, -1, 0, 1, 2, 0, 1]

        assertThat(lld1.toList()).containsExactly(0, -1, -2, -1, 0, 1, 2, 0, 1).inOrder();
    }

    @Test
    public void expandForAddLast() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(0);    // [0]
        lld1.addLast(1);    // [0, 1]
        lld1.addFirst(-1);  // [-1, 0, 1]
        lld1.addLast(2);    // [-1, 0, 1, 2]
        lld1.addFirst(-2);  // [-2, -1, 0, 1, 2]
        lld1.addLast(0);    // [-2, -1, 0, 1, 2, 0]
        lld1.addLast(1);    // [-2, -1, 0, 1, 2, 0, 1]
        lld1.addFirst(-1);  // [-1, -2, -1, 0, 1, 2, 0, 1]
        lld1.addLast(0);  // [-1, -2, -1, 0, 1, 2, 0, 1, 0]

        assertThat(lld1.toList()).containsExactly(-1, -2, -1, 0, 1, 2, 0, 1, 0).inOrder();
    }

    @Test
    public void shrinkTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 17; i++) {
            lld1.addLast(i);
        }
        for (int i = 0; i < 14; i++) {
            lld1.removeLast();
        }
        //Test through visualizer
    }


}

