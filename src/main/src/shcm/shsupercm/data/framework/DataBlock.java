package shcm.shsupercm.data.framework;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Default {@link DataKeyedBlock} with {@link String} key support.
 */
public class DataBlock extends DataKeyedBlock<String> {
    public DataBlock() {}

    @Override
    protected void write(DataOutput dataOut) throws IOException, DataSerializer.UnknownDataTypeException {
        dataOut.writeByte(1);
        dataOut.writeInt(values.size());
        if(!values.isEmpty()) {
            for(String key : values.keySet()) {
                DataSerializer.write(dataOut, key);
                DataSerializer.write(dataOut, values.get(key));
            }
        }
    }

    @Override
    public boolean isCorrectKeyType(Object key) {
        return key instanceof String;
    }

    @Override
    public DataBlock set(String key, Object value) {
        assert key != null && !key.isEmpty();
        super.set(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        assert key != null && !key.isEmpty();
        return super.get(key);
    }
}
