package example.hp.com.myclient.Utils;

/**
 * Created by hp on 2015/10/30.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}