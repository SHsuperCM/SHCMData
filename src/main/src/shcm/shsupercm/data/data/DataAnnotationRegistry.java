package shcm.shsupercm.data.data;

import shcm.shsupercm.data.data.generation.DataAnnotationProcessor;
import shcm.shsupercm.data.framework.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class DataAnnotationRegistry {
    private static final Map<Class<? extends IData>, DataTypeHandler> REGISTRY = new HashMap<>();
    private static final Map<byte[], Class<? extends IData>> ID_CLASS_REGISTRY = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Will register all generated data handler(generated using {@link shcm.shsupercm.data.data.annotations.Data})
     * TODO CLEAN IF FORCED
     */
    public static void init(boolean force) {
        if(!force && initialized)
            return;
        long dataTypeNumber = -1;
        while (true) {
            try {
                register((DataTypeHandler<?>) Class.forName(DataAnnotationProcessor.GENERATED_CLASS_PACKAGE + '.' + DataAnnotationProcessor.GENERATED_CLASS_NAME + ++dataTypeNumber).newInstance());
            } catch (ClassNotFoundException e) {
                break;
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        initialized = true;
    }

    protected static void register(DataTypeHandler<?> handler) {
        REGISTRY.put(handler.getType(), handler);
        ID_CLASS_REGISTRY.put(handler.dataTypeUID(), handler.getType());
        DataRegistry.register(new DataRegistryBuilder<>(handler));
    }

    public static DataBlock write(Class<? extends IData> type, DataBlock dataBlock) {
        return REGISTRY.get(type).write(dataBlock);
    }

    public static IData read(DataBlock dataBlock) {
        return REGISTRY.get(ID_CLASS_REGISTRY.get(dataBlock.get(DataRegistry.DATA_ID_IDENTIFIER))).read(dataBlock);
    }

    public static byte[] getID(Class<? extends IData> type) {
        return REGISTRY.get(type).dataTypeUID();
    }

    public static abstract class DataTypeHandler<T extends IData> {
        public abstract Class<T> getType();

        public abstract T newT();

        public abstract byte[] dataTypeUID();

        public abstract DataBlock write(DataBlock dataBlock);

        public abstract IData read(DataBlock dataBlock);
    }

    private static class DataRegistryBuilder<T extends IData> implements DataRegistry.Builder<T> {
        private final DataTypeHandler handler;

        public DataRegistryBuilder(DataTypeHandler<?> handler) {
            this.handler = handler;
        }

        @Override
        public T newT() {
            return (T) handler.newT();
        }

        @Override
        public T read(DataBlock dataBlock) {
            return (T) handler.read(dataBlock);
        }
    }
}
