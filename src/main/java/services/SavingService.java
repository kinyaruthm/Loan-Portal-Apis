package services;

import dtos.requests.LoanRequest;
import dtos.requests.SavingsRequest;
import dtos.response.BasicResponse;
import dtos.response.SavingsResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SavingService {
    public static SavingsResponse getSavingsperMember(Connection conx, String memberNo) {
        SavingsResponse res=new SavingsResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.Savings WHERE MemberNumber=?");
            ps.setString(1, memberNo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                res.setAmount(rs.getDouble("Amount"));
                res.setSchemeId(rs.getString("SchemeId"));
                res.setSchemeName(rs.getString("SchemeName"));
                res.setSavingsAccount(rs.getString("SavingsAccount"));
                res.setMemberNumber(rs.getString("MemberNumber"));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BasicResponse ContributeToScheme(Connection conx, SavingsRequest request) {
        BasicResponse response = new BasicResponse();
        try{
               PreparedStatement ps=conx.prepareStatement("INSERT INTO Deploy.Savings(SchemeId,SchemeName,MemberNumber,Amount,SavingsAccount) VALUES(?,?,?,?,?)");
                ps.setString(1, request.getSchemeId());
                ps.setString(2, request.getSchemeName());
                ps.setString(3, request.getMemberNumber());
                ps.setDouble(4, request.getAmount());
                ps.setString(5, request.getSavingsAccount());

                ps.executeUpdate();
                response.setStatus(0);
                response.setMessage("Successfully contributed");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }


}
