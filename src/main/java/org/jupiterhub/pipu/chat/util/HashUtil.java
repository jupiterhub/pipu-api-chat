package org.jupiterhub.pipu.chat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public class HashUtil {

    /**
     * produces same output regardless of order
     */
    public static Integer commutativeHash(String input1, String input2) {
        List.of(input1, input2);
        return Set.of(input1, input2).hashCode();
    }

    public static String generateKey(String parameter1, String parameter2) {
        String input = parameter1 + parameter2;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder keyBuilder = new StringBuilder();
            for (byte b : hash) {
                keyBuilder.append(String.format("%02x", b));
            }
            return keyBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
