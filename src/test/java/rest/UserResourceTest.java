package rest;

import dtos.RentalDTO;
import dtos.UserDTO;
import entities.Rental;
import entities.Role;
import entities.User;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserResourceTest extends ResourceTestEnvironment {
    private final String BASE_URL = "/users/";

    @Test
    public void createUserTest() {
        UserDTO userDTO = createUserDTO();

        int id = given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED_201.getStatusCode())
                .contentType(ContentType.JSON)
                .body("username", equalTo(userDTO.getUsername()))
                .body("age", equalTo(userDTO.getAge()))
                .body("id", notNullValue())
                .body("roles",hasSize(1))
                .body("roles",hasItem("user"))
                .extract().path("id");

        assertDatabaseHasEntity(new User(), id);
    }

    @Test
    public void createUserInvalidUsernameTest() {
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setUsername(faker.letterify("??"))
                .build();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", notNullValue());
    }

    @Test
    public void createUserWithUsernameThatAlreadyExistTest() {
        User existingUser = createAndPersistUser();
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setUsername(existingUser.getUsername())
                .build();

        given()
                .header("Content-type",ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", notNullValue());
    }

    @Test
    public void createUserInvalidPasswordTest() {
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setPassword(faker.letterify("??"))
                .build();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message",notNullValue());
    }

    @Test
    public void createUserIllegalAgeTest() {
        UserDTO userDTO = createUserDTO();
        userDTO = new UserDTO.Builder(userDTO)
                .setAge(faker.random().nextInt(121,300))
                .build();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message",notNullValue());
    }

    @Test
    public void getUserTest() {
        User user = createAndPersistUser();

        int id = user.getId();
        login(user);
        given()
            .header("Content-type", ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .get(BASE_URL+"me")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .contentType(ContentType.JSON)
            .body("username", equalTo(user.getUsername()))
            .body("age", equalTo(user.getAge()))
            .body("id", equalTo(id))
            .body("roles",hasSize(1))
            .body("roles",hasItem("user"));
    }

    @Test
    public void getAllUsersTest() {
        User admin = createAndPersistAdmin();
        login(admin);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$",hasItem(hasEntry("username",admin.getUsername())));
    }

    @Test
    public void getAllUsersWhenUnauthenticatedTest() {
        given()
                .header("Content-type", ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    public void getAllUsersWhenUnauthorizedTest() {
        User user = createAndPersistUser();
        login(user);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED_401.getStatusCode());
    }

    @Test
    public void postRentalTest() {
        RentalDTO rentalDTO = createRentalDTO();
        User admin = createAndPersistAdmin();
        login(admin);

        int id = given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(GSON.toJson(rentalDTO))
                .when()
                .post(BASE_URL+"rentals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED_201.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
                .body("contact_person", equalTo(rentalDTO.getContactPerson()))
                .body("house", notNullValue())
                .extract().path("id");

        assertDatabaseHasEntity(new Rental(), id);
    }

    @Test
    public void postRentalWhenAuthenticatedTest() {
        given()
                .header("Content-type", ContentType.JSON)
                .when()
                .post(BASE_URL+"rentals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    public void postRentalWhenUnauthorizedTest() {
        User user = createAndPersistUser();
        login(user);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .post(BASE_URL+"rentals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED_401.getStatusCode());
    }

    @Test
    public void postRentalWithNonExistingHouseTest() {
        RentalDTO rentalDTO = createRentalDTO();
        rentalDTO = new RentalDTO.Builder()
                .setId(rentalDTO.getId())
                .setStartDate(rentalDTO.getStartDate())
                .setEndDate(rentalDTO.getEndDate())
                .setPriceAnnual(rentalDTO.getPriceAnnual())
                .setDeposit(rentalDTO.getDeposit())
                .setContactPerson(rentalDTO.getContactPerson())
                .setHouseId(nonExistingId)
                .setTenantIds(rentalDTO.getTenantIds())
                .build();
        User admin = createAndPersistAdmin();
        login(admin);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(GSON.toJson(rentalDTO))
                .when()
                .post(BASE_URL+"rentals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }

    @Test
    public void postRentalWithNonExistingTenantIdTest() {
        RentalDTO rentalDTO = createRentalDTO();
        ArrayList<Integer> tenantIds = new ArrayList<>();
        tenantIds.add(nonExistingId);
        rentalDTO = new RentalDTO.Builder()
                .setId(rentalDTO.getId())
                .setStartDate(rentalDTO.getStartDate())
                .setEndDate(rentalDTO.getEndDate())
                .setPriceAnnual(rentalDTO.getPriceAnnual())
                .setDeposit(rentalDTO.getDeposit())
                .setContactPerson(rentalDTO.getContactPerson())
                .setHouseId(rentalDTO.getHouseId())
                .setTenantIds(tenantIds)
                .build();
        User admin = createAndPersistAdmin();
        login(admin);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(GSON.toJson(rentalDTO))
                .when()
                .post(BASE_URL+"rentals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }

    @Test
    public void deleteRentalTest() {
        Rental rental = createAndPersistRental();
        User admin = createAndPersistAdmin();
        login(admin);

        given()
                .header("x-access-token", securityToken)
                .when()
                .delete(BASE_URL+"rentals/"+rental.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT_204.getStatusCode());
    }

    @Test
    public void deleteRentalWhenUnauthenticatedTest() {
        Rental rental = createAndPersistRental();

        given()
                .when()
                .delete(BASE_URL+"rentals/"+rental.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    @Test
    public void deleteRentalWhenUnauthorizedTest() {
        Rental rental = createAndPersistRental();
        User user = createAndPersistUser();
        login(user);

        given()
                .header("x-access-token", securityToken)
                .when()
                .delete(BASE_URL+"rentals/"+rental.getId())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED_401.getStatusCode());
    }

    @Test
    public void deleteRentalWithNonExistingIdTest() {
        User admin = createAndPersistAdmin();
        login(admin);

        given()
                .header("x-access-token", securityToken)
                .when()
                .delete(BASE_URL+"rentals/"+nonExistingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT_204.getStatusCode());
    }
}
