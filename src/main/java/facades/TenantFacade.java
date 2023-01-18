package facades;

import entities.Tenant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public List<Tenant> getAllTenants() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Tenant> query = em.createQuery("SELECT t FROM Tenant t", Tenant.class);
        List<Tenant> tenants = query.getResultList();
        em.close();

        return tenants;
    }
}