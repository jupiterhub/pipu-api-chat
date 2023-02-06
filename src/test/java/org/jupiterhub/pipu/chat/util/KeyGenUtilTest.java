package org.jupiterhub.pipu.chat.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jupiterhub.pipu.chat.exception.GenerateKeyException;
import org.wildfly.common.Assert;

import static org.junit.jupiter.api.Assertions.*;

class KeyGenUtilTest {


    @Test
    void sameOrderRegardlessOfInput() {
        // Given
        String user1 = "jupiter";
        String user2 = "mike";

        // When
        String hash1 = KeyGenUtil.generateKey(user1, user2);
        System.out.println(hash1);
        String hash2 = KeyGenUtil.generateKey(user2, user1);
        System.out.println(hash2);

        // Then
        assertEquals("jupiter-mike", hash1);
        assertEquals("jupiter-mike", hash2);
    }
    @Test
    void throwExceptionIfParamIsNull() {
        // Given / When
        GenerateKeyException thrownFirstMissing = assertThrows(GenerateKeyException.class, () -> {
            KeyGenUtil.generateKey(null, "jup");
        });
        GenerateKeyException thrownSecondMissing = assertThrows(GenerateKeyException.class, () -> {
            KeyGenUtil.generateKey("jup", null);
        });
        GenerateKeyException thrownBothMissing = assertThrows(GenerateKeyException.class, () -> {
            KeyGenUtil.generateKey( null, null);
        });

        // Then
        assertEquals("Both parameters are required to generate key.", thrownFirstMissing.getMessage());
        assertEquals("Both parameters are required to generate key.", thrownSecondMissing.getMessage());
        assertEquals("Both parameters are required to generate key.", thrownBothMissing.getMessage());
    }
}