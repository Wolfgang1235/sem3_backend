package facades;

import entities.House;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

public class HouseFacade {
    private static EntityManagerFactory emf;
    private static HouseFacade instance;

    private HouseFacade() {
    }

    public static HouseFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HouseFacade();
        }
        return instance;
    }

    public House getHouseById(int id) {
        EntityManager em = emf.createEntityManager();

        House house = em.find(House.class, id);
        em.close();

        if (house == null) {
            throw new EntityNotFoundException("House with id: "+id+" does not exist in database");
        }

        return house;
    }
}
