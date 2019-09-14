package shcm.shsupercm.data.utils;

import shcm.shsupercm.data.data.DataRegistry;
import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.framework.DataBlock;

import java.io.StringReader;

/**
 * Converts data to and from a string representation.
 */
public class DataStringConversion {
    /**
     * Formats a supported object into string.
     * @param object data to convert to string.
     * @return the string representation of the data.
     */
    public static String toString(Object object) {
        if(object == null)
            return "";

        if(object instanceof IData)
            return toString(DataRegistry.write(new DataBlock(), (IData) object));

        String string = object.toString();

        if(object.getClass().isArray()) {
            StringBuilder sb = new StringBuilder("[");

            boolean comma = false;

            if(object instanceof boolean[]) {
                for(boolean o : (boolean[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof byte[]) {
                for(byte o : (byte[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof short[]) {
                for(short o : (short[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof char[]) {
                for(char o : (char[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof int[]) {
                for(int o : (int[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof float[]) {
                for(float o : (float[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof long[]) {
                for(long o : (long[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else if(object instanceof double[]) {
                for(double o : (double[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            } else {
                for(Object o : (Object[])object) {
                    if(comma)
                        sb.append(',');
                    else
                        comma = true;

                    sb.append(toString(o));
                }
            }

            sb.append(']');
            return sb.toString();
        }

        if(object instanceof String)
            return '\"' + object.toString() + '\"';
        if(object instanceof Boolean)
            return object.toString();
        if(object instanceof Byte)
            return string + 'b';
        if(object instanceof Short)
            return string + 's';
        if(object instanceof Character)
            return '\'' + object.toString() + '\'';
        if(object instanceof Integer)
            return string + 'i';
        if(object instanceof Float)
            return string + 'f';
        if(object instanceof Long)
            return string + 'l';
        if(object instanceof Double)
            return string + 'd';

        return string;
    }

    //For reading first non escaped string
    //"(?:[^"\\]|\\.)*"

    //todo split into more specific exceptions
    public static class StringFormatException extends RuntimeException {}
}
