package science.atlarge.opencraft.opencraft.serverless;

public class HelloLambdaOutput {
    private String message;
    private String body;
    private String location;

    public String getLocation() {
        return location;
    }

    public String getMessage() {
        return message;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
