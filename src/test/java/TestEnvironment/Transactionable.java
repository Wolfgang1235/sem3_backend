package TestEnvironment;

import entities.Entity;

import javax.persistence.EntityManager;

//Functional interface fordi man så kan give metoden som en lambda funktion
@FunctionalInterface
public interface Transactionable {

    Entity transact(EntityManager em);
}
