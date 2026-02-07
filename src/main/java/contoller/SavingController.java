package contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.requests.LoginRequest;
import dtos.requests.MemberRegRequest;
import dtos.requests.SavingsRequest;
import dtos.response.BasicResponse;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.*;

@Stateless
@Path("")
public class SavingController {
    @Inject
    private DBConnectionService db;

    @GET
    @Path("savings")
    @Produces({MediaType.APPLICATION_JSON})
    public Response loanGET(@Context HttpHeaders hh,@QueryParam("memberNo")String memberNo) {
        try {

            BasicResponse res = new BasicResponse();
            LoginService login=new LoginService();
            LoginRequest session = login.validateMemberToken(hh);
            if(session == null || session.getMemberToken() == null){
                res.setStatus(-1);
                res.setMessage("Invalid Member Token");
                return Response.status(Response.Status.FORBIDDEN).entity(res).build();
            }
            res.setData(SavingService.getSavingsperMember(db.getConnection(),memberNo));
            res.setStatus(0);
            res.setMessage("success");
            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }

    @POST
    @Path("savings")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response SavingsPOST(@Context HttpHeaders hh, String request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BasicResponse res = new BasicResponse();
            LoginService login=new LoginService();
            LoginRequest session = login.validateMemberToken(hh);
            if(session == null || session.getMemberToken() == null){
                res.setStatus(-1);
                res.setMessage("Invalid Member Token");
                return Response.status(Response.Status.FORBIDDEN).entity(res).build();
            }
            SavingsRequest req=mapper.readValue(request, SavingsRequest.class);
            res= SavingService.ContributeToScheme(db.getConnection(),req);
            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }

}
