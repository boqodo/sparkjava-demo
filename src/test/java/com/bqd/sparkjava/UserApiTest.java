package com.bqd.sparkjava;

import org.junit.Test;

import static io.restassured.RestAssured.when;

public class UserApiTest {


    @Test
    public final void test() {
        when().get("api/users")
                .then().statusCode(200);
    }
    @Test
    public final void testRoute(){

    }
}
