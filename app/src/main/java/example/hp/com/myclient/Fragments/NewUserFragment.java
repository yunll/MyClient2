package example.hp.com.myclient.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import example.hp.com.myclient.R;
import example.hp.com.myclient.MyUser;
import example.hp.com.myclient.Tools.MyApplication;

/**
 * Created by hp on 2015/10/21.
 */
public class NewUserFragment extends Fragment {

    //    private Button btnlogin,btnregister;
    private ImageButton btnlogin,btnregister;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newuser, container, false);
        ChangeFragment(R.id.content_frame, new UserFragment(), false);

        return view;
    }


    // FIXME: 2015/10/28 两个页面会重叠？？
    @Override
    public void onStart() {
        super.onStart();
        if(MyUser.isLoggedIn()){
            try {
                ChangeFragment(R.id.content_frame, new LoggedFragment(), false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    /**
     *
     * @param containerViewId fragment的容器
     * @param fragment 要转换的页面
     * @param isAddedStack 是否添加到回退栈
     */
    public void ChangeFragment(int containerViewId ,Fragment fragment, boolean isAddedStack){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        // FIXME: 2015/10/29 不知道怎么添加切换动画
//        transaction.setCustomAnimations(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
//                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(containerViewId,fragment);
        if(isAddedStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


}