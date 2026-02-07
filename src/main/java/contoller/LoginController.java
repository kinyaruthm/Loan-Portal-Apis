package contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.requests.LoginRequest;
import dtos.response.BasicResponse;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.DBConnectionService;
import services.LoanService;
import services.LoginService;

import java.sql.Connection;

@Stateless
@Path("")
public class LoginController {
    @Inject
    private DBConnectionService db;

    @POST
    @Path("login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response loginGet(@Context HttpHeaders hh, String requestStr) {
        try {
            BasicResponse res = new BasicResponse();
            LoginService service = new LoginService();
            ObjectMapper mapper = new ObjectMapper();
            LoginRequest request=mapper.readValue(requestStr, LoginRequest.class);
            res = service.login(db.getConnection(), request);
            LoginService.addLoginSession(request.getMemberToken(), request);
            res.setStatus(0);
            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }

}
