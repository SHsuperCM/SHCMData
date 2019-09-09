package shcm.shsupercm.data.data;

import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.data.annotations.Data;

/**
 * Makes the implementing class representable as a DataBlock.<br>
 * The implementing class must be registered either automatically from the @{@link Data} annotation or through {@link DataRegistry#register(DataRegistry.Builder)}.
 */
public interface IData {
    /**
     * Do not implement if registering the class using the @{@link Data} annotation!<br><br>
     * The unique id of this IData type is a byte array that provides identification for {@link DataRegistry}.<br>
     * DataRegistry will require the registry of this class to happen as soon as possible(most importantly, before operations with it are done).<br>
     * Convention for this id is that is uses 3 bytes to describe the IData type and therefore allows for almost 17 million registered types.<br>
     * If more/less types are necessary, there is nothing stopping the implementer of this from providing a bigger/smaller byte array.<br>
     * Something important to note is that DataRegistry expects the id to be consistent for its class, upon removal of a data type it is best practice to not use the empty id that is left behind.
     * @return this IData type's unique id.
     */
    default byte[] dataTypeUID() {
        return DataAnnotationRegistry.getID(getClass());
    }

    /**
     * If registering the class using the @{@link Data} annotation; there is no need to implement this method(although still possible).<br>
     * If not implementing, automatic field handling will try to write into the object.(See @{@link Data})<br><br>
     * Writes this object into a DataBlock(overwriting existing data within the DataBlock).
     * @param dataBlock the DataBlock to write on top of.
     * @return the DataBlock representation of the object.
     */
    default DataBlock write(DataBlock dataBlock) {
        return DataAnnotationRegistry.write(dataBlock, this);
    }

    /**
     * If registering the class using the @{@link Data} annotation; there is no need to implement this method(although still possible).<br>
     * If not implementing, automatic field handling will try to read into the object.(See @{@link Data})<br><br>
     * Reads a DataBlock into this object(overwriting existing data within the object).
     * @param dataBlock the DataBlock representation of the object.
     * @return this IData
     */
    default IData read(DataBlock dataBlock) {
        return DataAnnotationRegistry.read(dataBlock);
    }
}

