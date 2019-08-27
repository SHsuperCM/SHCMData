package shcm.shsupercm.data.framework;

/**
 * Makes the implementing class representable as a DataKeyedBlock.
 * @param <K> the key type
 */
public interface IKeyedData<K> {
    /**
     * Writes this object into a DataKeyedBlock(overwriting existing data within the DataKeyedBlock).
     * @param dataKeyedBlock the DataKeyedBlock to write on top of.
     * @return the DataKeyedBlock representation of the object.
     */
    DataKeyedBlock<K> write(DataKeyedBlock<K> dataKeyedBlock);

    /**
     * Reads a DataKeyedBlock into this object(overwriting existing data within the object).
     * @param dataKeyedBlock the DataKeyedBlock representation of the object.
     */
    void read(DataKeyedBlock<K> dataKeyedBlock);
}
