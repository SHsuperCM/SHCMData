package shcm.shsupercm.data.utils;

import shcm.shsupercm.data.data.DataRegistry;
import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.framework.DataBlock;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Converts data to and from a string representation.
 */
public class DataStringConversion {
    //todo inject backslashes to strings and remove them upon reading.
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
            //todo inject escape backslashes if not present
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

    /**
     * Reads the string and extracts the first(SHCMData compatible) value it finds.
     * @param string the string to read from.
     * @return the returned value and some more information.
     */
    public static ReadValue extractFirstValue(String string) throws StringFormatException {
        if(string != null && !string.isEmpty()) {
            char first = string.charAt(0);
            if(first == ' ' || first == '\n' || first == '\t')
                return extractFirstValue(string.substring(1));
            int length = 0;
            StringBuilder read = new StringBuilder();
            if(first == ',')
                return new ReadValue(string.substring(1), 1, SYNTAX.NEXT);
            else if(first == ':')
                return new ReadValue(string.substring(1), 1, SYNTAX.EXPAND);
            else if(first == ']')
                return new ReadValue(string.substring(1), 1, SYNTAX.EOA);
            else if(first == '}')
                return new ReadValue(string.substring(1), 1, SYNTAX.EOB);
            else if(Character.toLowerCase(first) == 'f') {
                if(string.substring(0, 5).toLowerCase().equals("false"))
                    return new ReadValue(string.substring(5), 5, false);
            } else if(Character.toLowerCase(first) == 't') {
                if(string.substring(0, 4).toLowerCase().equals("true"))
                    return new ReadValue(string.substring(4), 4, true);
            } else if((first >= '0' && first <= '9') || first == '-' || first == '.') {
                read.append(first);
                length++;
                char c = string.charAt(length);
                while((c >= '0' && c <= '9') || c == '-' || c == '.') {
                    read.append(c);
                    length++;
                    if(length >= string.length())
                        break;
                    c = string.charAt(length);
                }

                String readString = read.toString();
                switch (Character.toLowerCase(c)) {
                    case 'b':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(string.substring(length + 1), length, Byte.parseByte(readString));
                    case 's':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(string.substring(length + 1), length, Short.parseShort(readString));
                    case 'f':
                        return new ReadValue(string.substring(length + 1), length, Float.parseFloat(readString));
                    case 'l':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(string.substring(length + 1), length, Long.parseLong(readString));
                    case 'd':
                        return new ReadValue(string.substring(length + 1), length, Double.parseDouble(readString));
                    case 'i':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(string.substring(length + 1), length, Integer.parseInt(readString));

                    default:
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(string.substring(length), length, Integer.parseInt(readString));
                }
            } else  if(first == '\'') {
                if(string.charAt(2) == '\'')
                    return new ReadValue(string.substring(3), 3, string.charAt(1));
            } else if(first == '"') {
                read.append('"');
                length++;

                int backslashes = 0;

                while(true) {
                    if(length >= string.length())
                        throw new StringFormatException();
                    char c = string.charAt(length);
                    length++;
                    if(c == '"' && backslashes % 2 == 0)
                        return new ReadValue(string.substring(length), length, read.toString().substring(1));
                    //todo remove escape backslashes
                    read.append(c);
                    if(c == '\\')
                        backslashes++;
                    else
                        backslashes = 0;
                }
            } else if(first == '{') {//todo.. do..

            } else if(first == '[') {
                ArrayList<Object> values = new ArrayList<>();
                length++;
                ReadValue readValue = extractFirstValue(string.substring(1));
                if(readValue != null)
                    length += readValue.valueLength;

                while (true) {
                    if(readValue == null)
                        throw new StringFormatException();
                    if(readValue.value instanceof SYNTAX) {
                        if(readValue.value == SYNTAX.EOA) {
                            return new ReadValue(readValue.remainingString, length, Equality.arrayPrimitiveStandardsCast(values));
                        }
                        throw new StringFormatException();
                    }
                    values.add(readValue.value);
                    readValue = extractFirstValue(readValue.remainingString);
                    if(readValue != null)
                        length += readValue.valueLength;
                    if(readValue != null && readValue.value == SYNTAX.EOA)
                        return new ReadValue(readValue.remainingString, length, Equality.arrayPrimitiveStandardsCast(values));
                    if(readValue != null && readValue.value == SYNTAX.NEXT) {
                        readValue = extractFirstValue(readValue.remainingString);
                        if(readValue != null)
                            length += readValue.valueLength;
                    } else
                        throw new StringFormatException();
                }
            }
        }

        return null;
    }

    /**
     * Syntax symbols.
     */
    private enum SYNTAX {
        /**
         * Signifying the move to another block/array entry.
         */
        NEXT,
        /**
         * Signifying the expansion of a value key its actual value.
         */
        EXPAND,
        /**
         * Signifying the end of an array.
         */
        EOA,
        /**
         * Signifying the end of a block.
         */
        EOB
    }

    /**
     *
     */
    public static class ReadValue {

        public final String remainingString;
        public final int valueLength;
        public final Object value;

        public ReadValue(String remainingString, int valueLength, Object value) {
            this.remainingString = remainingString;
            this.valueLength = valueLength;
            this.value = value;
        }
    }

    //todo split into more specific exceptions
    public static class StringFormatException extends RuntimeException {}
}
