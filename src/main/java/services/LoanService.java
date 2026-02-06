package services;

import dtos.requests.LoanRequest;
import dtos.response.BasicResponse;
import dtos.response.LoanResponse;
import dtos.response.RegistrationResponse;
import dtos.response.SavingsResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static java.lang.Thread.State.NEW;

public class LoanService {
    public static BasicResponse getLoans(Connection conx) {
        BasicResponse response = new BasicResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.Loans");
            ArrayList<LoanResponse> loans = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                LoanResponse res = new LoanResponse();
                res.setLoanId(rs.getString("LoanId"));
                res.setLoanName(rs.getString("LoanName"));
                res.setLoanAmount(rs.getDouble("LoanAmount"));
                res.setLoanStatus(rs.getString("LoanStatus"));
                res.setPeriod(rs.getInt("Period"));
                res.setMemberNumber(rs.getString("MemberNumber"));
                loans.add(res);
            }
            response.setData(loans);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static BasicResponse InsertLoans(Connection conx, LoanRequest request) {
        BasicResponse response = new BasicResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.Loans WHERE LoanId=?");
            ps.setString(1, request.getLoanId());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                response.setStatus(-1);
                response.setMessage("Loan already exists");
            }else{
                ps=conx.prepareStatement("INSERT INTO Deploy.Loans(LoanId,LoanName,LoanAmount,LoanStatus,Period,MemberNumber) VALUES(?,?,?,?,?,?)");
                ps.setString(1, request.getLoanId());
                ps.setString(2, request.getLoanName());
                ps.setDouble(3, request.getLoanAmount());
                ps.setString(4, request.getLoanStatus());
                ps.setInt(5, request.getPeriod());
                ps.setString(6, request.getMemberNumber());
                ps.executeUpdate();
                response.setStatus(0);
                response.setMessage("Successfully appraised");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static BasicResponse ApproveLoans(Connection conx, LoanRequest request) {
        BasicResponse response = new BasicResponse();
        try{
            PreparedStatement ps= conx.prepareStatement("SELECT * FROM Deploy.Loans WHERE LoanId=? AND LoanStatus= ? ");
            ps.setString(1, request.getLoanId());
            ps.setString(2, "NEW");
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                response.setStatus(-1);
                response.setMessage("Loan not in a status it can be approved");
            }else{
                ps=conx.prepareStatement("UPDATE Deploy.Loans SET LoanStatus=? WHERE LoanId=? AND LoanStatus= ? AND MemberNumber=?");
                ps.setString(1, "Approved");
                ps.setString(2, request.getLoanId());
                ps.setString(3, "NEW");
                ps.setString(4, request.getMemberNumber());
                ps.executeUpdate();
                response.setStatus(0);
                response.setMessage("Successfully updated");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    //eligibility check: Minimum savings 2000, loan eligible amount should be 3 times the savings
    public static Boolean checkEligibility(Connection conx,Double loanAmount,String MemberNo) {
        Boolean qualifies = false;
        try{
            SavingsResponse res=SavingService.getSavingsperMember(conx,MemberNo);
            if(res.getAmount()>= 2000){
                Double eligibleAmount=res.getAmount()* 3;
                if(loanAmount <= eligibleAmount){
                    qualifies=true;
                }
            }else {
                qualifies=false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return qualifies;
    }
}
