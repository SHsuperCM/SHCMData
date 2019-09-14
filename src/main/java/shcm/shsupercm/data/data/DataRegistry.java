package shcm.shsupercm.data.data;

import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.utils.Equality;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Handles the registry for {@link IData} types.
 */
public class DataRegistry {
    public static final String DATA_ID_IDENTIFIER = "shcmdid";

    /**
     * Actual registry.
     */
    private static final HashMap<UniqueDataId, Builder> REGISTRY = new HashMap<>();

    /**
     * Registers the builder for the {@link IData} type.
     * @param builder the builder for the {@link IData} type(made to be used with a lambda method reference to the {@link IData}'s default constructor).
     */
    public static void register(Builder<?> builder) {
        REGISTRY.put(new UniqueDataId(builder.newT().dataTypeUID()), builder);
    }

    /**
     * Removes the builder associated with the given id.
     * @param id the id whose associated builder should be removed.
     */
    protected static void remove(UniqueDataId id) {
        REGISTRY.remove(id);
    }

    /**
     * Constructs a new {@link IData} instance based on the id.
     * @param id the id of the {@link IData} type.
     * @return the new {@link IData} instance.
     */
    public static IData create(byte[] id) {
        DataAnnotationRegistry.init(false);
        return REGISTRY.get(new UniqueDataId(id)).newT();
    }

    /**
     * Constructs a new {@link IData} instance based on the id and reads the data block into it.
     * @param id the id of the {@link IData} type.
     * @param datablock the data block to read into the new {@link IData}.
     * @return the new {@link IData} instance.
     */
    public static IData read(byte[] id, DataBlock datablock) {
        DataAnnotationRegistry.init(false);
        return REGISTRY.get(new UniqueDataId(id)).read(datablock);
    }

    /**
     * Constructs a new {@link IData} instance based on the id that is stored within the data block and reads the data block into it.
     * @param datablock the data block to read into the new {@link IData}.
     * @return the new {@link IData} instance.
     */
    public static Object read(DataBlock datablock) {
        DataAnnotationRegistry.init(false);

        Builder builder = REGISTRY.get(new UniqueDataId((byte[]) datablock.get(DATA_ID_IDENTIFIER)));
        if(builder == null)
            return datablock;
        else
            return builder.read(datablock);
    }

    /**
     * Writes the data into the data block along with its id.
     * @param dataBlock the data block to write to.
     * @param data the data to write into the data block.
     * @return the data block containing the written data({@code dataBlock}).
     */
    public static DataBlock write(DataBlock dataBlock, IData data) {
        DataAnnotationRegistry.init(false);
        dataBlock.set(DATA_ID_IDENTIFIER, data.dataTypeUID());
        return data.write(dataBlock);
    }

    /**
     * Simple "lambdaifyable" constructor for IData types.<br>
     * Made to be used from an {@link IData}'s default constructor method reference.
     * @param <T> the IData type to build
     */
    public interface Builder<T extends IData> {
        /**
         * Simple "lambdaifyable" constructor for T.
         * @return the new T instance.
         */
        T newT();

        /**
         * Constructs an instance of T using {@link #newT()} and reads a DataBlock into it.
         * @param dataBlock DataBlock to read into the new T instance.
         * @return the new T instance.
         */
        @SuppressWarnings("unchecked")
        default T read(DataBlock dataBlock) {
            return (T) newT().read(dataBlock);
        }
    }

    /**
     * A HashMap-safe wrapper for byte array data id.
     */
    protected static final class UniqueDataId {
        /**
         * Actual value.
         */
        final byte[] bytes;

        UniqueDataId(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.bytes);
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj.getClass().equals(UniqueDataId.class) && Equality.areObjectsEqual(this.bytes, ((UniqueDataId)obj).bytes);
        }
    }
}
