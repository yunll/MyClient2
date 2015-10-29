package example.hp.com.myclient.TempLogin;

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
import example.hp.com.myclient.Tools.HttpUtils;
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
                String userId=etxtId.getText().toString().trim();
                String password=etxtPassword.getText().toString().trim();
                if(userId.isEmpty()||password.isEmpty()){
                    Toast.makeText(MyApplication.getContext(),
                            "用户名或密码不能为空，请检查输入",Toast.LENGTH_SHORT).show();
                }else{
                    String query="username="+userId+"&password="+password;
                    Log.d("queryString____________", query);
                    String url= HttpUtils.BASE_URL+"/login?"+query;
                    // FIXME: 2015/10/23 Http模块有问题
                    new SubmitAsyncTask().execute(url);
                }
                break;
            case R.id.btnForgetPassword:
                Log.d("btnForgetPassword","btnForgetPassword");
                break;
            case R.id.txtLoginToResiger:
                // 暂时性的
                String username=etxtId.getText().toString().trim();
                String pw=etxtPassword.getText().toString().trim();
                String query="username="+username+"&password="+pw;
                String url= HttpUtils.BASE_URL+"/register?"+query;
                new SubmitAsyncTask().execute(url);
                break;

        }
    }


    private void checkToFinish(){
        if(MyUser.isLoggedIn()){
            Log.d("LoginFragmentfinish", "checkToFinish ");
            this.finish();
        }
    }
    /**
     *  用于login的task
     */
    public class SubmitAsyncTask extends AsyncTask<String, Void, String> {
        String info = "";
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = params[0];
            String reps = doGet(url);
            return reps;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            String res = result.trim();
            if(res.equals("0")){
                MyUser.setUsername(etxtId.getText().toString().trim());
                MyUser.setIsLoggedIn(true);
                checkToFinish();
            }else if(res.equals("1")){
                Toast.makeText(MyApplication.getContext(),
                        "用户名或密码错误，请检查.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MyApplication.getContext(),
                        "网络连接错误，请稍候重试..",Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(result);
        }
    }
    private String doGet(String url){
        String responseStr = "";
        try {
            HttpGet httpRequest = new HttpGet(url);
            HttpParams params = new BasicHttpParams();
            ConnManagerParams.setTimeout(params, 1000);
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            HttpConnectionParams.setSoTimeout(params, 5000);
            httpRequest.setParams(params);

            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            final int ret = httpResponse.getStatusLine().getStatusCode();
            if(ret == HttpStatus.SC_OK){
                responseStr = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
            }else{
                responseStr = "-1";
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseStr;
    }
}
