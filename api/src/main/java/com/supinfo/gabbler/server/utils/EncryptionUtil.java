package com.supinfo.gabbler.server.utils;

/**
 * @author Alvin Meimoun
 */
public class EncryptionUtil {

    public static String encodeToSHA256(String originalString){
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(originalString);
    }
}
