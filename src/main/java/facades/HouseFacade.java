package facades;

import entities.House;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

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
            throw new EntityNotFoundException("House does not exist in database");
        }

        return house;
    }

    public List<House> getAllHouses() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<House> query = em.createQuery("SELECT h FROM House h", House.class);
        List<House> houses = query.getResultList();
        em.close();

        return houses;
    }
}
