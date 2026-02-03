package services;

import dtos.response.BasicResponse;
import dtos.response.RegistrationResponse;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MemberService {
    public static BasicResponse getMemberRegistration(java.sql.Connection conx) {
        BasicResponse response = new BasicResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Main.MasterFile");
            ArrayList<RegistrationResponse> members = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                RegistrationResponse res = new RegistrationResponse();
                res.setMemberNo(rs.getString("MemberNo"));
                res.setCellPhone(rs.getString("CellPhone"));
                res.setEmailAddress(rs.getString("EmailAddress"));
                res.setIdNumber(rs.getString("IdNumber"));
                res.setDateOfBirth(rs.getString("DateOfBirth"));
                members.add(res);
            }
            response.setData(members);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
