package contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.requests.LoanRequest;
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
import services.LoanService;
import services.MemberService;

@Stateless
@Path("loan")
public class LoanContoller {
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
    @Path("loan")
    @Produces({MediaType.APPLICATION_JSON})
    public Response loanGET(@Context HttpHeaders hh) {
        try {
            BasicResponse res = new BasicResponse();
            LoanService service = new LoanService();
            res.setData(service.getLoans(db.getConnection()));
            res.setStatus(0);
            res.setMessage("success");
            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }

    @POST
    @Path("loan")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response loanPOST(@Context HttpHeaders hh,String requestStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BasicResponse res = new BasicResponse();
            LoanService service = new LoanService();
            LoanRequest request=mapper.readValue(requestStr, LoanRequest.class);
            if(request.getAction().equalsIgnoreCase("new")){
                res=service.InsertLoans(db.getConnection(),request);
            }else if(request.getAction().equalsIgnoreCase("approve")){
                res=service.ApproveLoans(db.getConnection(),request);
            }else if(request.getAction().equalsIgnoreCase("post")){
                res=service.InsertLoans(db.getConnection(),request);
            }else {
                //reject
                res=service.InsertLoans(db.getConnection(),request);
            }

            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }
}
