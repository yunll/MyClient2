package example.hp.com.myclient.BTClient.Module;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by hp on 2015/10/3.
 */
// Parcelable接口，用于intent自定义类型传递
public class MyBluetoothDevice implements Parcelable {

    // 全局通用的变量列表
    public static List<MyBluetoothDevice> myBluetoothDeviceList=new ArrayList<>();
    public static List<BluetoothDevice> bluetoothDeviceList=new ArrayList<>();
    public static List<String> addressList=new ArrayList<>();

    // 初始化各个列表
    public static void Init() {
        if (myBluetoothDeviceList.size() > 0) {
            MyBluetoothDevice TempMyBluetoothDevice = myBluetoothDeviceList.get(0);
            if (TempMyBluetoothDevice != null) {
                // 如果列表里存在设备,才可能需要清空列表
                if (TempMyBluetoothDevice.isConnected() == true) {
                    // 如果第一个设备已连接，那么需要把他保留下来
                    BluetoothDevice tempBluetoothDevice = bluetoothDeviceList.get(0);
                    String tempAddress = addressList.get(0);

                    myBluetoothDeviceList.clear();
                    bluetoothDeviceList.clear();
                    addressList.clear();

                    myBluetoothDeviceList.add(TempMyBluetoothDevice);
                    bluetoothDeviceList.add(tempBluetoothDevice);
                    addressList.add(tempAddress);
                } else {
                    myBluetoothDeviceList.clear();
                    bluetoothDeviceList.clear();
                    addressList.clear();
                }
            }
        }
    }

    // 设备名字
    public String deviceName;
    // 设备地址
    public String address;
    // 设备与我的距离
    public double distance=99999;
    // size记录当前rssi数，方便计算距离
    private int size;
    // 记录RSSI的数组
    private Queue<Short> rssiQueue=new ArrayDeque<>();

    /*
    * 额外信息：
    * */
    // 是否连接当前设备，是的话则显示详细信息
    private boolean isConnected;
    // 目标定车场的详细地址
    private String parkingAddress;
    // 剩余可用车位数
    private int available;
    // 收费情况
    private int fare;

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public MyBluetoothDevice(){
        deviceName="未知";
        address="未知";
        distance=99999;
        size=0;
    }
    public MyBluetoothDevice(String name,String address,short rssi){
        this.deviceName=name;
        this.address=address;

        distance=99999;
        size=1;
        rssiQueue.add(rssi);
        CalculateDistance();
    }

//    public void setConnectedState(boolean state){
//        isConnected=state;
//    }

    // 添加新获取的RSSI
    public void AddRssi(short rssi){
        // TODO: 2015/10/4 处理RSSI数据！
        if(size<5){
            rssiQueue.add(rssi);
            size++;
        }else{
            rssiQueue.poll();
            rssiQueue.add(rssi);
        }
        // 更新完RSSI后，计算距离
        CalculateDistance();
    }

    // 计算距离
    private void CalculateDistance(){
        double sum=0;
        for(short temprssi:rssiQueue){
            sum+=temprssi;
        }
        double rssi=sum/size;
        double n=3;
        distance=Math.pow(10,((Math.abs(rssi)-63)/(10*n)));
    }

    // ----------------------下面是Parcelable的内容----------------------------------

    protected MyBluetoothDevice(Parcel in) {
        deviceName = in.readString();
        address = in.readString();
        distance = in.readDouble();
    }


    public static final Creator<MyBluetoothDevice> CREATOR = new Creator<MyBluetoothDevice>() {
        @Override
        public MyBluetoothDevice createFromParcel(Parcel in) {
            return new MyBluetoothDevice(in);
        }
        @Override
        public MyBluetoothDevice[] newArray(int size) {
            return new MyBluetoothDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceName);
        dest.writeString(address);
        dest.writeDouble(distance);
    }
}
