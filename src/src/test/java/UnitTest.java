import jakarta.inject.Inject;
import org.junit.Test;
import services.DBConnectionService;
import services.LoanService;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitTest {
    @Inject
    private DBConnectionService db;

    @Test
    public void testConnection() throws SQLException {
        Connection conx = db.getConnection();
        if (conx == null) {
            System.out.println("no connection");
        }else{
            System.out.println("connected");
        }
    }
    @Test
    public void testCheckEligibility_ApproveLoan() throws SQLException {

        Connection conx = db.getConnection(); // Unit test -> no real DB
        Double loanAmount = 16000.00;
        String memberNumber = "MB10003";

        // Act

        Boolean result = LoanService.checkEligibility(
                conx,
                loanAmount,
                memberNumber
        );

        // Assert
        if(result){
            assertTrue(result, "Member should qualify for the loan");
            System.out.println("member qualified");
        }else{
            System.out.println("member did not qualify for the loan");
            assertFalse(result, "Member should not qualify for the loan");
        }

    }
}
