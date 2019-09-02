package shcm.shsupercm.data.data;

import shcm.shsupercm.data.framework.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class DataAnnotationRegistry {
    private static final Map<Class<? extends IData>, DataTypeHandler> REGISTRY = new HashMap<>();
    private static final Map<byte[], Class<? extends IData>> ID_CLASS_REGISTRY = new HashMap<>();
    private static boolean initialized = false;

    protected static void register(DataTypeHandler<?> handler) {
        REGISTRY.put(handler.getType(), handler);
        ID_CLASS_REGISTRY.put(handler.dataTypeUID(), handler.getType());
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

        public abstract byte[] dataTypeUID();

        public abstract DataBlock write(DataBlock dataBlock);

        public abstract IData read(DataBlock dataBlock);
    }
}
