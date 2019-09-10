package shcm.shsupercm.data.data.annotations;

import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.data.generation.DataAnnotationProcessor;
import shcm.shsupercm.data.data.DataAnnotationRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implementors of {@link IData} annotated with this will be automatically registered and managed by {@link DataAnnotationProcessor} and {@link DataAnnotationRegistry}.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Data {
    /**
     * The id of the data type(commonly 3 bytes).
     */
    byte[] value();

    /**
     * Tells the processor to ignore this field when writing/reading a data block.
     */
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface Ignore {}

    /**
     * Tells the processor to name this field something else while in a data block.
     */
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface Name {
        /**
         * The name of the field in the data block representation.
         */
        String value();
    }

    /**
     * Defines a different access approach to the field.<br>
     * If not present for a private field, default getters and setters would be assumed to exist and be used.
     */
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface Access {
        /**
         * Defines the getter for the field.<br>
         * If null; will assume default getter is present.
         */
        String getter();

        /**
         * Defines the setter for the field.<br>
         * If null; will assume default setter is present.
         */
        String setter();
    }

    /**
     * Defines that the field contains an enum type.
     */
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface Enum {
        /**
         * Defines how the enum value should be stored.
         */
        Method value() default Method.NAME;

        /**
         * Methods for how the enum value should be stored.
         */
        enum Method {
            /**
             * Defines that the enum values should be stored as their ordinal position(integer index).<br>
             * Serializes and deserializes faster as well as weighs less than {@link #NAME}.
             */
            ORDINAL,
            /**
             * Defines that the enum values should be stored as their name(string name).<br>
             * More reliable than {@link #ORDINAL}.
             */
            NAME
        }
    }
}
