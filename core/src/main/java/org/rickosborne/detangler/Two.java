package org.rickosborne.detangler;

public class Two<T> {
    public final T left;
    public final T right;

    private Two(final T left, final T right) {
        this.left = left;
        this.right = right;
    }

    public static <T> Two<T> of(final T left, final T right) {
        return new Two<>(left, right);
    }
}
