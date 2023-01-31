package org.jupiterhub.pipu.chat.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DirectoryResourceTest {

    @Test
    public void testExtensionsIdEndpoint() {
        given()
                .when().get("/directory/1")
                .then()
                .statusCode(200)
                .body("$.size()", is(1));
    }
}