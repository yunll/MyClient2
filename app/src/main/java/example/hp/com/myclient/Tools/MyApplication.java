package example.hp.com.myclient.Tools;

import android.app.Application;
import android.content.Context;

/**
 * Created by hp on 2015/10/20.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
