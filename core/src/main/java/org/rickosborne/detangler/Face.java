package org.rickosborne.detangler;

public class Face {
    public final int[] indices;
    private String _string;
    private Integer _hashCode;

    public Face(final int[] indices) {
        this.indices = indices;
    }

    public boolean canSplit() {
        return this.indices.length > 3;
    }

    public Two<Face> randomSplit() {
        final int count = indices.length;
        final int leftIndex = Rand.index(count);
        final int rightOffset = Rand.index(count - 3);
        final int rightIndex = (leftIndex + 2 + rightOffset) % count;
        final int lowIndex = Math.min(leftIndex, rightIndex);
        final int highIndex = Math.max(leftIndex, rightIndex);
        return split(lowIndex, highIndex);
    }

    public Two<Face> split(final int lowIndex, final int highIndex) {
        assert lowIndex < highIndex;
        final int width = highIndex - lowIndex;
        final int count = indices.length;
        assert width > 1 && width < count;
        final int outerCount = count + 1 - width;
        final int innerCount = width + 1;
        final int[] inner = new int[innerCount];
        System.arraycopy(indices,  lowIndex, inner, 0, innerCount);
        final int[] outer = new int[outerCount];
        System.arraycopy(indices, 0, outer, 0, lowIndex + 1);
        System.arraycopy(indices, highIndex, outer, lowIndex + 1, count - highIndex);
        return Two.of(new Face(inner), new Face(outer));
    }

    public int size() {
        return indices.length;
    }

    @Override
    public String toString() {
        if (_string == null) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indices.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(indices[i]);
            }
            _string = sb.toString();
        }
        return _string;
    }

    @Override
    public int hashCode() {
        if (_hashCode == null) {
            _hashCode = toString().hashCode();
        }
        return _hashCode;
    }

    public static Face ofSize(final int size) {
        assert size > 0;
        final int[] indices = new int[size];
        for (int i = 0; i < size; i++) {
            indices[i] = i;
        }
        return new Face(indices);
    }
}
