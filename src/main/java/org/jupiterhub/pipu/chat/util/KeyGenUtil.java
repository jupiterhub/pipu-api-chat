package org.jupiterhub.pipu.chat.util;

import org.jupiterhub.pipu.chat.exception.ChatIdCreationException;
import org.jupiterhub.pipu.chat.exception.GenerateKeyException;

import java.util.Arrays;
import java.util.List;

public class KeyGenUtil {

    /**
     * Same key regardless of order
     */
    public static String commutativeKey(String input1, String input2) {
        if (input1 == null || input2 == null) {
            throw new GenerateKeyException("Both parameters are required to generate key.");
        }

        String[] strings = {input1, input2};
        Arrays.sort(strings);

        return strings[0] + "-" + strings[1];
    }

    /**
     * Creates same chat id regardless of the order
     */
    public static String createChatId(List<String> people) {
        if (people == null || people.size() < 2) {
            throw new ChatIdCreationException("At least 2 people must be provided to generate a key");
        }

        return  KeyGenUtil.commutativeKey(people.get(0), people.get(1));
    }
}
