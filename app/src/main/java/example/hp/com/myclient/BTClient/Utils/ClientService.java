package example.hp.com.myclient.BTClient.Utils;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import example.hp.com.myclient.BTClient.Module.MyBluetoothDevice;
import example.hp.com.myclient.Tools.BluetoothTools;
import example.hp.com.myclient.Tools.MyApplication;

/**
 * Created by hp on 2015/9/24.
 */
public class ClientService extends Service {

    private ControlBinder mBinder=new ControlBinder();

    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private ClientCommunicateThread communicateThread;

    // 正在运行状态
    private boolean isRunning=true;

    // 正在连接，连接时就取消搜索,连接成功后就恢复为false
    // 同时，每次发消息是，也标记为icConnecting
    private boolean icConnecting=false;

    // 是否连接成功,连接成功后，记录远程设备的地址
    private boolean isConnected=false;
    private String remoteDeviceAddress;

    Handler mhandler=new Handler();
    private final Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // 不是连接状态，则扫描附近设备
            if(!icConnecting){
                bluetoothAdapter.cancelDiscovery();
                // 开始搜索设备
                if(!bluetoothAdapter.isEnabled()){
                    // 如果蓝牙处于关闭状态，那么就询问打开
                    Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoveryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(discoveryIntent);
                }
                bluetoothAdapter.startDiscovery();
            }
            // 如果要求继续运行的话，那就继续搜索，否则就停下来
            if(isRunning){
                mhandler.postDelayed(runnable,400);
            }
        }
    };

    // 流程控制的binder
    public class ControlBinder extends Binder{
        // 选择设备进行连接
        public void SelectedDevice(int pos){
            // 暂停搜索，不然连接很卡
            icConnecting=true;
            bluetoothAdapter.cancelDiscovery();
            new ClientConnectThread(handler, MyBluetoothDevice.bluetoothDeviceList.get(pos)).start();
        }
        // 发送消息至对方设备
        public void DataToServer(String data){
            if (communicateThread != null) {
                communicateThread.writeObject(data);
            }
        }
        // 请求额外信息
        public boolean RequestForExtra(int pos){
            // TODO: 2015/10/18  应该一口气全部都下过来，因为这些信息百度地图也要用
            // 向这个设备发送extra请求，然后判断返回结果
            if(true){
                return true;
            }else{
                return false;
            }

        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
                    // 如果连接成功，就标注连接成功，并且
                    // FIXME: 2015/10/18 这里会有bug，比如切换到桌面再切换回来
                    icConnecting=false;
                    isConnected=true;
                    // TODO: 2015/9/25  启动交流线程
                    BluetoothSocket socket=(BluetoothSocket)msg.obj;
                    String deviceName=socket.getRemoteDevice().getName();

                    // 记录远程设备的地址到本地
                    remoteDeviceAddress=socket.getRemoteDevice().getAddress();

                    communicateThread=new ClientCommunicateThread(handler,socket);
                    communicateThread.start();

                    Intent connectSuccessIntent=new Intent(BluetoothTools.ACTION_CONNECT_SUCCESS);
                    connectSuccessIntent.putExtra(BluetoothTools.REMOTE_DEVICE_ADDRESS,remoteDeviceAddress);
                    connectSuccessIntent.putExtra(BluetoothTools.REMOTE_DEVICE_NAME,deviceName);

                    sendBroadcast(connectSuccessIntent);

                    break;
                case BluetoothTools.MESSAGE_RECEIVE_MESSAGE:
                    // FIXME: 2015/10/17 更新为接收到消息，判断类别，然后再进行操作
                    // 接收到来自线程的消息，那么就更新到activity上
                    Intent msgIntent=new Intent(BluetoothTools.ACTION_MESSAGE_TO_ACTIVITY);
                    msgIntent.putExtra(BluetoothTools.DATA,(Serializable)msg.obj);
                    sendBroadcast(msgIntent);
                    break;
                case BluetoothTools.MESSAGE_CONNECT_ERROR:
                    icConnecting= false;
                    isConnected=false;
                    Intent errorIntent=new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
                    sendBroadcast(errorIntent);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    // 系统的蓝牙类搜索时自己发出的广播
    private BroadcastReceiver discoveryReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    // 设备搜索完毕（可能是超时终止）
                    if(MyBluetoothDevice.bluetoothDeviceList.size()==0){
                        // TODO: 2015/9/25  没有找到可用设备，请重新搜索
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    // 每次查找到设备，就更新到界面上
                    BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    Log.d(device.getName() + "____________", Short.toString(rssi));

                    MyBluetoothDevice myBluetoothDevice;
                    try {
                        int pos=MyBluetoothDevice.addressList.indexOf(device.getAddress());
                        if(pos==-1){
                            // 如果没有出现过这个地址，那么就添加到列表里
                            MyBluetoothDevice.bluetoothDeviceList.add(device);
                            MyBluetoothDevice.addressList.add(device.getAddress());
                            myBluetoothDevice=new MyBluetoothDevice(device.getName(),
                                    device.getAddress(),rssi);
                            MyBluetoothDevice.myBluetoothDeviceList.add(myBluetoothDevice);
                            // FIXME: 2015/10/18 自动连接功能
//                        // region #直接去连接设备，不确定能不能这么做
//                        int selectedpos=BluetoothTools.ParkingDeviceAddressList.indexOf(device.getAddress());
//                        if(selectedpos!=-1&&true){
//                            // 如果这个设备是停车场里的，而且当前符合当前进出状态
//                            // 那么就去连接他
//                            try{
//                                mBinder.SelectedDevice(selectedpos);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                                Toast.makeText(MyApplication.getContext(),
//                                        "连接失败，请重试",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        // endregion
                        }else{
                            // 如果已经出现过这个地址，那么就把新的RSSI保存到数组
                            MyBluetoothDevice.myBluetoothDeviceList.get(pos).AddRssi(rssi);
                            myBluetoothDevice=MyBluetoothDevice.myBluetoothDeviceList.get(pos);

                            // FIXME: 2015/10/18 改为只判断与连接诶中的设备的距离
                            if(myBluetoothDevice.address==remoteDeviceAddress&&
                                    myBluetoothDevice.distance<0.5) {
                                Toast.makeText(MyApplication.getContext(), "与目标设备距离较近",
                                        Toast.LENGTH_SHORT).show();
                                mBinder.DataToServer("距离为"+
                                        Double.toString(myBluetoothDevice.distance)+"米");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent foundIntent=new Intent(BluetoothTools.ACTION_FOUND_DEVICE);
                    sendBroadcast(foundIntent);
                    break;
            }
        }
    };


    // 用于控制程序流程的广播
    private BroadcastReceiver controlReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch (action){
                case BluetoothTools.ACTION_START_DISCOVERY:
                    // TODO: 2015/10/18 清空当前连接状态。？？是否有必要？感觉应该保留连接状态
//                    isConnected=false;
//                    icConnecting=false;

                    // 每隔500ms，就扫描一次
                    mhandler.postDelayed(runnable,5000);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        //关于设备搜索的filter
        IntentFilter discoveryFilter = new IntentFilter();
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, discoveryFilter);

        // 关于流程控制的filter
        IntentFilter controlFilter = new IntentFilter();
        controlFilter.addAction(BluetoothTools.ACTION_START_DISCOVERY);
//        controlFilter.addAction(BluetoothTools.ACTION_SELECTED_DEVICE);
//        controlFilter.addAction(BluetoothTools.ACTION_STOP_SERVICE);
//        controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_SERVICE);
        registerReceiver(controlReceiver, controlFilter);

        isRunning=true;

        // 清空操作
        MyBluetoothDevice.Init();
        super.onCreate();
    }

    // TODO: 2015/10/4   每次启动服务时
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning=true;

        return super.onStartCommand(intent, flags, startId);
    }

    // TODO: 2015/10/4 每次停止服务时
    @Override
    public void onDestroy() {
        isRunning=false;


        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
