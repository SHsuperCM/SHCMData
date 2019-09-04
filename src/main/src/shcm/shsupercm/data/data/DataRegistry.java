package shcm.shsupercm.data.data;

import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.utils.Equality;

import java.util.Arrays;
import java.util.HashMap;

public class DataRegistry {
    public static final String DATA_ID_IDENTIFIER = "shcmdata:data_id";

    /**
     * Actual registry.
     */
    private static final HashMap<UniqueDataId, Builder> REGISTRY = new HashMap<>();

    public static void register(Builder<?> builder) {
        REGISTRY.put(new UniqueDataId(builder.newT().dataTypeUID()), builder);
    }

    protected static void remove(UniqueDataId id) {
        REGISTRY.remove(id);
    }

    public static IData create(byte[] id) {
        return REGISTRY.get(new UniqueDataId(id)).newT();
    }

    public static IData read(byte[] id, DataBlock datablock) {
        return REGISTRY.get(new UniqueDataId(id)).read(datablock);
    }

    public static IData read(DataBlock datablock) {
        return REGISTRY.get(new UniqueDataId((byte[]) datablock.get(DATA_ID_IDENTIFIER))).read(datablock);
    }

    public static DataBlock write(DataBlock dataBlock, IData data) {
        dataBlock.set(DATA_ID_IDENTIFIER, data.dataTypeUID());
        return data.write(dataBlock);
    }

    /**
     * Simple "lambdaifyable" constructor for IData types.
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
