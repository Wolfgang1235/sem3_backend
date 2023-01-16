/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import entities.Role;
import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidPasswordException;
import errorhandling.InvalidUsernameException;
import errorhandling.UniqueException;
import facades.UserFacade;

public class Populator {

    public static void main(String[] args) throws InvalidPasswordException, InvalidUsernameException, IllegalAgeException, UniqueException {
        populateWithTestUsers();
    }

    //Fails quietly
    public static void populateWithInitialUsers(EntityManagerFactory emf) {
        try {
            UserFacade userFacade = UserFacade.getFacade(emf);
            Role userRole = persistRole("user",emf);
            Role adminRole = persistRole("admin",emf);

            //Change password to something secret in your own project!!!!!!!
            User user = new User("user", "test123", 33);
            User admin = new User("admin", "test123", 44);

            user.addRole(userRole);
            admin.addRole(adminRole);

            userFacade.createUser(user);
            userFacade.createUser(admin);
        } catch (Exception e) {
            //
        }
    }

    public static void populateWithTestUsers() throws InvalidPasswordException, InvalidUsernameException, IllegalAgeException, UniqueException {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        UserFacade userFacade = UserFacade.getFacade(emf);

        Role userRole = persistRole("user",emf);
        Role adminRole = persistRole("admin",emf);

        User user = new User("user","1234",26);
        User admin = new User("admin","1234",26);
        User userAdmin = new User("user_admin","1234",23);
        User noRole = new User("no_role","1234",36);

        user.addRole(userRole);
        admin.addRole(adminRole);
        userAdmin.addRole(userRole);
        userAdmin.addRole(adminRole);

        userFacade.createUser(user);
        userFacade.createUser(admin);
        userFacade.createUser(userAdmin);
        userFacade.createUser(noRole);

        System.out.println("PW: " + user.getPassword());
        System.out.println("Testing user with OK password: " + user.verifyPassword("1234"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("1235"));
        System.out.println("Created TEST Users");
    }

    private static Role persistRole(String roleString, EntityManagerFactory emf) {
        Role role = new Role(roleString);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(role);
        em.getTransaction().commit();

        return role;
    }
}
