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
    @DisplayName("INSTANCE should be static and final")
    void instanceFieldShouldBeStaticAndFinal() throws NoSuchFieldException {
        var field = Singleton.class.getDeclaredField("INSTANCE");
        var modifiers = field.getModifiers();

        assertTrue(java.lang.reflect.Modifier.isStatic(modifiers));
        assertTrue(java.lang.reflect.Modifier.isFinal(modifiers));
    }

    @Test
    @DisplayName("Enum Singleton returns the same instance")
    void enumSingletonReturnSameInstance() {
        EnumSingleton first = EnumSingleton.INSTANCE;
        EnumSingleton second = EnumSingleton.INSTANCE;

        assertSame(first, second);
    }

    @Test
    @DisplayName("Reflection can create another class singleton instance")
    void reflectionCanCreateAnotherClassSingleton() throws Exception {
        var constructor = Singleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Singleton first = Singleton.getInstance();
        Singleton second = constructor.newInstance();

        assertNotSame(first, second);
    }

    @Test
    @DisplayName("Reflection cannot create another enum singleton instance")
    void reflectionCannotCreateAnotherEnumSingleton() throws Exception {
        var constructor = EnumSingleton.class.getDeclaredConstructor(String.class, int.class);

        assertThrows(
                IllegalAccessException.class,
                () -> constructor.newInstance("OTHER", 1)
        );
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

    @Test
    @DisplayName("Class Singleton loses identity after deserialization")
    void classSingletonLosesIdentityAfterDeserialization() throws Exception {
        Singleton origin = Singleton.getInstance();

        byte[] bytes = serialize(origin);
        Singleton restored = deserialize(bytes);

        assertNotSame(origin, restored);
    }

    @Test
    @DisplayName("Enum Singleton preserves identity after deserialization")
    void enumSingletonPreservesIdentityAfterDeserialization() throws Exception {
        EnumSingleton origin = EnumSingleton.INSTANCE;

        byte[] bytes = serialize(origin);
        EnumSingleton restored = deserialize(bytes);

        assertSame(origin, restored);
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

    private byte[] serialize(Object object) throws java.io.IOException {
        try (var output = new java.io.ByteArrayOutputStream();
             var stream = new java.io.ObjectOutputStream(output)) {
            stream.writeObject(object);
            return output.toByteArray();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] bytes) throws java.io.IOException, ClassNotFoundException {
        try (var input = new java.io.ByteArrayInputStream(bytes);
             var stream = new java.io.ObjectInputStream(input)) {
            return (T) stream.readObject();
        }
    }
}
