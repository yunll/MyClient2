package example.hp.com.myclient.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.List;

import example.hp.com.myclient.Models.ConsumeRecord;
import example.hp.com.myclient.R;
import example.hp.com.myclient.MyUser;
import example.hp.com.myclient.Tools.HttpUtils;
import example.hp.com.myclient.Tools.MyApplication;

/**
 * Created by hp on 2015/10/28.
 */
public class LoggedFragment extends Fragment implements View.OnClickListener{

    private TextView txtUsername;
    private Button btnLogout;
    private Button btnHistory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_logged, container, false);

        txtUsername=(TextView)view.findViewById(R.id.logged_txt_username);
        txtUsername.setText(MyUser.getUsername());

        btnLogout=(Button)view.findViewById(R.id.logged_btn_logout);
        btnHistory=(Button)view.findViewById(R.id.logged_btn_consume_history);

        btnLogout.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        return view;
    }

    /**
     *
     * @param containerViewId fragment������
     * @param fragment Ҫת����ҳ��
     * @param isAddedStack �Ƿ���ӵ�����ջ
     */
    public void ChangeFragment(int containerViewId ,Fragment fragment, boolean isAddedStack){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
//                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(containerViewId, fragment);
        if(isAddedStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logged_btn_logout:
                MyUser.setIsLoggedIn(false);
                ChangeFragment(R.id.content_frame, new UserFragment(), false);
                break;
            case R.id.logged_btn_consume_history:
                String query="username="+MyUser.getUsername();
                String url= HttpUtils.BASE_URL+"/consumehistory?"+query;
                new SubmitAsyncTask().execute(url);
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
            Log.d("res__________________",res);
            Gson gson=new Gson();
            List<ConsumeRecord> list = gson.fromJson(res, new
                    TypeToken<List<ConsumeRecord>>(){}.getType());
            for(ConsumeRecord c: list){
                Toast.makeText(MyApplication.getContext(),
                        c.toString(),Toast.LENGTH_SHORT).show();
                Log.d("consume____________",c.toString());
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
