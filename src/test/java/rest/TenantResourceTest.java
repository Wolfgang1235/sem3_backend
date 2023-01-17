package rest;

import entities.Tenant;
import entities.User;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;

public class TenantResourceTest extends ResourceTestEnvironment {
    private final String BASE_URL = "/tenants/";

    @Test
    public void getAllTenantsTest() {
        Tenant tenant = createAndPersistTenant();
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
                .body("$", hasItem(hasEntry("name", tenant.getName())));
    }

    @Test
    public void getAllTenantsWhenUnauthenticatedTest() {
        given()
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    public void getAllTenantsWhenUnauthorizedTest() {
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
