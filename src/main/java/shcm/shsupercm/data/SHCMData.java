package shcm.shsupercm.data;

/**
 * SHCMData was made by SHsuperCM and is under the MIT License.
 * This class represents the whole library.
 */
public class SHCMData {
    /**
     * The comparable representation of the library's internal version.
     */
    public static final int versionID = 12;
    /**
     * The comparable representation of the data's format's version.<br>
     * This refers to how the data itself is stored and not any of the API functions and methods.
     */
    public static final int formatID = 4;
    /**
     * The displayable representation of the library's version.
     */
    public static final String versionName = "1." + formatID + "-" + versionID;
}
