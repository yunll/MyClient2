package example.hp.com.myclient.TempLogin;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

import example.hp.com.myclient.MyUser;
import example.hp.com.myclient.R;
import example.hp.com.myclient.Tools.HttpUtils;
import example.hp.com.myclient.Tools.MyApplication;

public class TempRegister extends AppCompatActivity implements OnClickListener{

    private EditText etxtUsername;
    private EditText etxtPassword;
    private Button btnSubmit;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_register_activity);

        etxtUsername=(EditText)findViewById(R.id.register_etxt_username);
        etxtPassword=(EditText)findViewById(R.id.register_etxt_password);

        btnSubmit=(Button)findViewById(R.id.register_btn_submit);
        btnCancel=(Button)findViewById(R.id.register_btn_cancel);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void checkToFinish(){
        if(MyUser.isLoggedIn()){
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn_submit:
                String username=etxtUsername.getText().toString().trim();
                String password=etxtPassword.getText().toString().trim();

                // TODO: 2015/10/28 联网注册
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(MyApplication.getContext(),
                            "用户名或密码不能为空，请检查输入",Toast.LENGTH_SHORT).show();
                }else{
                    String query="username="+username+"&password="+password;
                    Log.d("queryString____________", query);
                    String url= HttpUtils.BASE_URL+"/register?"+query;
                    // FIXME: 2015/10/23 Http模块有问题
                    new SubmitAsyncTask().execute(url);
                }
                break;
            case R.id.register_btn_cancel:
                this.finish();
                break;
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
                // 注册成功
                MyUser.setUsername(etxtUsername.getText().toString().trim());
                MyUser.setIsLoggedIn(true);
                checkToFinish();
            }else if(res.equals("1")){
                Toast.makeText(MyApplication.getContext(),
                        "该用户名已存在，更换..", Toast.LENGTH_SHORT).show();
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
