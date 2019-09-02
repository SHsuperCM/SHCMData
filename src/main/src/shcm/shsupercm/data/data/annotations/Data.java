package shcm.shsupercm.data.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Data {
    byte[] value();

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface Ignore {}

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface Name {
        String value();
    }
}
