package facades;

import TestEnvironment.TestEnvironment;
import entities.House;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HouseFacadeTest extends TestEnvironment {
    private static HouseFacade facade;

    public HouseFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = HouseFacade.getFacade(emf);
    }

    @Test
    public void getHouseByIdTest() {
        House expected = createAndPersistHouse();

        House actual = facade.getHouseById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void getHouseByNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class,()-> facade.getHouseById(nonExistingId));
    }

    @Test
    public void getAllHousesTest() {
        House houseA = createAndPersistHouse();
        House houseB = createAndPersistHouse();
        int expected = 2;

        List<House> actual = facade.getAllHouses();

        assertEquals(expected, actual.size());
        assertTrue(actual.contains(houseA));
        assertTrue(actual.contains(houseB));
    }

    @Test
    public void getAllHousesWithNothingTest() {
        assertDoesNotThrow(()-> facade.getAllHouses());
    }
}
