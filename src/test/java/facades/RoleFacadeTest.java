package facades;

import TestEnvironment.TestEnvironment;
import entities.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleFacadeTest extends TestEnvironment {
    private static RoleFacade facade;

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = RoleFacade.getFacade(emf);
    }

    @Test
    void getRoleByRoleTest() {
        Role expected = createAndPersistRole();

        Role actual = facade.getRoleByRole(expected.getRole());

        assertEquals(expected.getRole(),actual.getRole());
    }

    @Test
    void getNonExistingRoleByRoleTest() {
        Role role = createRole();

        assertThrows(EntityNotFoundException.class, () -> facade.getRoleByRole(role.getRole()));
    }
}
