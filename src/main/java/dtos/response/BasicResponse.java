package dtos.response;

public class BasicResponse {
    private int status;
    private String message;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static BasicResponse OfSuccess(){
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(0);
        basicResponse.setMessage("success");
        return basicResponse;

    }
    public static BasicResponse OfSuccess(String message){
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(0);
        basicResponse.setMessage(message);
        return basicResponse;

    }
    public static BasicResponse OfSuccess(Object data){
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(0);
        basicResponse.setMessage("success");
        basicResponse.setData(data);
        return basicResponse;

    }
    public static BasicResponse OfSuccess(String message,Object data){
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(0);
        basicResponse.setMessage(message);
        basicResponse.setData(data);
        return basicResponse;

    }
    public static BasicResponse Failure(String message){
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(1);
        basicResponse.setMessage(message);
        return basicResponse;
    }
    public static BasicResponse Failure(){
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatus(1);
        basicResponse.setMessage("failed");
        return basicResponse;
    }

}
