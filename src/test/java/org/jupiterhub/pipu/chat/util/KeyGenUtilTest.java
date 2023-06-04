package org.jupiterhub.pipu.chat.util;

import org.junit.jupiter.api.Test;
import org.jupiterhub.pipu.chat.exception.GenerateKeyException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyGenUtilTest {


    @Test
    void sameOrderRegardlessOfInput() {
        // Given
        String user1 = "jupiter";
        String user2 = "mike";

        // When
        String hash1 = KeyGenUtil.commutativeKey(user1, user2);
        String hash2 = KeyGenUtil.commutativeKey(user2, user1);

        // Then
        assertEquals("jupiter-mike", hash1);
        assertEquals("jupiter-mike", hash2);
    }
    @Test
    void throwExceptionIfParamIsNull() {
        // Given / When
        GenerateKeyException thrownFirstMissing = assertThrows(GenerateKeyException.class, () -> KeyGenUtil.commutativeKey(null, "jup"));
        GenerateKeyException thrownSecondMissing = assertThrows(GenerateKeyException.class, () -> KeyGenUtil.commutativeKey("jup", null));
        GenerateKeyException thrownBothMissing = assertThrows(GenerateKeyException.class, () -> KeyGenUtil.commutativeKey( null, null));

        // Then
        assertEquals("Both parameters are required to generate key.", thrownFirstMissing.getMessage());
        assertEquals("Both parameters are required to generate key.", thrownSecondMissing.getMessage());
        assertEquals("Both parameters are required to generate key.", thrownBothMissing.getMessage());
    }

    @Test
    void createChatId() {
        // Given
        String user1 = "jupiter";
        String user2 = "mike";

        // When
        String hash1 = KeyGenUtil.createChatId(List.of(user1, user2));
        String hash2 = KeyGenUtil.createChatId(List.of(user2, user1));

        // Then
        assertEquals("jupiter-mike", hash1);
        assertEquals("jupiter-mike", hash2);
    }
}