package example.hp.com.myclient.BaiduMap;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;

public class init_key {
    Activity activity;
    public init_key(Activity a){
        activity = a;
        if ( initDirs() ) {
            initNavi();
        }
    }
    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "BNSDKDemo";
    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if ( mSDCardPath == null ) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if ( !f.exists() ) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    private void initNavi() {
        BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath + "/BaiduNaviSDK_SO");
        BaiduNaviManager.getInstance().init(activity, mSDCardPath, APP_FOLDER_NAME,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                            Toast.makeText(activity, authinfo, Toast.LENGTH_LONG).show();
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }

                    }
                    public void initSuccess() {
                    }

                    public void initStart() {
                    }

                    public void initFailed() {
                    }
                }, null /*mTTSCallback*/);
    }


    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
}
