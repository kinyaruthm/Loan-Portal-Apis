package dtos.requests;

public class SavingsRequest {
    private String schemeId;
    private String schemeName;
    private String memberNumber;
    private double Amount;
    private String SavingsAccount;

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getSavingsAccount() {
        return SavingsAccount;
    }

    public void setSavingsAccount(String savingsAccount) {
        SavingsAccount = savingsAccount;
    }
}
