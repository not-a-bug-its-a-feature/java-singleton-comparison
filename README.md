# Java Singleton Comparison

A small Java project comparing two approaches to implementing the Singleton pattern:

- Class-based Singleton using a private constructor
- Enum-based Singleton

This project uses JUnit tests to verify the characteristics of each implementation, including constructor visibility, reflection, multithreaded access, and serialization/deserialization.

## 1. Development Environment

- Java 25
- Maven
- JUnit 5

## 2. Implementations

### 2.1. Class Singleton

A Singleton implemented using a regular Java class.

```java
public class Singleton {

    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

Key characteristics:

- A `private` constructor prevents ordinary external instantiation.
- A `static` field holds the single instance.
- The `static` `getInstance()` method provides access to that instance.
- The `static final` instance is initialized safely through the JVM's class initialization mechanism.

However, additional considerations may be necessary to preserve singleton identity when using reflection or default Java deserialization.

### 2.2. Enum Singleton

A Singleton implemented using an enum.

```java
public enum EnumSingleton {
    INSTANCE;
}
```

Key characteristics:

- The instance is accessed through the `INSTANCE` enum constant.
- Standard Java reflection cannot create additional enum instances.
- Java's built-in serialization mechanism preserves enum constant identity during deserialization.
- No explicit `getInstance()` or `readResolve()` method is required.

## 3. Test Cases

| Test | What It Verifies |
|---|---|
| `constructorShouldBePrivate` | The class-based Singleton constructor is private. |
| `classSingletonReturnSameInstance` | Repeated calls return the same class-based Singleton instance. |
| `instanceFieldShouldBeStaticAndFinal` | The `INSTANCE` field is both `static` and `final`. |
| `enumSingletonReturnSameInstance` | Repeated access returns the same enum instance. |
| `reflectionCanCreateAnotherClassSingleton` | Reflection can create a separate instance of the class-based Singleton. |
| `reflectionCannotCreateAnotherEnumSingleton` | Reflection cannot create another enum instance. |
| `classSingletonIsSharedAcrossThreads` | Multiple threads receive the same class-based Singleton instance. |
| `enumSingletonIsSharedAcrossThreads` | Multiple threads receive the same enum instance. |
| `classSingletonLosesIdentityAfterDeserialization` | Default deserialization can break the identity of a class-based Singleton. |
| `enumSingletonPreservesIdentityAfterDeserialization` | Enum identity is preserved after deserialization. |

### Notes on Test Coverage

**Multithreading**

The class-based implementation uses JVM class initialization to safely initialize its instance. The tests verify that multiple threads receive the same object.

These tests do not prove that every Singleton implementation is thread-safe. Concurrent access to mutable internal state must be considered separately.

**Reflection**

Reflection can invoke a private constructor in the class-based implementation under ordinary test conditions. Java prevents reflective instantiation of enum constants through its standard reflection API.

**Serialization**

The class-based serialization test demonstrates that default deserialization can create a separate instance. The enum test verifies that the original enum constant is returned.

The class-based implementation implements `Serializable` specifically for this comparison. Serialization support is not required for a Singleton unless the application needs it.

## 4. Running the Tests

Run the following command from the project root:

```bash
mvn test
```

Maven compiles the project and executes the JUnit tests.

## 5. Implementation Comparison

| Aspect | Class Singleton | Enum Singleton |
|---|---|---|
| Access | `Singleton.getInstance()` | `EnumSingleton.INSTANCE` |
| Constructor protection | Requires a private constructor | Managed by the enum mechanism |
| Reflective instantiation | Possible under certain conditions | Prevented by Java |
| Default deserialization identity | Requires additional protection if identity must be preserved | Preserved by Java |
| Implementation complexity | May require additional defensive code | Relatively simple |

## 6. Conclusion

Both approaches can implement the Singleton pattern, but they provide different levels of protection and flexibility.

A class-based Singleton offers direct control over instance creation and initialization. However, reflection and serialization may require additional safeguards.

An enum-based Singleton uses built-in Java language features to provide a concise implementation with strong protection against reflective instantiation and reliable identity preservation during default serialization.

**For most cases without special constraints, an enum-based Singleton is a good default choice.** A class-based implementation may be appropriate when requirements such as lazy initialization or a particular class design justify the additional complexity.

The goal of this project is to verify these differences through tests and understand how they influence implementation choices.
