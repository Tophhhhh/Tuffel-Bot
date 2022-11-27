package de.toph.util;

/**
 * 
 * @author Toph
 *
 *         String Utils
 */
public class StringUtil {

    /**
     * @param string
     * @return isvalid
     * 
     *         true if the string is valid
     */
    public static boolean isVaild(String string) {
	return string != null && !string.isEmpty();
    }
}
