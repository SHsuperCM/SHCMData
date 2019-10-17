package shcm.shsupercm.data.utils;

import shcm.shsupercm.data.data.DataRegistry;
import shcm.shsupercm.data.data.IData;
import shcm.shsupercm.data.framework.DataBlock;
import shcm.shsupercm.data.framework.DataKeyedBlock;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

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

        if(object instanceof String) {
            if(string.contains("\"")) {
                StringBuilder sb = new StringBuilder();

                int backslash = 0;
                for(char c : string.toCharArray()) {
                    if(c == '"' && backslash % 2 == 0)
                        sb.append('\\');
                    if(c == '\\')
                        backslash++;
                    else
                        backslash = 0;
                    sb.append(c);
                }

                string = sb.toString();
            }
            return '\"' + string + '\"';
        } if(object instanceof Boolean)
            return string;
        if(object instanceof Byte)
            return string + 'b';
        if(object instanceof Short)
            return string + 's';
        if(object instanceof Character)
            return '\'' + string + '\'';
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
    public static Object fromString(String string) throws StringFormatException {
        return extractFirstValue(string).value;
    }

    //todo look for things that contradict the data format
    //todo unique exception throwing
    /**
     * Reads the string and extracts the first(SHCMData compatible) value it finds.
     * @param substring the string to read from.
     * @return the returned value and some more information.
     */
    private static ReadValue extractFirstValue(String substring) throws StringFormatException {
        if(substring != null && !substring.isEmpty()) {
            char first = substring.charAt(0);
            if(first == ' ' || first == '\n' || first == '\t')
                return extractFirstValue(substring.substring(1));
            int length = 0;
            StringBuilder read = new StringBuilder();
            if(first == ',')
                return new ReadValue(substring.substring(1), SYNTAX.NEXT);
            else if(first == ':')
                return new ReadValue(substring.substring(1), SYNTAX.EXPAND);
            else if(first == ']')
                return new ReadValue(substring.substring(1), SYNTAX.EOA);
            else if(first == '}')
                return new ReadValue(substring.substring(1), SYNTAX.EOB);
            else if(Character.toLowerCase(first) == 'f') {
                if(substring.substring(0, 5).toLowerCase().equals("false"))
                    return new ReadValue(substring.substring(5), false);
            } else if(Character.toLowerCase(first) == 't') {
                if(substring.substring(0, 4).toLowerCase().equals("true"))
                    return new ReadValue(substring.substring(4), true);
            } else if((first >= '0' && first <= '9') || first == '-' || first == '.') {
                read.append(first);
                length++;
                char c = substring.charAt(length);
                while((c >= '0' && c <= '9') || c == '-' || c == '.') {
                    read.append(c);
                    length++;
                    if(length >= substring.length())
                        break;
                    c = substring.charAt(length);
                }

                String readString = read.toString();
                switch (Character.toLowerCase(c)) {
                    case 'b':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(substring.substring(length + 1), Byte.parseByte(readString));
                    case 's':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(substring.substring(length + 1), Short.parseShort(readString));
                    case 'f':
                        return new ReadValue(substring.substring(length + 1), Float.parseFloat(readString));
                    case 'l':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(substring.substring(length + 1), Long.parseLong(readString));
                    case 'd':
                        return new ReadValue(substring.substring(length + 1), Double.parseDouble(readString));
                    case 'i':
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(substring.substring(length + 1), Integer.parseInt(readString));

                    default:
                        if(readString.contains("."))
                            throw new StringFormatException();
                        return new ReadValue(substring.substring(length), Integer.parseInt(readString));
                }
            } else  if(first == '\'') {
                if(substring.charAt(2) == '\'')
                    return new ReadValue(substring.substring(3), substring.charAt(1));
            } else if(first == '"') {
                read.append('"');
                length++;

                int backslashes = 0;

                while(true) {
                    if(length >= substring.length())
                        throw new StringFormatException();
                    char c = substring.charAt(length);
                    length++;
                    if(c == '"' && backslashes % 2 == 0)
                        return new ReadValue(substring.substring(length), read.toString().substring(1));
                    if(c != '\\' || backslashes % 2 == 1)
                        read.append(c);
                    if(c == '\\')
                        backslashes++;
                    else
                        backslashes = 0;
                }
            } else if(first == '{') {
                ReadValue readValue = extractFirstValue(substring.substring(1));
                if(readValue == null)
                    throw new StringFormatException();
                boolean isDataBlock = readValue.value instanceof String;
                DataKeyedBlock dataKeyedBlock = isDataBlock ? new DataBlock() : new DataKeyedBlock<>(readValue.value.getClass());
                Object key;
                while (true) {
                    if(readValue == null || readValue.value instanceof SYNTAX)
                        throw new StringFormatException();
                    key = readValue.value;
                    readValue = extractFirstValue(readValue.remainingString);
                    if(readValue == null || readValue.value != SYNTAX.EXPAND)
                        throw new StringFormatException();
                    readValue = extractFirstValue(readValue.remainingString);
                    if(readValue == null || readValue.value instanceof SYNTAX)
                        throw new StringFormatException();
                    if(!dataKeyedBlock.isCorrectKeyType(key))
                        throw new StringFormatException();
                    //noinspection unchecked
                    dataKeyedBlock.set(key, readValue.value);
                    readValue = extractFirstValue(readValue.remainingString);
                    if(readValue == null || !(readValue.value instanceof SYNTAX))
                        throw new StringFormatException();//if null, could not find EOB
                    if(readValue.value == SYNTAX.EOB) {
                        return new ReadValue(readValue.remainingString, (isDataBlock && dataKeyedBlock.exists(DataRegistry.DATA_ID_IDENTIFIER)) ? DataRegistry.read((DataBlock) dataKeyedBlock) : dataKeyedBlock);
                    } else if(readValue.value == SYNTAX.NEXT) {
                        readValue = extractFirstValue(readValue.remainingString);
                    } else
                        throw new StringFormatException();
                }
            } else if(first == '[') {
                ArrayList<Object> values = new ArrayList<>();
                ReadValue readValue = extractFirstValue(substring.substring(1));

                while (true) {
                    if(readValue == null)
                        throw new StringFormatException();
                    if(readValue.value instanceof SYNTAX) {
                        if(readValue.value == SYNTAX.EOA) {
                            try {
                                Object array = standardizeArray(values);
                                if(array != null && array.getClass().equals(Object[].class))
                                    throw new StringFormatException();
                                return new ReadValue(readValue.remainingString, array);
                            } catch (ClassCastException e) {
                                throw new StringFormatException();
                            }
                        }
                        throw new StringFormatException();
                    }
                    values.add(readValue.value);
                    readValue = extractFirstValue(readValue.remainingString);
                    if(readValue != null && readValue.value == SYNTAX.EOA) {
                        try {
                            Object array = standardizeArray(values);
                            if(array != null && array.getClass().equals(Object[].class))
                                throw new StringFormatException();
                            return new ReadValue(readValue.remainingString, array);
                        } catch (ClassCastException e) {
                            throw new StringFormatException();
                        }
                    }
                    if(readValue != null && readValue.value == SYNTAX.NEXT) {
                        readValue = extractFirstValue(readValue.remainingString);
                    } else
                        throw new StringFormatException();//if null, could not find EOA
                }
            }
        }

        return null;
    }

    private static Object standardizeArray(ArrayList<?> originalValues) {
        if(originalValues.size() >= 1) {
            Object first = originalValues.get(0);
            if(first instanceof DataBlock) {
                try {
                    DataBlock[] array = new DataBlock[originalValues.size()];
                    array[0] = (DataBlock) first;
                    for (int i = 1; i < array.length; i++) {
                        array[i] = (DataBlock) originalValues.get(i);
                    }
                    return array;
                } catch (Exception ignored) {}
            }
            if(first instanceof DataKeyedBlock) {
                try {
                    DataKeyedBlock[] array = new DataKeyedBlock[originalValues.size()];
                    array[0] = (DataKeyedBlock) first;
                    for(int i = 1; i < array.length; i++) {
                        array[i] = (DataKeyedBlock)originalValues.get(i);
                    }
                    return array;
                } catch (Exception ignored) {}
            }
            if(first instanceof IData) {
                try {
                    IData[] array = new IData[originalValues.size()];
                    array[0] = (IData) first;
                    for(int i = 1; i < array.length; i++) {
                        array[i] = (IData)originalValues.get(i);
                    }
                    return array;
                } catch (Exception ignored) {}
            }
        } else {
            return null;
        }

        return Equality.arrayPrimitiveStandardsCast(originalValues);
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
         * Signifying the expansion of a value key's actual value.
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

    private static class ReadValue {

        final String remainingString;
        final Object value;

        ReadValue(String remainingString, Object value) {
            this.remainingString = remainingString;
            this.value = value;
        }
    }

    //todo split into more specific exceptions
    //todo include index of problematic character
    public static class StringFormatException extends RuntimeException {}
}
