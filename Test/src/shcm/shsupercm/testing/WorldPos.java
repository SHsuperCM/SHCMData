package shcm.shsupercm.testing;

import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.data.annotations.Data;
import shcm.shsupercm.data.framework.DataBlock;

@Data({0,15,-25})
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

    /*@Override
    public DataBlock write(DataBlock dataBlock) {
        dataBlock
            .set("x", posX)
            .set("y", posY)
            .set("z", posZ);
        return dataBlock;
    }

    /*@Override
    public IData read(DataBlock dataBlock) {
        posX = (int) dataBlock.get("x");
        posY = (int) dataBlock.get("y");
        posZ = (int) dataBlock.get("z");
        return this;
    }*/

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WorldPos && ((WorldPos) obj).posX == this.posX && ((WorldPos) obj).posY == this.posY && ((WorldPos) obj).posZ == this.posZ;
    }
}
