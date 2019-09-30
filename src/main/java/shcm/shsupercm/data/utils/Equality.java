package shcm.shsupercm.data.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * Equality was made by SHsuperCM and is under the WTFPL-2.0 licence.( https://tldrlegal.com/license/do-wtf-you-want-to-public-license-v2-(wtfpl-2.0) )<br>
 * Feel free to use anywhere and without credit.
 */
public class Equality {
    /**
     * Checks if 2 objects are equal and takes array comparison as well as primitive array comparison into account.
     * @param a first object
     * @param b second object
     * @return a is equal to b
     */
    @SuppressWarnings("ConstantConditions")
    public static boolean areObjectsEqual(Object a, Object b) {
        if(a == b || a.equals(b))
            return true;

        if(a.getClass().isArray() && b.getClass().isArray()) {
            if(a instanceof boolean[] && b instanceof boolean[])
                return Arrays.equals((boolean[])a, (boolean[])b);
            else if(a instanceof byte[] && b instanceof byte[])
                return Arrays.equals((byte[])a, (byte[])b);
            else if(a instanceof short[] && b instanceof short[])
                return Arrays.equals((short[])a, (short[])b);
            else if(a instanceof char[] && b instanceof char[])
                return Arrays.equals((char[])a, (char[])b);
            else if(a instanceof int[] && b instanceof int[])
                return Arrays.equals((int[])a, (int[])b);
            else if(a instanceof float[] && b instanceof float[])
                return Arrays.equals((float[])a, (float[])b);
            else if(a instanceof long[] && b instanceof long[])
                return Arrays.equals((long[])a, (long[])b);
            else if(a instanceof double[] && b instanceof double[])
                return Arrays.equals((double[])a, (double[])b);
            else
                return Arrays.equals((Object[])a, (Object[])b);
        }

        return false;
    }

    /**
     * Will try to cast arrays/collections to primitive arrays.
     *
     * @param originalValues the array or collection to cast.
     * @return the new cast array.
     */
    public static Object arrayPrimitiveStandardsCast(Object originalValues) throws ClassCastException {
        if(originalValues instanceof Collection)
            return arrayPrimitiveStandardsCast(((Collection) originalValues).toArray());

        if(originalValues.getClass().isArray()) {
            if(originalValues instanceof boolean[] || originalValues instanceof byte[] || originalValues instanceof short[] || originalValues instanceof char[] || originalValues instanceof int[] || originalValues instanceof float[] || originalValues instanceof long[] || originalValues instanceof double[])
                return originalValues;

            if(originalValues instanceof Boolean[]) {
                boolean[] values = new boolean[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (boolean)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Byte[]) {
                byte[] values = new byte[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (byte)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Short[]) {
                short[] values = new short[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (short)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Character[]) {
                char[] values = new char[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (char)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Integer[]) {
                int[] values = new int[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (int)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Float[]) {
                float[] values = new float[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (float)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Long[]) {
                long[] values = new long[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (long)Array.get(originalValues, i);
                return values;
            } else if(originalValues instanceof Double[]) {
                double[] values = new double[Array.getLength(originalValues)];
                for(int i = 0; i < values.length; i++)
                    values[i] = (double)Array.get(originalValues, i);
                return values;
            } else if(Array.getLength(originalValues) >= 1) {
                if (Array.get(originalValues, 0) instanceof Boolean) {
                    boolean[] values = new boolean[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (boolean) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Byte) {
                    byte[] values = new byte[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (byte) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Short) {
                    short[] values = new short[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (short) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Character) {
                    char[] values = new char[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (char) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Integer) {
                    int[] values = new int[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (int) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Float) {
                    float[] values = new float[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (float) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Long) {
                    long[] values = new long[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (long) Array.get(originalValues, i);
                    return values;
                } else if (Array.get(originalValues, 0) instanceof Double) {
                    double[] values = new double[Array.getLength(originalValues)];
                    for (int i = 0; i < values.length; i++)
                        values[i] = (double) Array.get(originalValues, i);
                    return values;
                }
            }
        }

        return originalValues;
    }
}
