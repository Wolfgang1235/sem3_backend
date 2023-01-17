package rest;

import dtos.HouseDTO;
import entities.House;
import facades.HouseFacade;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("houses")
public class HouseResource extends Resource {
    private final HouseFacade facade = HouseFacade.getFacade(EMF);

    @GET
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllHouses() {
        List<House> houses = facade.getAllHouses();
        List<HouseDTO> houseDTOS = new ArrayList<>();
        for (House house : houses) {
            houseDTOS.add(new HouseDTO.Builder()
                    .setId(house.getId())
                    .setAddress(house.getAddress())
                    .setCity(house.getCity())
                    .setNumberOfRooms(house.getNumberOfRooms())
                    .build());
        }
        String houseDTOsToJson = GSON.toJson(houseDTOS);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(houseDTOsToJson).build();
    }
}
