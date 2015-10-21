package example.hp.com.myclient.BTClient.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import example.hp.com.myclient.Tools.BluetoothTools;


/**
 * Created by hp on 2015/9/25.
 */
public class ClientConnectThread extends Thread {

    private Handler handler;
    private BluetoothDevice device;
    private BluetoothSocket socket;


    public ClientConnectThread(Handler handler,BluetoothDevice device){
        this.handler=handler;
        this.device=device;
    }

    @Override
    public void run() {
        // 连接之前，先取消搜索
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            // TODO: 2015/9/22 UUID??
            // 首先通过UUID，获取一个socket
            // TODO: 2015/9/22  这个socket可能是作为客户端的蓝牙设备的socket？？
            socket = device.createRfcommSocketToServiceRecord(BluetoothTools.PRIVATE_UUID);

            // TODO: 2015/9/22   可以直接连接？
            socket.connect();
        } catch (Exception ex) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
            return;
        }
        // 告诉handler已经连接成功
        Message msg = handler.obtainMessage();
        msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
        msg.obj = socket;
        // 这个msg来自handler.obtainMessage(), 所以他直接发送给这个handler
        msg.sendToTarget();
    }
}
