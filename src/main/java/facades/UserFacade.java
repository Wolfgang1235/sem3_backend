package facades;

import entities.Entity;

import javax.persistence.*;

import entities.Rental;
import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidDateException;
import errorhandling.InvalidUsernameException;
import errorhandling.UniqueException;
import security.errorhandling.AuthenticationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserFacade {
    private static EntityManagerFactory emf;
    private static UserFacade instance;

    public static final int MINIMUM_AGE = 13;
    public static final int MAXIMUM_AGE = 120;

    public static final int MINIMUM_USERNAME_LENGTH = 3;
    public static final int MAXIMUM_USERNAME_LENGTH = 20;

    private UserFacade() {
    }

    public static UserFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username =:username ",User.class);
            query.setParameter("username",username);
            try {
                user = query.getSingleResult();
            } catch (NoResultException e) {
                throw new AuthenticationException("Invalid user name or password");
            }

            if (!user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(User user) throws IllegalAgeException, InvalidUsernameException, UniqueException {
        EntityManager em = emf.createEntityManager();

        if(user.getAge() < MINIMUM_AGE || user.getAge() > MAXIMUM_AGE) {
            throw new IllegalAgeException(user.getAge());
        }

        if(user.getUsername() == null || user.getUsername().equals("")) {
            throw new InvalidUsernameException("Username cannot be null or an empty string");
        }

        if(user.getUsername().length() < MINIMUM_USERNAME_LENGTH || user.getUsername().length() > MAXIMUM_USERNAME_LENGTH) {
            throw new InvalidUsernameException("Username length should be between " + MINIMUM_USERNAME_LENGTH + " and " +
                    + MAXIMUM_USERNAME_LENGTH+ " characters");
        }

        TypedQuery<Long> query = em.createQuery("SELECT count(u) FROM User u WHERE u.username =:username", Long.class);
        query.setParameter("username", user.getUsername());
        if (query.getSingleResult() > 0) {
            throw new UniqueException("Username already in use");
        }

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }

    public void updateUser(User user) throws UniqueException, InvalidUsernameException, IllegalAgeException {
        EntityManager em = emf.createEntityManager();

        validateUser(user);

        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (RollbackException exception){
            throw new UniqueException(exception.getMessage());
        } finally {
            em.close();
        }
    }

    private void validateUser(User user) throws IllegalAgeException, InvalidUsernameException {
        if(user.getUsername() == null || user.getUsername().equals("")) {
            throw new InvalidUsernameException("Username cannot be null or an empty string");
        }

        if(user.getUsername().length() < MINIMUM_USERNAME_LENGTH ||
                user.getUsername().length() > MAXIMUM_USERNAME_LENGTH) {
            throw new InvalidUsernameException("Username length should be between "
                    + MINIMUM_USERNAME_LENGTH + " and "
                    + MAXIMUM_USERNAME_LENGTH+ " characters");
        }

        if(user.getAge() < MINIMUM_AGE || user.getAge() > MAXIMUM_AGE) {
            throw new IllegalAgeException(user.getAge());
        }
    }

    public User getUserById(int id) {
        EntityManager em = emf.createEntityManager();

        User user = em.find(User.class,id);
        em.close();

        if (user == null) {
            throw new EntityNotFoundException("User with id: "+id+" does not exist in database");
        }

        return user;
    }

    public List<User> getAllUsers() {
        EntityManager em = emf.createEntityManager();

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u",User.class);
        List<User> allUsers = query.getResultList();
        em.close();

        return allUsers;
    }

    public List<Rental> getRentalsByUserId(int id) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Rental> query = em.createQuery("SELECT r FROM Rental r " +
                "JOIN r.tenants t " +
                "JOIN t.user u " +
                "WHERE u.id=:user_id", Rental.class);
        query.setParameter("user_id", id);
        List<Rental> rentals = query.getResultList();
        em.close();

        if (rentals.size() == 0) {
            throw new EntityNotFoundException("User with id: "+id+" does not exist in database");
        }

        return rentals;
    }

    public Rental createRental(Rental rental) throws InvalidDateException {
        EntityManager em = emf.createEntityManager();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        dateFormat.setLenient(false);

        try {
            Date startDate = dateFormat.parse(rental.getStartDate());
            Date endDate = dateFormat.parse(rental.getEndDate());

            if (startDate.after(endDate) || startDate.equals(endDate)) {
                throw new InvalidDateException("Start date is equals to or after End date");
            }

            em.getTransaction().begin();
            em.persist(rental);
            em.getTransaction().commit();

        } catch (ParseException exception) {
            throw new InvalidDateException("The date format is not valid");

        } finally {
            em.close();
        }

        return rental;
    }

    public void updateRental(Rental rental) throws InvalidDateException {
        EntityManager em = emf.createEntityManager();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        dateFormat.setLenient(false);

        if (rental == null) {
            throw new EntityNotFoundException("The entity is null");
        }

        try {
            Date startDate = dateFormat.parse(rental.getStartDate());
            Date endDate = dateFormat.parse(rental.getEndDate());

            if (startDate.after(endDate) || startDate.equals(endDate)) {
                throw new InvalidDateException("Start date is equals to or after End date");
            }

            em.getTransaction().begin();
            em.merge(rental);
            em.getTransaction().commit();

        } catch (ParseException exception) {
            throw new InvalidDateException("The date format is not valid");

        } finally {
            em.close();
        }
    }

    public void deleteUser(Integer id) {
        EntityManager em = emf.createEntityManager();

        User user = em.find(User.class,id);

        if (user == null) {
            throw new EntityNotFoundException("Entity doesn't exist");
        }
        try {
            deleteEntity(user,em);

        } catch (NoResultException exception) {
            throw new EntityNotFoundException("User with id: "+id+" does not exist");
        }
    }

    public void deleteRental(int id) {
        EntityManager em = emf.createEntityManager();

        Rental rental = em.find(Rental.class, id);

        if (rental == null) {
            throw new EntityNotFoundException("Rental with id: "+id+" does not exist in database");
        }

        deleteEntity(rental, em);
    }

    private void deleteEntity(Entity entity, EntityManager em) {
        try {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}