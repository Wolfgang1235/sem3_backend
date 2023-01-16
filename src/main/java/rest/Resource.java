package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class Resource {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    @Context
    SecurityContext securityContext;
}
