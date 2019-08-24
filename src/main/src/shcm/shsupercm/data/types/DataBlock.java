package shcm.shsupercm.data.types;

import shcm.shsupercm.data.framework.DataSerializer;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Default {@link DataKeyedBlock} with {@link String} key support.
 */
public class DataBlock extends DataKeyedBlock<String> {
    public DataBlock() {
        super(null);
    }

    @Override
    public void write(DataOutput dataOut) throws IOException, DataSerializer.UnknownDataTypeException {
        dataOut.writeByte(1);
        if(!values.isEmpty()) {
            for(String key : values.keySet()) {
                dataOut.writeBoolean(true);
                DataSerializer.write(dataOut, key);
                DataSerializer.write(dataOut, values.get(key));
            }
        }
        dataOut.writeBoolean(false);
    }

    @Override
    public void set(String key, Object value) {
        assert key != null && !key.isEmpty();
        super.set(key, value);
    }

    @Override
    public Object get(String key) {
        assert key != null && !key.isEmpty();
        return super.get(key);
    }
}
