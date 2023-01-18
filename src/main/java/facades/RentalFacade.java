package facades;

import entities.Rental;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public class RentalFacade {
    private static EntityManagerFactory emf;
    private static RentalFacade instance;

    private RentalFacade() {
    }

    public static RentalFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RentalFacade();
        }
        return instance;
    }

    public Rental getRentalById(int id) {
        EntityManager em = emf.createEntityManager();;

        Rental rental = em.find(Rental.class, id);
        em.close();

        if (rental == null) {
            throw new EntityNotFoundException("Rental with id: "+id+" does not exist in database");
        }

        return rental;
    }

    public List<Rental> getAllRentals() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Rental> query = em.createQuery("SELECT r FROM Rental r", Rental.class);
        List<Rental> rentals = query.getResultList();
        em.close();

        return rentals;
    }

}
