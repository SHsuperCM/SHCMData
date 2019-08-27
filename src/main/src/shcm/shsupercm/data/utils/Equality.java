package shcm.shsupercm.data.utils;

import java.util.Arrays;

/**
 * Equality was made by SHsuperCM and is under no license.
 * Feel free to use anywhere and without credit.
 */
public class Equality {
    /**
     * Checks if 2 objects are equal and takes array comparison as well as primitive array comparison into account.
     * @param a first object
     * @param b second object
     * @return a is equal to b
     */
    public static boolean areObjectsEqual(Object a, Object b) {
        if(a == b || a.equals(b))
            return true;

        if(a.getClass().isArray() && a.getClass().equals(b.getClass())) {
            if(a instanceof boolean[])
                return Arrays.equals((boolean[])a, (boolean[])b);
            else if(a instanceof byte[])
                return Arrays.equals((byte[])a, (byte[])b);
            else if(a instanceof short[])
                return Arrays.equals((short[])a, (short[])b);
            else if(a instanceof char[])
                return Arrays.equals((char[])a, (char[])b);
            else if(a instanceof int[])
                return Arrays.equals((int[])a, (int[])b);
            else if(a instanceof float[])
                return Arrays.equals((float[])a, (float[])b);
            else if(a instanceof long[])
                return Arrays.equals((long[])a, (long[])b);
            else if(a instanceof double[])
                return Arrays.equals((double[])a, (double[])b);
            else
                return Arrays.equals((Object[])a, (Object[])b);
        }

        return false;
    }
}
