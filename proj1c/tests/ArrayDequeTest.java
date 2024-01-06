import deque.ArrayDeque;
import deque.Deque;
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
    public void iterableTest() {
        Deque<String> ald1 = new ArrayDeque<>();

        ald1.addLast("front");
        ald1.addLast("middle");
        ald1.addLast("back");
        for (String s : ald1) {
            System.out.println(s);
        }
        assertThat(ald1).containsExactly("front", "middle", "back");
    }

    @Test
    public void testEqualDeques() {
        Deque<String> ald1 = new ArrayDeque<>();
        Deque<String> ald2 = new ArrayDeque<>();

        ald1.addLast("front");
        ald1.addLast("middle");
        ald1.addLast("back");

        ald2.addLast("front");
        ald2.addLast("middle");
        ald2.addLast("back");

        assertThat(ald1).isEqualTo(ald2);
    }

    @Test
    public void toStringTest() {
        Deque<String> ald1 = new ArrayDeque<>();

        ald1.addLast("front");
        ald1.addLast("middle");
        ald1.addLast("back");

        System.out.println(ald1);
    }
    

}
