package shcm.shsupercm.data.framework;

/**
 * Makes the implementing class representable as a DataBlock.
 */
public interface IData {
    /**
     * Writes this object into a DataBlock(overwriting existing data within the DataBlock).
     * @param dataBlock the DataBlock to write on top of.
     * @return the DataBlock representation of the object.
     */
    DataBlock write(DataBlock dataBlock);

    /**
     * Reads a DataBlock into this object(overwriting existing data within the object).
     * @param dataBlock the DataBlock representation of the object.
     */
    void read(DataBlock dataBlock);
}
