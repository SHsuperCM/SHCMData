package shcm.shsupercm.testing;

import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.data.annotations.Data;

@Data({0, 15, -25})
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
}
