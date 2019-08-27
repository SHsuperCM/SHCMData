package shcm.shsupercm.data.framework;

import shcm.shsupercm.data.utils.Equality;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DataKeyedBlock<K> {
    protected HashMap<K, Object> values = new HashMap<>();
    private final K exampleKeyType;

    public DataKeyedBlock(K exampleKeyType) {
        assert !(exampleKeyType instanceof String);
        this.exampleKeyType = exampleKeyType;
    }

    //todo create custom no-constructor provider

    //todo hold key type as class instead of example key

    public DataKeyedBlock<K> set(K key, Object value) {
        values.put(key, value);
        return this;
    }

    public Object get(K key) {
        return values.get(key);
    }

    public boolean isCorrectKeyType(Object key) {
        return this.exampleKeyType.getClass().isInstance(key);
    }

    public Set<K> getKeys() {
        return values.keySet();
    }

    protected void write(DataOutput dataOut) throws IOException, DataSerializer.UnknownDataTypeException {
        dataOut.writeByte(DataSerializer.getByteForType(exampleKeyType));
        dataOut.writeInt(values.size());
        if(!values.isEmpty()) {
            for(K key : values.keySet()) {
                DataSerializer.write(dataOut, key);
                DataSerializer.write(dataOut, values.get(key));
            }
        }
    }

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

    private static DataKeyedBlock createKeyTypeBasedOnByte(byte type) throws DataSerializer.UnexpectedByteException {
        switch (type) {
            case -1:
                return new DataKeyedBlock<DataKeyedBlock>(new DataKeyedBlock<>(null));
            case 1:
                return new DataBlock();
            case 2:
                return new DataKeyedBlock<>(false);
            case 3:
                return new DataKeyedBlock<>((byte)0);
            case 4:
                return new DataKeyedBlock<>((short)0);
            case 5:
                return new DataKeyedBlock<>('\u0000');
            case 6:
                return new DataKeyedBlock<>(0);
            case 7:
                return new DataKeyedBlock<>(0F);
            case 8:
                return new DataKeyedBlock<>(0L);
            case 9:
                return new DataKeyedBlock<>(0D);

            case -3:
                return new DataKeyedBlock<>(new DataBlock[0]);
            case -2:
                return new DataKeyedBlock<>(new DataKeyedBlock[0]);
            case 11:
                return new DataKeyedBlock<>(new String[0]);
            case 12:
                return new DataKeyedBlock<>(new boolean[0]);
            case 13:
                return new DataKeyedBlock<>(new byte[0]);
            case 14:
                return new DataKeyedBlock<>(new short[0]);
            case 15:
                return new DataKeyedBlock<>(new char[0]);
            case 16:
                return new DataKeyedBlock<>(new int[0]);
            case 17:
                return new DataKeyedBlock<>(new float[0]);
            case 18:
                return new DataKeyedBlock<>(new long[0]);
            case 19:
                return new DataKeyedBlock<>(new double[0]);
        }

        throw new DataSerializer.UnexpectedByteException();
    }

    public DataBlock getBlock(K key) {
        return (DataBlock) get(key);
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
}
