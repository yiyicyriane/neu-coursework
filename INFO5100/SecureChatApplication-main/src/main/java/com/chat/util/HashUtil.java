package com.chat.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;

// RSA util for client and test
public class HashUtil {
    public static String generateSalt() throws Exception {
        SecureRandom random = new SecureRandom();
            
        // Generate a byte array for the salt
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        
        // Encode the salt as a Base64 string
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String passwordHash(String password, String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}
