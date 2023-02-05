package org.jupiterhub.pipu.chat.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest {

    @Test
    void sameOrderRegardlessOfInput() {
        // Given
        String user1 = "jupi";
        String user2 = "joe";

        // When
        int hash1 = HashUtil.commutativeHash(user1, user2);
        System.out.println(hash1);
        int hash2 = HashUtil.commutativeHash(user2, user1);
        System.out.println(hash2);

        // Then
        assertEquals(hash1, hash2);
    }
}