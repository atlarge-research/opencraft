package science.atlarge.opencraft.opencraft.net.http;

public interface HttpCallback {

    void done(String response);

    void error(Throwable throwable);

}
