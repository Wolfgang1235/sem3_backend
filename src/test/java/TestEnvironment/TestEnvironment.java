package TestEnvironment;

import com.github.javafaker.Faker;
import entities.*;
import entities.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class TestEnvironment {
    protected int nonExistingId;
    protected static Faker faker = Faker.instance(new Locale("da-DK"));
    protected static Role role;
    protected static EntityManagerFactory emf;

    protected static final String password = "test123";

    @BeforeEach
    void setup() {
        nonExistingId = faker.random().nextInt(-100, 0);

        resetDatabase();
    }

    private void resetDatabase() {
        truncateDatabase();
        populateDatabase();
    }

    private void truncateDatabase() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Rental").executeUpdate();
            em.createQuery("DELETE FROM House").executeUpdate();
            em.createQuery("DELETE FROM Tenant").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private void populateDatabase() {
        role = createRole();
        role.setRole("user");
        persist(role);
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
    }

    protected Entity persist(Entity entity) {
        return runInTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    protected Entity update(Entity entity) {
            return runInTransaction(em -> em.merge(entity));
    }


    //Tager en lambda funktion som parameter
    private Entity runInTransaction(Transactionable transactionable) {
        EntityManager em = emf.createEntityManager();
        Entity entity;
        try {
            em.getTransaction().begin();
            //Kører lambda funktionen
            entity = transactionable.transact(em);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return entity;
    }

    protected User createAndPersistUser() {
        User user = createUser();
        return (User) persist(user);
    }

    protected User createUser() {
        try {
            User user = new User(
                    faker.letterify("?????"),
                    password,
                    faker.number().numberBetween(13, 120)
            );
            user.addRole(role);
            return user;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return null;
    }

    protected Role createAndPersistRole() {
        Role role = createRole();
        return (Role) persist(role);
    }

    protected Role createRole() {
        return new Role(faker.letterify("????"));
    }

    protected Tenant createAndPersistTenant() {
        Tenant tenant = createTenant();
        return (Tenant) persist(tenant);
    }

    protected Tenant createTenant() {
        User user = createAndPersistUser();
        return new Tenant(
                faker.name().firstName(),
                faker.number().numberBetween(10000000,99999999),
                faker.job().title(),
                user);
    }

    protected House createAndPersistHouse() {
        House house = createHouse();
        return (House) persist(house);
    }

    protected House createHouse() {
        return new House(
                faker.address().streetAddress(),
                faker.address().city(),
                faker.number().numberBetween(1,5));
    }

    protected Rental createRental() {
        List<Tenant> tenants = new ArrayList<>();
        tenants.add(createAndPersistTenant());
        return new Rental(
                faker.bothify("1#/0#/200#"),
                faker.bothify("1#/0#/201#"),
                faker.number().randomDigit(),
                faker.number().randomDigit(),
                faker.name().firstName(),
                createAndPersistHouse(),
                tenants);
    }

    protected void assertDatabaseHasEntity(Entity entity, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            entity = em.find(entity.getClass(), id);
            assertNotNull(entity, "Entity: " + entity.getClass()+" with id: " + id + " does not exist..");
        } finally {
            em.close();
        }
    }

    protected void assertDatabaseDoesNotHaveEntity(Entity entity, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Entity foundEntity = em.find(entity.getClass(), id);
            assertNull(foundEntity, "Entity: " + entity.getClass()+" with id: " + id + " does exist!");
        } finally {
            em.close();
        }
    }

    protected void assertDatabaseHasEntityWith(Entity persistedEntity, String property, Object value) {
        assertDatabaseHasEntity(persistedEntity,persistedEntity.getId());

        assertDatabaseHasPropertyAndValue(persistedEntity,property,value);
    }

    protected void assertDatabaseHasPropertyAndValue(Entity persistedEntity, String property, Object value) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Entity> query = em.createQuery(
                "SELECT e FROM " + persistedEntity.getClass().getSimpleName()
                        + " e WHERE e." + property + " = :value " +
                        "AND e.id =:id",Entity.class);

        query.setParameter("value",value);
        query.setParameter("id",persistedEntity.getId());

        Entity entity = null;
        try {
            entity = query.getSingleResult();
        } catch (Exception e) {
            //
        }finally {
            em.close();
        }

        assertNotNull(entity, persistedEntity.getClass().getSimpleName()+" does not have "+
                property+" with value "+value+" in the database");
    }

//
//    protected void assertDatabaseHasEntityWith(Entity persistedEntity, String property, int value) {
//        assertDatabaseHasEntity(persistedEntity,persistedEntity.getId());
//
//        assertDatabaseHasPropertyAndValue(persistedEntity,property,value);
//    }
//
//    protected void assertDatabaseHasPropertyAndValue(Entity persistedEntity, String property, int value) {
//        EntityManager em = emf.createEntityManager();
//
//        TypedQuery<Entity> query = em.createQuery(
//                "SELECT e FROM " + persistedEntity.getClass().getSimpleName()
//                        + " e WHERE e." + property + " = :value " +
//                        "AND e.id =:id",Entity.class);
//
//        query.setParameter("value",value);
//        query.setParameter("id",persistedEntity.getId());
//
//        Entity entity = null;
//        try {
//            entity = query.getSingleResult();
//        } catch (Exception e) {
//            //
//        }finally {
//            em.close();
//        }
//
//        assertNotNull(entity, persistedEntity.getClass().getSimpleName()+" does not have "+
//                property+" with value "+value+" in the database");
//    }

    protected void assertDatabaseHasEntitiesRelated(Entity entity, Entity related) {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT count(entity) FROM " + entity.getClass().getSimpleName()
                +"  entity WHERE entity."+related.getClass().getSimpleName().toLowerCase()+".id = " + related.getId());

        Long amount = (Long) query.getSingleResult();

        assertNotEquals(0,amount);
    }

    protected void assertDatabaseDoesNotHaveEntitiesRelated(Entity entity, Entity notRelated) {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("SELECT count(entity) FROM " + entity.getClass().getSimpleName()
                +"  entity WHERE entity."+notRelated.getClass().getSimpleName().toLowerCase()+".id = " + notRelated.getId());

        Long amount = (Long) query.getSingleResult();

        assertEquals(0,amount);
    }
}
