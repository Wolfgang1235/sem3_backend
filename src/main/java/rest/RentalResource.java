package rest;

import dtos.HouseDTO;
import dtos.RentalDTO;
import entities.Rental;
import facades.RentalFacade;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("rentals")
public class RentalResource extends Resource {
    private final RentalFacade facade = RentalFacade.getFacade(EMF);

    @GET
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllRentals() {
        List<Rental> rentals = facade.getAllRentals();
        List<RentalDTO> rentalDTOS = new ArrayList<>();
        for (Rental rental : rentals) {
            HouseDTO houseDTO = new HouseDTO.Builder()
                    .setId(rental.getHouse().getId())
                    .setAddress(rental.getHouse().getAddress())
                    .setCity(rental.getHouse().getCity())
                    .setNumberOfRooms(rental.getHouse().getNumberOfRooms())
                    .build();
            rentalDTOS.add(new RentalDTO.Builder()
                    .setId(rental.getId())
                    .setStartDate(rental.getStartDate())
                    .setEndDate(rental.getEndDate())
                    .setPriceAnnual(rental.getPriceAnnual())
                    .setDeposit(rental.getDeposit())
                    .setContactPerson(rental.getContactPerson())
                    .setHouse(houseDTO)
                    .setTenantIds(rental.getTenantIds())
                    .build());
        }
        String rentalDTOsToJson = GSON.toJson(rentalDTOS);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(rentalDTOsToJson).build();
    }
}
