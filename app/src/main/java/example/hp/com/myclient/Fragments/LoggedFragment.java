package example.hp.com.myclient.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import example.hp.com.myclient.R;
import example.hp.com.myclient.MyUser;
/**
 * Created by hp on 2015/10/28.
 */
public class LoggedFragment extends Fragment implements View.OnClickListener{

    private TextView txtUsername;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_logged, container, false);

        txtUsername=(TextView)view.findViewById(R.id.logged_txt_username);
        txtUsername.setText(MyUser.getUsername());

        btnLogout=(Button)view.findViewById(R.id.logged_btn_logout);
        btnLogout.setOnClickListener(this);
        return view;
    }

    /**
     *
     * @param containerViewId fragment的容器
     * @param fragment 要转换的页面
     * @param isAddedStack 是否添加到回退栈
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
        }
    }
}
