package shcm.shsupercm.testing;

import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.data.annotations.Data;
import shcm.shsupercm.data.framework.DataBlock;

@Data({0,15,-25})
public class WorldPos implements IData {
    @Data.Name("x")
    public int posX;
    private int posY;
    @Data.Access(getter = "getZ", setter = "setZ")
    private int posZ;
    @Data.Ignore
    public String dimension;

    public WorldPos() {
    }

    public WorldPos(int posX, int posY, int posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getZ() {
        return posZ;
    }

    public void setZ(int posZ) {
        this.posZ = posZ;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WorldPos && ((WorldPos) obj).posX == this.posX && ((WorldPos) obj).posY == this.posY && ((WorldPos) obj).posZ == this.posZ;
    }
}
