package facades;

import entities.Tenant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

public class TenantFacade {
    private static EntityManagerFactory emf;
    private static TenantFacade instance;

    private TenantFacade() {
    }

    public static TenantFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TenantFacade();
        }
        return instance;
    }

    public Tenant getTenantById(int id) {
        EntityManager em = emf.createEntityManager();

        Tenant tenant = em.find(Tenant.class, id);
        em.close();

        if (tenant == null) {
            throw new EntityNotFoundException("Tenant with id: "+id+" does not exist in database");
        }

        return tenant;
    }
}
