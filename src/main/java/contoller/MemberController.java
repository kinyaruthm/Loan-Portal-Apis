package contoller;


import dtos.requests.MemberRegRequest;
import dtos.response.BasicResponse;
import dtos.response.RegistrationResponse;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.DBConnectionService;
import services.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateless
@Path("/member")
public class MemberController {
    @Inject
    private DBConnectionService db;

    @Context
    HttpServletRequest mRequest;

    @Context
    HttpServletResponse mResponse;

    @PostConstruct
    public void init() {
        mResponse.setHeader("Strict-Transport-Security", "max-age=15768000");
        mResponse.setHeader("X-XSS-Protection", "1; mode=block");
        mResponse.setHeader("X-Content-Type-Options", "nosniff");
    }

    @GET
    @Path("profile")
    @Produces({MediaType.APPLICATION_JSON})
    public Response profileGET(@Context HttpHeaders hh) {
        try {
            BasicResponse res = new BasicResponse();
            MemberService service = new MemberService();
            res.setData(service.getMemberRegistration(db.getConnection()));
            res.setStatus(0);
            res.setMessage("success");
            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }

    @POST
    @Path("registration")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response RegistrationPOST(@Context HttpHeaders hh,String request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BasicResponse res = new BasicResponse();
            MemberService service = new MemberService();
            MemberRegRequest req=mapper.readValue(request, MemberRegRequest.class);
            res=service.RegisterMember(db.getConnection(),req);
            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }

}
