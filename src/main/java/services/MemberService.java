package services;

import dtos.requests.LoanRequest;
import dtos.requests.MemberRegRequest;
import dtos.response.BasicResponse;
import dtos.response.RegistrationResponse;
import jakarta.inject.Inject;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MemberService {

    public static BasicResponse getMemberRegistration( Connection conx) {
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

    public static BasicResponse RegisterMember(Connection conx, MemberRegRequest request) {
        BasicResponse response = new BasicResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Main.MasterFile WHERE MemberNumber=?");
            ps.setString(1, request.getIdNumber());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                response.setStatus(-1);
                response.setMessage("Member already exists");
            }else{
                ps=conx.prepareStatement("INSERT INTO Main.MaterFile(MemberNo,CellPhone,EmailAddress,IdNumber,DateOfBirth) VALUES(?,?,?,?,?)");
                ps.setString(1, request.getMemberNo());
                ps.setString(2, request.getCellPhone());
                ps.setString(3, request.getEmailAddress());
                ps.setString(4, request.getIdNumber());
                ps.setString(5, request.getDateOfBirth());

                ps.executeUpdate();
                response.setStatus(0);
                response.setMessage("Successfully appraised");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
