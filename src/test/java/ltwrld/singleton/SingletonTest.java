package ltwrld.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


public class SingletonTest {
    @Test
    @DisplayName("Constructor should be private to enforce singleton pattern")
    void constructorShouldBePrivate() throws NoSuchMethodException {
        var constructor = Singleton.class.getDeclaredConstructor();
        assertTrue(
                java.lang.reflect.Modifier.isPrivate(
                        constructor.getModifiers()
                )
        );
    }

    @Test
    @DisplayName("Class Singleton returns the same instance")
    void classSingletonReturnSameInstance() {
        Singleton first = Singleton.getInstance();
        Singleton second = Singleton.getInstance();

        assertSame(first, second);
    }

    @Test
    @DisplayName("Enum Singleton returns the same instance")
    void enumSingletonReturnSameInstance() {
        EnumSingleton first = EnumSingleton.INSTANCE;
        EnumSingleton second = EnumSingleton.INSTANCE;

        assertSame(first, second);
    }

    @Test
    @DisplayName("Class Singleton returns the same instance across multiple threads")
    void classSingletonIsSharedAcrossThreads() throws Exception {
        assertSingletonIsThreadSafe(Singleton::getInstance);
    }

    @Test
    @DisplayName("Enum Singleton returns the same instance across multiple threads")
    void enumSingletonIsSharedAcrossThreads() throws Exception {
        assertSingletonIsThreadSafe(() -> EnumSingleton.INSTANCE);
    }


    private <T> void assertSingletonIsThreadSafe(Callable<T> supplier) throws Exception {
        try (var executor = Executors.newFixedThreadPool(10)) {
            var tasks = Collections.nCopies(100, supplier);
            var futures = executor.invokeAll(tasks);

            T expected = supplier.call();

            for (var future : futures) {
                assertSame(expected, future.get());
            }
        }
    }
}
