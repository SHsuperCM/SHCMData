package shcm.shsupercm.data.framework;

import shcm.shsupercm.data.data.DataRegistry;
import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.utils.DataStringConversion;
import shcm.shsupercm.data.utils.Equality;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Simple data structure resembling a Map(and backed by a {@link HashMap}). <br>
 * Note that the key types are limited! See {@link #keyType} for more details.
 *
 * @param <K> key type.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DataKeyedBlock<K> {
    /**
     * Actual contents in runtime storage.
     */
    protected HashMap<K, Object> values = new HashMap<>();

    /**
     * The data's type of keys.<br>
     * To simplify things, this has a limited number of types.<br>
     * The types are:
     * <pre>
     * • For a String-keyed block, see {@link DataBlock}.
     * • All Object-wrapped primitives.
     *     * Meaning that instead of boolean.class, use Boolean.class.
     * </pre>
     */
    private final Class<K> keyType;

    /**
     * Constructs a DataKeyedBlock.
     *
     * @param keyType See {@link #keyType}.
     */
    public DataKeyedBlock(Class<K> keyType) {
        if(keyType.equals(String.class))
            throw new NonDataBlockStringKeyedBlockException();
        this.keyType = keyType;
    }

    protected DataKeyedBlock() {
        keyType = null;
    }

    /**
     * Checks if a value is assigned to the specified key.
     * @param key the search term for the associated value.
     * @return indication if value exists
     */
    public boolean exists(K key) {
        return this.values.get(key) != null;
    }

    /**
     * From {@link java.util.Map#put(Object, Object)}:<br>
     * Associates the specified value with the specified key in this map (optional operation). If the map previously contained a mapping for the key, the old value is replaced by the specified value.
     * @return this instance
     */
    public DataKeyedBlock<K> set(K key, Object value) {
        if (value == null)
            this.remove(key);
        else
            this.values.put(key, value);
        return this;
    }

    /**
     * From {@link java.util.Map#get(Object)}:<br>
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped.
     */
    public Object get(K key) {
        return this.values.get(key);
    }

    /**
     * From {@link java.util.Map#remove(Object)}:<br>
     * Removes the mapping for a key from this map if it is present.
     * @param key the key whose associated value should be removed.
     */
    public void remove(K key) {
        this.values.remove(key);
    }

    /**
     * See {@link #get(Object)}.<br>
     * Counting on the user of the method to know that the key is associated with a DataBlock-type value.
     */
    public DataBlock getBlock(K key) {
        return (DataBlock) get(key);
    }

    /**
     * Checks if the specified key is of the correct type to fit in this block.
     * @param key the key to check.
     * @return Indication if {@code} key is of the correct type.
     */
    public boolean isCorrectKeyType(Object key) {
        return this.keyType.isInstance(key);
    }

    /**
     * From {@link java.util.Map#keySet()}:<br>
     * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa.
     * @return a set view of the keys contained in this map
     */
    public Set<K> getKeys() {
        return this.values.keySet();
    }

    /**
     * Writes this block into the data stream.
     * @param dataOut the stream to write this block into.
     */
    protected void write(DataOutput dataOut) throws IOException, DataSerializer.UnknownDataTypeException {
        dataOut.writeByte(DataSerializer.getByteForType(this.keyType));
        dataOut.writeInt(this.values.size());
        if(!this.values.isEmpty()) {
            for(K key : this.values.keySet()) {
                DataSerializer.write(dataOut, key);
                DataSerializer.write(dataOut, this.values.get(key));
            }
        }
    }

    /**
     * Read a new DataKeyedBlock from the data stream.
     * @param dataIn the stream to read from.
     * @return the read DataKeyedBlock.
     */
    @SuppressWarnings("unchecked")
    protected static DataKeyedBlock read(DataInput dataIn) throws IOException, DataSerializer.UnexpectedByteException {
        byte keyType = dataIn.readByte();
        int size = dataIn.readInt();
        DataKeyedBlock dataKeyedBlock = createKeyTypeBasedOnByte(keyType);
        for(int i = 0; i < size; i++) {
            Object key = DataSerializer.read(dataIn);
            if (!dataKeyedBlock.isCorrectKeyType(key))
                throw new DataSerializer.UnexpectedByteException();
            Object value = DataSerializer.read(dataIn);
            dataKeyedBlock.set(key, value);
        }

        return dataKeyedBlock;
    }

    /**
     * Creates a new DataKeyedBlock with the type that's associated with the type id.
     * @param type the type id.
     * @return a new DataKeyedBlock.
     * @throws DataSerializer.UnexpectedByteException if the type cannot be made into a key type for DataKeyedBlock.
     */
    private static DataKeyedBlock createKeyTypeBasedOnByte(byte type) throws DataSerializer.UnexpectedByteException {
        switch (type) {
            case 1:
                return new DataBlock();
            case 2:
                return new DataKeyedBlock<>(Boolean.class);
            case 3:
                return new DataKeyedBlock<>(Byte.class);
            case 4:
                return new DataKeyedBlock<>(Short.class);
            case 5:
                return new DataKeyedBlock<>(Character.class);
            case 6:
                return new DataKeyedBlock<>(Integer.class);
            case 7:
                return new DataKeyedBlock<>(Float.class);
            case 8:
                return new DataKeyedBlock<>(Long.class);
            case 9:
                return new DataKeyedBlock<>(Double.class);

        }

        throw new DataSerializer.UnexpectedByteException();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append('{');
        boolean comma = false;
        for (K key : values.keySet()) {
            Object value = values.get(key);

            if(comma)
                string.append(',');
            else
                comma = true;

            string.append(DataStringConversion.toString(key));
            string.append(':');
            string.append(DataStringConversion.toString(value.toString()));
        }
        string.append('}');
        return string.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        try {
            DataKeyedBlock<K> other = (DataKeyedBlock<K>)obj;
            if (values.size() != other.values.size())
                return false;

            for (K key : getKeys()) {
                Object value = get(key);
                Object otherValue = other.get(key);
                if(!Equality.areObjectsEqual(value, otherValue))
                    return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * Thrown when constructing a {@link String}-keyed {@link DataKeyedBlock} instead of {@link DataBlock}.
     */
    public static class NonDataBlockStringKeyedBlockException extends RuntimeException {}
}
