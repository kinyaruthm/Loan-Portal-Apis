package dtos.response;

public class LoginResponse {
    private String memberNo;
    private String memberToken;

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberToken() {
        return memberToken;
    }

    public void setMemberToken(String memberToken) {
        this.memberToken = memberToken;
    }

    public LoginResponse(String memberNo, String memberToken) {
        this.memberNo = memberNo;
        this.memberToken = memberToken;
    }

    public LoginResponse() {
    }
}
