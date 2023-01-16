package rest;

import dtos.PopulateDTO;
import security.errorhandling.AuthenticationException;
import utils.Populator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("utilities")
public class UtilityResource extends Resource{

    @Path("populate")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response populate(String populateJson) throws AuthenticationException {
        PopulateDTO populateDTO = GSON.fromJson(populateJson, PopulateDTO.class);

        //Change secret to something secret in your own project!!!!!!!
        if (!populateDTO.getSecret().equals("exam3sem")) {
            throw new AuthenticationException("Wrong secret");
        }
        Populator.populateWithInitialUsers(EMF);
        return Response.ok().build();
    }
}
