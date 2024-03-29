package example.hp.com.myclient.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import example.hp.com.myclient.MyUser;
import example.hp.com.myclient.R;
import example.hp.com.myclient.TempLogin.TempLogin;
import example.hp.com.myclient.TempLogin.TempRegister;
import example.hp.com.myclient.Tools.MyApplication;

/**
 * Created by hp on 2015/10/21.
 */
public class UserFragment extends Fragment implements View.OnClickListener{

//    private Button btnlogin,btnregister;
    private ImageButton btnlogin,btnregister;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        btnlogin=(ImageButton)view.findViewById(R.id.btn_login);
        btnregister=(ImageButton)view.findViewById(R.id.btn_register);

        btnregister.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                // TODO: 2015/10/23 跳转到登录界面
                Intent intentLogin=new Intent(MyApplication.getContext(), TempLogin.class);
                startActivity(intentLogin);
                getActivity().overridePendingTransition(R.anim.a, R.anim.b);
                break;
            case R.id.btn_register:
                // TODO: 2015/10/23 跳转到注册界面
                Intent intentRegister=new Intent(MyApplication.getContext(), TempRegister.class);
                startActivity(intentRegister);
                getActivity().overridePendingTransition(R.anim.a, R.anim.b);
                break;
        }
    }
}