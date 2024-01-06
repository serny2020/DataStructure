import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class LinkedListDequeTest {
    
    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();

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
}
