package rest;

import dtos.TenantDTO;
import entities.Tenant;
import facades.TenantFacade;
import org.glassfish.grizzly.http.util.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("tenants")
public class TenantResource extends Resource {
    private final TenantFacade facade = TenantFacade.getFacade(EMF);

    @GET
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTenants() {
        List<Tenant> tenants = facade.getAllTenants();
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
}