package org.jupiterhub.pipu.chat.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@Disabled
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