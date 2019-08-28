import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.IData;

public class CustomStorageClassThing implements IData {
    public int posX;
    public int posY;
    public int posZ;


    @Override
    public byte[] dataTypeUID() {
        return new byte[]{0, 15, 37};
    }

    @Override
    public DataBlock write(DataBlock dataBlock) {
        return null;
    }

    @Override
    public IData read(DataBlock dataBlock) {
        return null;
    }
}
