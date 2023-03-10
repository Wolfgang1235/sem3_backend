package rest;

import TestEnvironment.TestEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HouseDTO;
import dtos.RentalDTO;
import dtos.UserDTO;
import entities.House;
import entities.Rental;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utils.EMF_Creator;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;

public class ResourceTestEnvironment extends TestEnvironment {
    protected static String securityToken;
    protected static final int SERVER_PORT = 7777;
    protected static final String SERVER_URL = "http://localhost/api";
    private static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    protected static HttpServer httpServer;
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig resourceConfig = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        EMF_Creator.startREST_TestWithDB();

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    protected static void login(User user) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", user.getUsername(), password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
    }

    protected void logout() {
        securityToken = null;
    }

    protected User createAndPersistAdmin() {
        User admin = createAndPersistUser();
        Role adminRole = new Role("admin");
        admin.addRole(adminRole);
        admin = (User) update(admin);
        return admin;
    }

    protected UserDTO createUserDTO() {
        User user = createUser();

        return new UserDTO.Builder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(password)
                .setAge(user.getAge())
                .setRoles(user.getRolesAsStringList())
                .build();
    }

    protected RentalDTO createRentalDTO() {
        Rental rental = createRental();

        return new RentalDTO.Builder()
                .setId(rental.getId())
                .setStartDate(rental.getStartDate())
                .setEndDate(rental.getEndDate())
                .setPriceAnnual(rental.getPriceAnnual())
                .setDeposit(rental.getDeposit())
                .setContactPerson(rental.getContactPerson())
                .setHouseId(rental.getHouse().getId())
                .setTenantIds(rental.getTenantIds())
                .build();
    }

    protected HouseDTO createHouseDTOFromHouse(House house) {
        return new HouseDTO.Builder()
                .setId(house.getId())
                .setAddress(house.getAddress())
                .setCity(house.getCity())
                .setNumberOfRooms(house.getNumberOfRooms())
                .build();
    }

    protected HouseDTO createHouseDTOFromRental(Rental rental) {
        return new HouseDTO.Builder()
                .setId(rental.getHouse().getId())
                .setAddress(rental.getHouse().getAddress())
                .setCity(rental.getHouse().getCity())
                .setNumberOfRooms(rental.getHouse().getNumberOfRooms())
                .build();
    }
}
