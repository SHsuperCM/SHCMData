package shcm.shsupercm.data.framework;

/**
 * Makes the implementing class representable as a DataBlock.<br>
 */
public interface IData {
    /**
     * The unique id of this IData type is a byte array that provides identification for {@link DataRegistry}.<br>
     * DataRegistry will require the registry of this class to happen as soon as possible(most importantly, before operations with it are done).<br>
     * Convention for this id is that is uses 3 bytes to describe the IData type and therefore allows for almost 17 million registered types.<br>
     * If more/less types are necessary, there is nothing stopping the implementer of this from providing a bigger/smaller byte array.<br>
     * Something important to note is that DataRegistry expects the id to be consistent for its class, upon removal of a data type it is best practice to not use the empty id that is left behind.
     * @return this IData's unique id.
     */
    byte[] dataTypeUID();

    /**
     * Writes this object into a DataBlock(overwriting existing data within the DataBlock).
     * @param dataBlock the DataBlock to write on top of.
     * @return the DataBlock representation of the object.
     */
    DataBlock write(DataBlock dataBlock);

    /**
     * Reads a DataBlock into this object(overwriting existing data within the object).
     * @param dataBlock the DataBlock representation of the object.
     * @return this IData
     */
    IData read(DataBlock dataBlock);
}

