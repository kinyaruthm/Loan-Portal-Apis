package contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.requests.LoanRequest;
import dtos.requests.LoginRequest;
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
import services.LoginService;
import services.MemberService;

@Stateless
@Path("")
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
            LoginService login=new LoginService();
            LoginRequest session = login.validateMemberToken(hh);
            if(session == null || session.getMemberToken() == null){
                res.setStatus(-1);
                res.setMessage("Invalid Member Token");
                return Response.status(Response.Status.FORBIDDEN).entity(res).build();
            }
            LoanService service = new LoanService();
            res=service.getLoans(db.getConnection());
            res.setStatus(0);
            res.setMessage("success");
            return Response.ok().entity(res).build();
        } catch (Exception ex) {
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
            LoginService login=new LoginService();
            LoginRequest session = login.validateMemberToken(hh);
            if(session == null || session.getMemberToken() == null){
                res.setStatus(-1);
                res.setMessage("Invalid Member Token");
                return Response.status(Response.Status.FORBIDDEN).entity(res).build();
            }
            LoanService service = new LoanService();
            LoanRequest request=mapper.readValue(requestStr, LoanRequest.class);
            if(request.getAction().equalsIgnoreCase("new")){
                //check eligibility
                Boolean eligible=false;
                eligible=service.checkEligibility(db.getConnection(), request.getLoanAmount(), request.getMemberNumber());
                if(eligible){
                    res=service.InsertLoans(db.getConnection(),request);
                }else{
                    res.setMessage("you are not eligible for this loan");
                    res.setStatus(-1);
                }
            }else if(request.getAction().equalsIgnoreCase("approve")){
                res=service.ApproveLoans(db.getConnection(),request);
            }else if(request.getAction().equalsIgnoreCase("post")) {
                //implement a loan disbursment
                res = service.InsertLoans(db.getConnection(), request);
            }
//            }else {
//                //reject
//                res=service.InsertLoans(db.getConnection(),request);
//            }

            return Response.ok().entity(res).build();
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error").build();
        }
    }
}
