package dtos.requests;

public class LoginRequest {
    private String username;
    private String password;
    private String memberToken;

    public enum Role {
        ADMIN,
        LOAN_OFFICER,
        MEMBER,
        AUDITOR
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberToken() {
        return memberToken;
    }

    public Object setMemberToken(String memberToken) {
        this.memberToken = memberToken;
        return null;
    }
}

