package facades;

import TestEnvironment.TestEnvironment;
import entities.Rental;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RentalFacadeTest extends TestEnvironment {
    private static RentalFacade facade;

    public RentalFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = RentalFacade.getFacade(emf);
    }

    @Test
    public void getRentalByIdTest() {
        Rental expected = createAndPersistRental();

        Rental actual = facade.getRentalById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void getRentalByNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class, ()-> facade.getRentalById(nonExistingId));
    }

    @Test
    public void getAllRentalsTest() {
        Rental rentalA = createAndPersistRental();
        Rental rentalB = createAndPersistRental();
        int expected = 2;

        List<Rental> actual = facade.getAllRentals();

        assertEquals(expected, actual.size());
        assertTrue(actual.contains(rentalA));
        assertTrue(actual.contains(rentalB));
    }

    @Test
    public void getAllRentalsWithNothingTest() {
        assertDoesNotThrow(()-> facade.getAllRentals());
    }
}
