package rest;

import dtos.*;
import entities.House;
import entities.Rental;
import entities.Tenant;
import entities.User;
import errorhandling.*;
import facades.*;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("users")
public class UserResource extends Resource {
    private final UserFacade facade = UserFacade.getFacade(EMF);
    private final RoleFacade roleFacade = RoleFacade.getFacade(EMF);
    private final HouseFacade houseFacade = HouseFacade.getFacade(EMF);
    private final TenantFacade tenantFacade = TenantFacade.getFacade(EMF);
    private final RentalFacade rentalFacade = RentalFacade.getFacade(EMF);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(String userFromJson) {
        UserDTO userDTO = GSON.fromJson(userFromJson, UserDTO.class);
        User user;

        try {
            user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getAge());
            user.addRole(roleFacade.getRoleByRole("user"));
            user = facade.createUser(user);
        } catch (UniqueException e) {
             throw new WebApplicationException(e.getMessage(),HttpStatus.CONFLICT_409.getStatusCode());
        }
        catch (InvalidUsernameException | InvalidPasswordException | IllegalAgeException e) {
            throw new BadRequestException(e.getMessage());
        }
        userDTO = buildStandardUserDTO(user);

        String userToJson = GSON.toJson(userDTO);
        return Response.status(HttpStatus.CREATED_201.getStatusCode()).entity(userToJson).build();
    }

    @GET
    @RolesAllowed({"user","admin"})
    @Path("me")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMe() {
        User user;
        int id = Integer.parseInt(securityContext.getUserPrincipal().getName());

        try {
            user = facade.getUserById(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        }
        UserDTO userDTO = buildStandardUserDTO(user);

        String userToJson = GSON.toJson(userDTO);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userToJson).build();
    }

    @GET
    @RolesAllowed("admin")
    @Produces ({MediaType.APPLICATION_JSON})
    public Response getAllUsers() {
        List<User> allUsers = facade.getAllUsers();
        List<UserDTO> allUserDTOs = new ArrayList<>();

        for (User user : allUsers) {
            allUserDTOs.add(new UserDTO.Builder()
                    .setId(user.getId())
                    .setUsername(user.getUsername())
                    .setAge(user.getAge())
                    .setRoles(user.getRolesAsStringList())
                    .build());
        }

        String userDtosToJson = GSON.toJson(allUserDTOs);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userDtosToJson).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateUser(String userFromJson, @PathParam("id") int id) {
        UserDTO userDTO = GSON.fromJson(userFromJson, UserDTO.class);
        User user;

        try {
            user = facade.getUserById(id);

            if (userDTO.getUsername() != null) {
                user.setUsername(userDTO.getUsername());
            }

            if (userDTO.getAge() != null) {
                user.setAge(userDTO.getAge());
            }
            facade.updateUser(user);

        }catch (EntityNotFoundException entityNotFoundException){
            throw new BadRequestException("User does not exist");

        } catch (UniqueException uniqueException) {
            throw new WebApplicationException("Chosen username is already in use",
                    HttpStatus.CONFLICT_409.getStatusCode());

        } catch (IllegalAgeException illegalAgeException) {
            throw new WebApplicationException("You need to be between 18 and 80 years old to use this site",
                    HttpStatus.CONFLICT_409.getStatusCode());

        } catch (InvalidUsernameException invalidUsernameException) {
            throw new WebApplicationException("Your username was either too long or too short, " +
                    "it should be between 3 and 20 characters",
                    HttpStatus.CONFLICT_409.getStatusCode());
        }

        UserDTO updatedUserDTO = buildStandardUserDTO(user);
        return Response.ok().entity(GSON.toJson(updatedUserDTO)).build();
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int id) {
        try {
            facade.deleteUser(id);
        } catch (EntityNotFoundException exception) {

        }
        return Response.noContent().build();
    }

    @GET
    @RolesAllowed("user")
    @Path("user-rentals")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRentalsByUser() {
        int userId = Integer.parseInt(securityContext.getUserPrincipal().getName());
        List<Rental> rentals;

        try {
            rentals = facade.getRentalsByUserId(userId);
        } catch (EntityNotFoundException exception) {
            throw new NotFoundException("No rentals could be found from current user");
        }

        List<RentalDTO> rentalDTOS = new ArrayList<>();
        for (Rental rental : rentals) {
            rentalDTOS.add(new RentalDTO.Builder()
                    .setId(rental.getId())
                    .setStartDate(rental.getStartDate())
                    .setEndDate(rental.getEndDate())
                    .setPriceAnnual(rental.getPriceAnnual())
                    .setDeposit(rental.getDeposit())
                    .setContactPerson(rental.getContactPerson())
                    .setHouseId(rental.getHouse().getId())
                    .build());
        }

        String rentalDTOsToJson = GSON.toJson(rentalDTOS);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(rentalDTOsToJson).build();
    }

    @GET
    @RolesAllowed("admin")
    @Path("tenants/{houseId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTenantsByHouseId(@PathParam("houseId") int houseId) {
        List<Tenant> tenants;

        try {
            tenants = facade.getTenantsByHouseId(houseId);
        } catch (EntityNotFoundException exception) {
            return Response.status(HttpStatus.NO_CONTENT_204.getStatusCode()).build();
        }

        List<TenantDTO> tenantDTOS = new ArrayList<>();
        for (Tenant tenant : tenants) {
            tenantDTOS.add(new TenantDTO.Builder()
                    .setId(tenant.getId())
                    .setName(tenant.getName())
                    .setPhone(tenant.getPhone())
                    .setJob(tenant.getJob())
                    .setUserId(tenant.getUser().getId())
                    .build());
        }

        String tenantDTOsToJson = GSON.toJson(tenantDTOS);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(tenantDTOsToJson).build();
    }

    @POST
    @RolesAllowed("admin")
    @Path("rentals")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response postRental(String rentalFromJson) {
        RentalDTO rentalDTO = GSON.fromJson(rentalFromJson, RentalDTO.class);
        House house;
        List<Tenant> tenants = new ArrayList<>();
        Rental rental;

        try {
            house = houseFacade.getHouseById(rentalDTO.getHouseId());
            for (Integer tenantId : rentalDTO.getTenantIds()) {
                tenants.add(tenantFacade.getTenantById(tenantId));
            }
            rental = new Rental(
                    rentalDTO.getStartDate(),
                    rentalDTO.getEndDate(),
                    rentalDTO.getPriceAnnual(),
                    rentalDTO.getDeposit(),
                    rentalDTO.getContactPerson(),
                    house,
                    tenants);

            rental = facade.createRental(rental);

        } catch (EntityNotFoundException entityNotFoundException) {
            throw new NotFoundException("The house is no where to be found");

        } catch (InvalidDateException invalidDateException) {
            return Response.status(HttpStatus.NO_CONTENT_204.getStatusCode()).build();
        }
        rentalDTO = buildStandardRentalDTO(rental);

        String rentalToJson = GSON.toJson(rentalDTO);
        return Response.status(HttpStatus.CREATED_201.getStatusCode()).entity(rentalToJson).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("rentals/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response putRental(@PathParam("id") int id, String rentalFromJson) {
        RentalDTO rentalDTO = GSON.fromJson(rentalFromJson, RentalDTO.class);
        Rental rental;

        try {
            rental = rentalFacade.getRentalById(id);
            House house = houseFacade.getHouseById(rentalDTO.getHouse().getId());

            rental.setStartDate(rentalDTO.getStartDate());
            rental.setEndDate(rentalDTO.getEndDate());
            rental.setPriceAnnual(rentalDTO.getPriceAnnual());
            rental.setDeposit(rentalDTO.getDeposit());
            rental.setContactPerson(rentalDTO.getContactPerson());
            rental.setHouse(house);

            facade.updateRental(rental);

        } catch (EntityNotFoundException entityNotFoundException) {
            throw new NotFoundException("Rental does not exist");

        } catch (InvalidDateException exception) {
            return Response.status(HttpStatus.NO_CONTENT_204.getStatusCode()).build();

        } catch (NullPointerException nullPointerException) {
            throw new NotFoundException("House does not exist");
        }

        RentalDTO updatedRentalDTO = buildStandardRentalDTO(rental);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(GSON.toJson(updatedRentalDTO)).build();
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("rentals/{id}")
    public Response deleteRental(@PathParam("id") int id) {
        try {
            facade.deleteRental(id);
        } catch (EntityNotFoundException exception) {

        }
        return Response.status(HttpStatus.NO_CONTENT_204.getStatusCode()).build();
    }

    private UserDTO buildStandardUserDTO(User user) {
        return new UserDTO.Builder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setAge(user.getAge())
                .setRoles(user.getRolesAsStringList())
                .build();
    }

    private RentalDTO buildStandardRentalDTO(Rental rental) {
        HouseDTO houseDTO = new HouseDTO.Builder()
                .setId(rental.getHouse().getId())
                .setAddress(rental.getHouse().getAddress())
                .setCity(rental.getHouse().getCity())
                .setNumberOfRooms(rental.getHouse().getNumberOfRooms())
                .build();

        return new RentalDTO.Builder()
                .setId(rental.getId())
                .setStartDate(rental.getStartDate())
                .setEndDate(rental.getEndDate())
                .setPriceAnnual(rental.getPriceAnnual())
                .setDeposit(rental.getDeposit())
                .setContactPerson(rental.getContactPerson())
                .setHouse(houseDTO)
                .setTenants(rental.getTenantsAsStringList())
                .build();
    }
}
