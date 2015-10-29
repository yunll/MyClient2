package example.hp.com.myclient.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import example.hp.com.myclient.BTClient.Module.MyBTDeviceAdapter;
import example.hp.com.myclient.BTClient.Module.MyBluetoothDevice;
import example.hp.com.myclient.MainActivity;
import example.hp.com.myclient.PullToRefresh.RefreshableView;
import example.hp.com.myclient.R;
import example.hp.com.myclient.SwitchButton.SwitchButton;
import example.hp.com.myclient.Tools.BluetoothTools;
import example.hp.com.myclient.Tools.MyApplication;


/**
 * Created by hp on 2015/10/21.
 */
public class BluetoothFragment extends Fragment {

    private Button btntest;

    // 用于显示listview和adapter
    private MyBTDeviceAdapter myBTDeviceAdapter;
    private ListView btDeviceList;

    /**
     * 用于控制下拉刷新
     */
    private RefreshableView refreshableView;

    private SwitchButton switchButton;

    BluetoothAdapter adapter= BluetoothTools.getBTAdapter();
//region # 接口
    private onMyBTFragmentListener myBTFragmentListener;

    public interface onMyBTFragmentListener {
        // Mainactivity实现onBeginScanningListener接口中的onBeginScanning方法
        // fragment中直接调用beginScanningListener.onBeginScanning()即可
        void onBeginScanning();
        void onSelectDevice(int position);
        boolean onSetBTEnable(boolean enable);
    }

    /**
     * 检查activity中是否已经实现了onBeginScanningListener接口
     * @param activity
     */
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            myBTFragmentListener =(onMyBTFragmentListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"接口未实现.");
        }
    }

//endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        // 下拉刷新的view
        refreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                // 如果蓝牙未打开，则提示一下
                if(BluetoothTools.getBTAdapter().isEnabled()){
                    try {
                        // TODO: 2015/10/22 更改释放时的动画，太生硬了
                        myBTFragmentListener.onBeginScanning();
                        // 这个sleep是表示显示搜索键3s
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    // TODO: 2015/10/28 提示蓝牙未打开,toast用不了
//                        Toast.makeText(MyApplication.getContext(),
//                                "蓝牙未打开，请先打开蓝牙.",Toast.LENGTH_SHORT).show();
                }
                if(true) {
                    // TODO: 2015/10/22 表示当搜索到设备时，才finish
                    refreshableView.finishRefreshing();
                }
            }
        }, 500);

        // 为listview绑定内容
        btDeviceList =(ListView)view.findViewById(R.id.btDeviceList);
        myBTDeviceAdapter=new MyBTDeviceAdapter(
                MyApplication.getContext(),R.layout.bt_device,
                MyBluetoothDevice.myBluetoothDeviceList);
        btDeviceList.setAdapter(myBTDeviceAdapter);


        btDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击设备，开始连接
                myBTFragmentListener.onSelectDevice(position);
            }
        });

        // 滑动开关控件
        // TODO: 2015/10/28 还应该监听外部的蓝牙开关？？
        switchButton =(SwitchButton)view.findViewById(R.id.switchbtn_bt);
        switchButton.setChecked(!adapter.isEnabled());
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // FIXME: 2015/10/28 这里一个遗留问题，就是状态是相反的，所以用了！
                if(!isChecked==true){
                    // 变成打开状态时，便询问打开蓝牙
                    // TODO: 2015/10/28 考虑加一个进度条
                    if(myBTFragmentListener.onSetBTEnable(true)==false){
                        // 如果打开失败
                        Toast.makeText(MyApplication.getContext(),
                                "打开蓝牙失败，请重试！",Toast.LENGTH_SHORT).show();
                        switchButton.setChecked(true);
                    }
                }else{
                    // TODO: 2015/10/28  关闭蓝牙时，首先询问是否关闭
//                    final boolean goingToClose=false;
//                    AlertDialog.Builder dialog=new AlertDialog.Builder(MyApplication.getContext());
//                    dialog.setTitle("是否关闭蓝牙？");
//                    dialog.setMessage("关闭蓝牙将会导致软件无法正常运作..");
//                    dialog.setCancelable(false);
//                    dialog.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // 如果真的要关闭蓝牙
//                        }
//                    });
//                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // 如果不关闭蓝牙
//                        }
//                    });
//                    dialog.show();


                    if(true){
                        // 如果真的要关闭蓝牙
                        if (myBTFragmentListener.onSetBTEnable(false) == false) {
                            // 如果关闭蓝牙失败
                            Toast.makeText(MyApplication.getContext(),
                                    "关闭蓝牙失败，请重试！", Toast.LENGTH_SHORT).show();
                            switchButton.setChecked(false);
                        }
                    }else{
                        // 如果不关闭蓝牙
                        switchButton.setChecked(false);
                    }
                    // 关闭之后，还要暂停线程
                }
            }
        });
        return view;
    }


//region # Public Function

    /**
     * 搜到新设备时，刷新listview
     */
    public void UpdaterDeviceList(){
        myBTDeviceAdapter.notifyDataSetChanged();
    }

// endregion



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

}
