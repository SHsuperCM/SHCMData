package shcm.shsupercm.data.utils;

/**
 * A simple generic value wrapper that defaults to another if null.
 * @param <T> value type
 */
public class DefaultNonNull<T> {
    /**
     * The value to default to in case {@link #value} is null.
     */
    private final T defaultValue;
    /**
     * The wrapped value.
     */
    private T value = null;

    /**
     * Constructs a {@link DefaultNonNull} wrapper around the provided default value.
     *
     * @param defaultValue the value to default to in case {@link #value} is null.
     */
    public DefaultNonNull(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Since you cannot check the {@link #get()} against null.
     * @return Indication of {@link #value} being null.
     */
    public boolean isValueNull() {
        return value == null;
    }

    /**
     * Sets the value.
     *
     * @param value {@link #value}
     */
    public DefaultNonNull set(T value) {
        this.value = value;
        return this;
    }

    public T getDefault() {
        return this.defaultValue;
    }

    /**
     * @return either the value or the default in case value is null.
     */
    public T get() {
        return isValueNull() ? getDefault() : this.value;
    }
}
