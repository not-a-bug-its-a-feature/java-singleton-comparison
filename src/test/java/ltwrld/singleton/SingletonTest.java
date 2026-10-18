package ltwrld.singleton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class SingletonTest {
    @Test
    void returnSameInstance() {
        Singleton first = Singleton.getInstance();
        Singleton second = Singleton.getInstance();

        assertSame(first, second);
    }
}
