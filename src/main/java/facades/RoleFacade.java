package facades;

import entities.Role;

import javax.persistence.*;

public class RoleFacade {
    private static EntityManagerFactory emf;
    private static RoleFacade instance;

    public static RoleFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RoleFacade();
        }
        return instance;
    }

    public Role getRoleByRole(String roleString) {
        EntityManager em = emf.createEntityManager();
        Role roleFound;
        try {
            TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.role =:role", Role.class);
            query.setParameter("role",roleString);
            roleFound = query.getSingleResult();
        } catch (NoResultException e){
            throw new EntityNotFoundException("No such Role exist");
        } finally {
            em.close();
        }
            return roleFound;
    }
}
