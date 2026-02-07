package services;

import dtos.requests.LoginRequest;
import dtos.response.BasicResponse;
import dtos.response.RegistrationResponse;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginService {
    public static BasicResponse login(Connection conx, LoginRequest request) {
        BasicResponse response = new BasicResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.MembersAuth where UserName=? AND  Password=?");
            ps.setString(1, request.getUsername());
            ps.setString(2, request.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                request.setMemberToken(UUID.randomUUID().toString() + "." + UUID.randomUUID().toString());
                response.setMessage("Successfully logged in");
                response.setData(request.getMemberToken());
            }else {
                response.setMessage("Invalid username or password");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private static Map<String, LoginRequest> loginsession = new HashMap<>();

    public static void addLoginSession(String memberToken, LoginRequest loggedInSession) {
        loginsession.put(memberToken, loggedInSession);
    }

    public LoginRequest validateMemberToken(HttpHeaders hh) {

        MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
        String memberToken = headerParams.getFirst("MemberToken");

        if (loginsession.containsKey(memberToken)) {
            return loginsession.get(memberToken);
        }

        return null;
    }
}
