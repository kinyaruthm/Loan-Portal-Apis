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
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.MasterFile");
            ArrayList<RegistrationResponse> members = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                RegistrationResponse res = new RegistrationResponse();
                res.setMemberNo(rs.getString("MemberNo"));
                res.setCellPhone(rs.getString("CellPhone"));
                res.setEmailAddress(rs.getString("EmailAddress"));
                res.setIdNumber(rs.getString("IdNumber"));
                res.setDateOfBirth(rs.getString("DateOfBirth"));
                res.setPayrollNo(rs.getString("PayrollNo"));
                res.setPhoneNo(rs.getString("PhoneNo"));
                res.setFullNames(rs.getString("FullNames"));
                res.setSalaryAcc(rs.getString("SalaryAcc"));
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
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.MasterFile WHERE MemberNo=?");
            ps.setString(1, request.getMemberNo());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                response.setStatus(-1);
                response.setMessage("Member already exists");
            }else{
                ps=conx.prepareStatement("INSERT INTO Deploy.MasterFile(MemberNo,CellPhone,EmailAddress,IdNumber,DateOfBirth,payrollNo,fullNames,salaryAcc) VALUES(?,?,?,?,?,?,?,?)");
                ps.setString(1, request.getMemberNo());
                ps.setString(2, request.getCellPhone());
                ps.setString(3, request.getEmailAddress());
                ps.setString(4, request.getIdNumber());
                ps.setString(5, request.getDateOfBirth());
                ps.setString(6,request.getPayrollNo());
                ps.setString(7,request.getFullNames());
                ps.setString(8, request.getSalaryAcc());

                ps.executeUpdate();
                response.setStatus(0);
                response.setMessage("Successfully added");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
