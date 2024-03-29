package example.hp.com.myclient;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.impl.client.TunnelRefusedException;

import java.util.ArrayList;
import java.util.List;

import example.hp.com.myclient.BTClient.Module.MyBluetoothDevice;
import example.hp.com.myclient.BTClient.Utils.ClientService;
import example.hp.com.myclient.Fragments.BluetoothFragment;
import example.hp.com.myclient.Fragments.ErweimaFragment;
import example.hp.com.myclient.Fragments.MapFragment;
import example.hp.com.myclient.Fragments.NewUserFragment;
import example.hp.com.myclient.Fragments.UserFragment;

import example.hp.com.myclient.Tools.BluetoothTools;
import example.hp.com.myclient.Tools.MyApplication;
import example.hp.com.myclient.Tools.TabAdapter;

public class MainActivity extends AppCompatActivity implements BluetoothFragment.onMyBTFragmentListener {

    // 表示有多少个页面的list
    private List<Fragment> fragmentList=new ArrayList<>();
    public static  String[] TITLES = new String[]{"地图", "附近蓝牙","二维码","我"};

    // 具体的页面
    private MapFragment mapFragment=new MapFragment();
    private BluetoothFragment bluetoothFragment=new BluetoothFragment();
    private ErweimaFragment erweimaFragment=new ErweimaFragment();
    private UserFragment userFragment=new UserFragment();

    private NewUserFragment newUserFragment=new NewUserFragment();

    // 导航栏&ViewPager
//    private TabPageIndicator mPageIndicator;
    private TitlePageIndicator titlePageIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter fragPagerAdapter;


    // temp
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initUI();
        initPages();

        // gson测试，已通过
//        Gson gson=new Gson();
//        MyBluetoothDevice temp1=new MyBluetoothDevice();
//        MyBluetoothDevice temp2=new MyBluetoothDevice();
//        List<MyBluetoothDevice> temp=new ArrayList<>();
//        temp.add(temp1);
//        temp.add(temp2);
//        String object =gson.toJson(temp,new TypeToken<List<MyBluetoothDevice>>(){}.getType());
//        Log.d("_______________", object);
        // region # BluetoothClient 部分


        // endregion
    }

    @Override
    protected void onStart() {

    // region # BluetoothClient 部分

        // 吧程序托管给service，activity只负责更新界面
        Intent startserverIntent = new Intent(MyApplication.getContext(), ClientService.class);
        startService(startserverIntent);

        // 绑定ibinder
        Intent bindIntent=new Intent(this,ClientService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
        intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        intentFilter.addAction(BluetoothTools.ACTION_MESSAGE_TO_ACTIVITY);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_ERROR);
        registerReceiver(broadcastReceiver, intentFilter);

    // endregion

        super.onStart();
//        BeginScan();
    }

    @Override
    protected void onStop() {


    // region # BluetoothClient 部分
        // 注销Ibinder
        unbindService(connection);
        // 暂停时，注销服务和广播
        Intent stopIntent=new Intent(this, ClientService.class);
        stopService(stopIntent);

        // 注销广播
        unregisterReceiver(broadcastReceiver);
    // endregion


        super.onStop();
    }

    /**
     * 初始化各页面内容
     */
    private void initPages(){
        // 必须按顺序添加
        fragmentList.add(mapFragment);
        fragmentList.add(bluetoothFragment);
        fragmentList.add(erweimaFragment);
//        fragmentList.add(userFragment);
        fragmentList.add(newUserFragment);

    }
    /**
     * 初始化MainActivity的界面
     */
    private void initUI(){
//        mPageIndicator = (TabPageIndicator)findViewById(R.id.page_indicator);
        titlePageIndicator=(TitlePageIndicator)findViewById(R.id.titlePage);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);


        fragPagerAdapter = new TabAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(fragPagerAdapter);

//        mPageIndicator.setViewPager(mViewPager, 0);
        titlePageIndicator.setViewPager(mViewPager, 0);
    }


// region  # BluetoothClient 部分


    // service中的流程控制器
    private ClientService.ControlBinder controlBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controlBinder=(ClientService.ControlBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case BluetoothTools.ACTION_START_DISCOVERY:
                    // 开始搜索时，清空已有的设备列表，但是保留已经连接的设备

                    MyBluetoothDevice.Init();
                    break;
                case BluetoothTools.ACTION_FOUND_DEVICE:
                    // 刷新一下列表
//                    myBTDeviceAdapter.notifyDataSetChanged();
                    bluetoothFragment.UpdaterDeviceList();
                    // TODO: 2015/10/17 如果是目标地址，那么就直接去连接（联网从数据库中读取，最好是提前下载过来）
//                    controlBinder.SelectedDevice(position);
                    break;
                case BluetoothTools.ACTION_CONNECT_SUCCESS:
//                    remoteDeviceName=intent.getStringExtra(BluetoothTools.REMOTE_DEVICE_NAME);
                    String remoteDeviceAddress=intent.getStringExtra(BluetoothTools.REMOTE_DEVICE_ADDRESS);

                    // 连接成功后，更新目标设备的状态(显示详情，并且移动到第一位)
                    int position=MyBluetoothDevice.addressList.indexOf(remoteDeviceAddress);

                    // TODO: 2015/10/17 连接成功时，首先获取extra，获取成功则更新上去，获取失败则进行提示
                    if(controlBinder.RequestForExtra(position)==true){

                    }else{
                        Toast.makeText(MyApplication.getContext(), "获取详细信息失败...请重试", Toast.LENGTH_SHORT).show();
                        // TODO: 2015/10/17 加一个重新获取的按钮(仅在已连接成功的设备上)
                    }
                    // 连接成功的设备移动到第一个
                    MyBluetoothDevice temp=MyBluetoothDevice.myBluetoothDeviceList.remove(position);
                    temp.setIsConnected(true);
                    MyBluetoothDevice.myBluetoothDeviceList.add(0,temp);
                    bluetoothFragment.UpdaterDeviceList();
                    break;
                case BluetoothTools.ACTION_CONNECT_ERROR:
                    // 连接失败时
//                    txtState.setText("连接失败，请重试..");
                    Toast.makeText(MyApplication.getContext(),
                            "连接失败，请重试..",Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothTools.ACTION_MESSAGE_TO_ACTIVITY:
                    // TODO: 2015/10/17 接受来自server的消息，然后解析消息

//                    String data=intent.getStringExtra(BluetoothTools.DATA);
//                    String msg=remoteDeviceName+"  "+new Date().toLocaleString()+":\n"+data;
//                    chatList.add(msg);
//                    arrayAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };



    @Override
    public void onBeginScanning() {
        // 下拉刷新，然后开始搜索设备
        Intent searchIntent=new Intent(BluetoothTools.ACTION_START_DISCOVERY);
        sendBroadcast(searchIntent);
    }

    @Override
    public void onSelectDevice(int position) {
        controlBinder.SelectedDevice(position);
    }

    @Override
    public boolean onSetBTEnable(boolean enable) {
        return controlBinder.setBTAdapterEnbale(enable);
    }

// endregion

}
