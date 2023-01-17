package facades;

import TestEnvironment.TestEnvironment;
import entities.Tenant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TenantFacadeTest extends TestEnvironment {
    private static TenantFacade facade;

    public TenantFacadeTest(){
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = TenantFacade.getFacade(emf);
    }

    @Test
    public void getTenantByIdTest() {
        Tenant expected = createAndPersistTenant();

        Tenant actual = facade.getTenantById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    public void getTenantByNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class,()-> facade.getTenantById(nonExistingId));
    }

    @Test
    public void getAllTenantsTest() {
        Tenant tenantA = createAndPersistTenant();
        Tenant tenantB = createAndPersistTenant();
        int expected = 2;

        List<Tenant> actual = facade.getAllTenants();

        assertEquals(expected, actual.size());
        assertTrue(actual.contains(tenantA));
        assertTrue(actual.contains(tenantB));
    }

    @Test
    public void getAllTenantsWithNothingTest() {
        assertDoesNotThrow(()-> facade.getAllTenants());
    }
}
