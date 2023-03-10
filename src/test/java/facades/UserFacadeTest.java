package facades;

import TestEnvironment.TestEnvironment;
import entities.House;
import entities.Rental;
import entities.Tenant;
import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidDateException;
import errorhandling.InvalidUsernameException;

import errorhandling.UniqueException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest extends TestEnvironment {
    private static UserFacade facade;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = UserFacade.getFacade(emf);
    }

    @Test
    public void createUserTest() throws Exception {
        User user = createUser();

        assertNull(user.getId());

        user = facade.createUser(user);

        assertNotNull(user.getId());
    }

    @Test
    public void createUserWithAgeBelowMinimumTest() {
        User user = createUser();
        user.setAge(12);
        assertThrows(IllegalAgeException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithAgeAboveMaximumTest() {
        User user = createUser();
        user.setAge(121);
        assertThrows(IllegalAgeException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithExactlyTheMinimumAgeTest() {
        User user = createUser();
        user.setAge(13);
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithExactlyTheMaximumAgeTest() {
        User user = createUser();
        user.setAge(120);
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithNullUsernameTest() {
        User user = createUser();
        user.setUsername(null);
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithEmptyUsernameTest() {
        User user = createUser();
        user.setUsername("");
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthBelowMinimumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("??")); //two random characters
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthAboveMaximumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("?????????????????????"));
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthExactlyMinimumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("???"));
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthExactlyMaximumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("????????????????????"));
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameThatAlreadyExistTest() {
        User userA = createAndPersistUser();
        User userB = createUser();
        userB.setUsername(userA.getUsername());

        assertThrows(UniqueException.class,
                () -> facade.createUser(userB));
    }

    @Test
    public void createUserWhenUserIsNullTest() {
        assertThrows(NullPointerException.class, () -> facade.createUser(null));
    }

    @Test
    public void updateAgeTest() throws Exception {
        User user = createAndPersistUser();
        user.setAge(faker.number().numberBetween(18,80));

        facade.updateUser(user);

        assertDatabaseHasEntityWith(user,"age",user.getAge());
    }

    @Test
    public void updateUsernameWhenItAlreadyIsInUseTest() {
        User userA = createAndPersistUser();
        User userB = createAndPersistUser();
        userA.setUsername(userB.getUsername());

        assertThrows(UniqueException.class,()-> facade.updateUser(userA));
    }

    @Test
    public void updateUsernameWhenEmptyTest() {
        User user = createAndPersistUser();
        user.setUsername("");

        assertThrows(InvalidUsernameException.class,()-> facade.updateUser(user));
    }

    @Test
    public void updateUsernameWhenNullTest() {
        User user = createAndPersistUser();
        user.setUsername("");

        assertThrows(InvalidUsernameException.class,()-> facade.updateUser(user));
    }

    @Test
    public void updateUsernameWhenUnderMinimumLengthTest() {
        User user = createAndPersistUser();
        user.setUsername(faker.letterify("??"));

        assertThrows(InvalidUsernameException.class,()-> facade.updateUser(user));
    }

    @Test
    public void updateUsernameWhenOverMaximumLengthTest() {
        User user = createAndPersistUser();
        user.setUsername(faker.letterify("?????????????????????"));

        assertThrows(InvalidUsernameException.class,()-> facade.updateUser(user));
    }

    @Test
    public void updateAgeWhenUnderMinimumTest() {
        User user = createAndPersistUser();
        user.setAge(12);

        assertThrows(IllegalAgeException.class,()-> facade.updateUser(user));
    }

    @Test
    public void updateAgeWhenOverMaximumTest() {
        User user = createAndPersistUser();
        user.setAge(121);

        assertThrows(IllegalAgeException.class,()-> facade.updateUser(user));
    }

    @Test
    public void getUserByIdTest() {
        User expected = createAndPersistUser();

        User actual = facade.getUserById(expected.getId());

        assertEquals(expected.getId(),actual.getId());
    }

    @Test
    public void getUserByNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class,()-> facade.getUserById(nonExistingId));
    }

    @Test
    public void getAllUsersTest() {
        User user = createAndPersistUser();

        List<User> actual = facade.getAllUsers();

        assertTrue(actual.contains(user));
    }

    @Test
    public void getRentalsByUserIdTest() {
        Tenant tenant = createAndPersistTenant();
        Rental rentalA = createAndPersistRental();
        Rental rentalB = createAndPersistRental();
        Rental rentalC = createAndPersistRental();
        rentalA.getTenants().add(tenant);
        rentalB.getTenants().add(tenant);
        update(rentalA);
        update(rentalB);
        int expected = 2;

        List<Rental> actual = facade.getRentalsByUserId(tenant.getUser().getId());

        assertEquals(expected, actual.size());
    }

    @Test
    public void getRentalsByNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class, ()-> facade.getRentalsByUserId(nonExistingId));
    }

    @Test
    public void getTenantsByHouseIdTest() {
        Rental rentalA = createAndPersistRental();
        Rental rentalB = createAndPersistRental();
        House house = createAndPersistHouse();
        rentalA.setHouse(house);
        rentalB.setHouse(house);
        update(rentalA);
        update(rentalB);
        int expected = 2;

        List<Tenant> actual = facade.getTenantsByHouseId(house.getId());

        assertEquals(expected, actual.size());
    }

    @Test
    public void getTenantsByNonExistingHouseIdTest() {
        assertThrows(EntityNotFoundException.class, ()-> facade.getTenantsByHouseId(nonExistingId));
    }

    @Test
    public void createRentalTest() throws InvalidDateException {
        Rental rental = createRental();

        Rental actual = facade.createRental(rental);

        assertDatabaseHasEntity(actual, actual.getId());
    }

    @Test
    public void createRentalWithInvalidStartAndEndDateTest() {
        Rental rental = createRental();
        rental.setStartDate(faker.bothify("??##??"));
        rental.setEndDate(faker.bothify("??##??"));

        assertThrows(InvalidDateException.class, ()-> facade.createRental(rental));
    }

    @Test
    public void createRentalWithImpossibleStartAndEndDateTest() {
        Rental rental = createRental();
        rental.setStartDate(faker.bothify("7#/4#/202#"));
        rental.setEndDate(faker.bothify("9#/6#/203#"));

        assertThrows(InvalidDateException.class, ()-> facade.createRental(rental));
    }

    @Test
    public void createRentalWithStartDateWhichExceedsEndDateTest() {
        Rental rental = createRental();
        rental.setStartDate(faker.bothify("24/04/204#"));
        rental.setStartDate(faker.bothify("1#/05/203#"));

        assertThrows(InvalidDateException.class, ()-> facade.createRental(rental));
    }

    @Test
    public void updateRentalStartAndEndDateTest() throws InvalidDateException {
        Rental rental = createAndPersistRental();
        rental.setStartDate(faker.bothify("1#/09/202#"));
        rental.setEndDate(faker.bothify("30/09/203#"));

        facade.updateRental(rental);

        assertDatabaseHasEntityWith(rental, "startDate", rental.getStartDate());
        assertDatabaseHasEntityWith(rental, "endDate", rental.getEndDate());
    }

    @Test
    public void updateRentalWithInvalidStartAndEndDateTest() {
        Rental rental = createAndPersistRental();
        rental.setStartDate(faker.bothify("??##?"));
        rental.setEndDate(faker.bothify("??##?"));

        assertThrows(InvalidDateException.class, ()-> facade.updateRental(rental));
    }

    @Test
    public void updateRentalWithImpossibleStartAndEndDateTest() {
        Rental rental = createAndPersistRental();
        rental.setStartDate(faker.bothify("5#/6#/202#"));
        rental.setEndDate(faker.bothify("6#/4#/203#"));

        assertThrows(InvalidDateException.class, ()-> facade.updateRental(rental));
    }

    @Test
    public void updateRentalWithStartDateWhichExceedsEndDateTest() {
        Rental rental = createAndPersistRental();
        rental.setStartDate(faker.bothify("1#/06/202#"));
        rental.setEndDate(faker.bothify("1#/06/201#"));

        assertThrows(InvalidDateException.class, ()-> facade.updateRental(rental));
    }

    @Test
    public void updateRentalWithNewHouseTest() throws InvalidDateException {
        Rental rental = createAndPersistRental();
        House house = createAndPersistHouse();
        rental.setHouse(house);

        facade.updateRental(rental);

        assertDatabaseHasEntitiesRelated(rental, house);
    }

    @Test
    public void updateNonExistingRentalTest() {
        assertThrows(EntityNotFoundException.class, ()-> facade.updateRental(null));
    }

    @Test
    public void deleteRentalTest() {
        Rental rental = createAndPersistRental();

        facade.deleteRental(rental.getId());

        assertDatabaseDoesNotHaveEntity(rental, rental.getId());
        assertDatabaseHasEntity(rental.getHouse(), rental.getHouse().getId());
    }

    @Test
    public void deleteRentalWithNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class, ()-> facade.deleteRental(nonExistingId));
    }
}
