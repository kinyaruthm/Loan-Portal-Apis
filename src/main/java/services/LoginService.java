package services;

import dtos.requests.LoginRequest;
import dtos.response.BasicResponse;
import dtos.response.LoginResponse;
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
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setMemberNo(rs.getString("MemberNo"));
                loginResponse.setMemberToken(UUID.randomUUID().toString() + "." + UUID.randomUUID().toString());
                LoginResponse session = new LoginResponse(loginResponse.getMemberNo(), loginResponse.getMemberToken());
                loginsession.put(loginResponse.getMemberToken(), session);
                response.setMessage("Successfully logged in");
                response.setData(loginResponse);
                response.setStatus(0);
            }else {
                response.setStatus(-1);
                response.setMessage("Invalid username or password");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private static Map<String, LoginResponse> loginsession = new HashMap<>();

    public static void addLoginSession(String memberToken, LoginResponse loggedInSession) {
        loginsession.put(memberToken, loggedInSession);
    }

    public LoginResponse validateMemberToken(HttpHeaders hh) {

        MultivaluedMap<String, String> headerParams = hh.getRequestHeaders();
        String memberToken = headerParams.getFirst("MemberToken");

        if (memberToken != null && loginsession.containsKey(memberToken)) {
            return loginsession.get(memberToken);
        }

        return null;
    }
}
