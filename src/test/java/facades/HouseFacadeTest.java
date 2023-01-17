package facades;

import TestEnvironment.TestEnvironment;
import entities.House;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
