package rest;

import entities.House;
import entities.User;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;

public class HouseResourceTest extends ResourceTestEnvironment {
    private final String BASE_URL = "/houses/";

    @Test
    public void getAllHousesTest() {
        House house = createAndPersistHouse();
        User admin = createAndPersistAdmin();
        login(admin);

        given()
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$", hasItem(hasEntry("address", house.getAddress())));
    }

    @Test
    public void getAllHousesWhenUnauthenticatedTest() {
        given()
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    public void getAllHousesWhenUnauthorizedTest() {
        User user = createAndPersistUser();
        login(user);

        given()
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED_401.getStatusCode());
    }
}
