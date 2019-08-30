import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.data.IData;

public class WorldPos implements IData {
    public int posX;
    public int posY;
    public int posZ;

    public WorldPos() {
    }

    public WorldPos(int posX, int posY, int posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    @Override
    public byte[] dataTypeUID() {
        return new byte[]{0, 15, 37};
    }

    @Override
    public DataBlock write(DataBlock dataBlock) {
        return dataBlock
                .set("x", this.posX)
                .set("y", this.posY)
                .set("z", this.posZ);
    }

    @Override
    public IData read(DataBlock dataBlock) {
        this.posX = (int) dataBlock.get("x");
        this.posY = (int) dataBlock.get("y");
        this.posZ = (int) dataBlock.get("z");
        return this;
    }
}
