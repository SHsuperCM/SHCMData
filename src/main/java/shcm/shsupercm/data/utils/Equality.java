package shcm.shsupercm.data.utils;

import java.util.Arrays;

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
}
