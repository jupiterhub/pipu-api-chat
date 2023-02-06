package org.jupiterhub.pipu.chat.util;

import org.jupiterhub.pipu.chat.exception.GenerateKeyException;

import java.util.Arrays;

public class KeyGenUtil {

    public static String generateKey(String input1, String input2) {
        if (input1 == null || input2 == null) {
            throw new GenerateKeyException("Both parameters are required to generate key.");
        }

        String[] strings = {input1, input2};
        Arrays.sort(strings);

        return strings[0] + "-" + strings[1];
    }
}
