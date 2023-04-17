package de.toph.util;

/**
 * 
 * @author Tophhhhh
 *
 * String Utils
 */
public class StringUtil {

    /**
     * @param string
     * @return isvalid
     * 
     * true if the string is valid
     */
    public static boolean isVaild(String string) {
	return string != null && !string.isEmpty();
    }
    
    /**
     * if value is null return the other value
     * 
     * @param value
     * @param otherCase
     * @return value or default value
     */
    public static String emtpyStringIfNull(String value) {
	return value != null ? value : "";
    }
}
