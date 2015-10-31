package example.hp.com.myclient.TempLogin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import example.hp.com.myclient.R;
import example.hp.com.myclient.Utils.HttpCallbackListener;
import example.hp.com.myclient.Utils.HttpUtil;
import example.hp.com.myclient.Tools.MyApplication;
import example.hp.com.myclient.MyUser;

public class TempLogin extends AppCompatActivity implements View.OnClickListener{

    private EditText etxtId;
    private EditText etxtPassword;

    private Button btnSubmit;
    private Button btnForget;

    private TextView txtGoToRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_login_activity);

        etxtId=(EditText)findViewById(R.id.etxtUserName);
        etxtPassword=(EditText)findViewById(R.id.extxPassword);

        btnSubmit=(Button)findViewById(R.id.btnSubmitLogin);
        btnForget=(Button)findViewById(R.id.btnForgetPassword);

        txtGoToRegister=(TextView)findViewById(R.id.txtLoginToResiger);

        btnSubmit.setOnClickListener(this);
        btnForget.setOnClickListener(this);
        txtGoToRegister.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmitLogin:
                final String userId=etxtId.getText().toString().trim();
                String password=etxtPassword.getText().toString().trim();
                if(userId.isEmpty()||password.isEmpty()){
                    Toast.makeText(MyApplication.getContext(),
                            "用户名或密码不能为空，请检查输入",Toast.LENGTH_SHORT).show();
                }else{
                    String query="username="+userId+"&password="+password;
                    String url= HttpUtil.BASE_URL+"/login?"+query;
                    Log.d("queryUrl____________", url);
                    // FIXME: 2015/10/23 Http模块有问题
                    HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.d(response, "onFinish ");
                            String res = response.trim();
                            if (res.equals("0")) {
                                // 登陆成功
                                MyUser.setUsername(userId);
                                MyUser.setIsLoggedIn(true);
                                checkToFinish();
                            } else if (res.equals("1")) {
                                // 账号或密码错误
//                                Toast.makeText(MyApplication.getContext(),
//                                        "账号或密码错误，请检查..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
//                            Toast.makeText(MyApplication.getContext(),
//                                    "网络连接错误，请稍候重试..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.btnForgetPassword:
                Log.d("btnForgetPassword","btnForgetPassword");
                break;
            case R.id.txtLoginToResiger:
                // 上方注册按钮，跳转到注册页面
                Intent intentRegister=new Intent(MyApplication.getContext(), TempRegister.class);
                startActivity(intentRegister);
                break;

        }
    }


    private void checkToFinish(){
        if(MyUser.isLoggedIn()){
            Log.d("LoginFragmentfinish", "checkToFinish ");
            this.finish();
            overridePendingTransition(R.anim.a,R.anim.b);
        }
    }
}
