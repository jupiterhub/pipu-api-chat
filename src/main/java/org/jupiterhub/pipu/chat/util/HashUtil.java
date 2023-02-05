package org.jupiterhub.pipu.chat.util;

import java.util.Set;

public class HashUtil {

    /**
     * produces same output regardless of order
     */
    public static Integer commutativeHash(String input1, String input2) {
        return Set.of(input1, input2).hashCode();
    }
}
