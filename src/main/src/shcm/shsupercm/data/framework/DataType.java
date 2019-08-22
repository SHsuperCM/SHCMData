package shcm.shsupercm.data.framework;

/**
 * Makes the implementing class a data type which can be serialized in SHCMData context.
 */
public abstract class DataType {

    public abstract byte[] serialize();

    public abstract void deserialize(byte[] bytes);

    public abstract String toString();
}
