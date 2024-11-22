package org.rickosborne.detangler;

import java.util.function.Consumer;

public class Holder<T> {
    private T value;

    public void setValue(final T value) {
        this.value = value;
    }

    public void withValue(final Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }
}
