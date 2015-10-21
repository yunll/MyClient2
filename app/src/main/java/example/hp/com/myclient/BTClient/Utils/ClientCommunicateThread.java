package example.hp.com.myclient.BTClient.Utils;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import example.hp.com.myclient.Tools.BluetoothTools;


/**
 * Created by hp on 2015/9/25.
 */
public class ClientCommunicateThread extends Thread {

    private Handler handler;
    private BluetoothSocket bluetoothSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private volatile boolean isRun=true;


    public ClientCommunicateThread(Handler handler,BluetoothSocket socket){
        this.handler=handler;
        this.bluetoothSocket=socket;
        try{
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

//            this.inputStream = new ObjectInputStream(new BufferedInputStream(bluetoothSocket.getInputStream()));
//            this.outputStream = new ObjectOutputStream(bluetoothSocket.getOutputStream());
        }
        catch(Exception e) {
            try{
                socket.close();
            }
            catch (IOException ee){
                ee.printStackTrace();
            }
            handler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 无限循环，但是只接收消息，不负责发送
        while (isRun) {
            // Log.d("CommunicatThread_isRun", "runrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrunrun");
            try {
                // FIXME: 2015/9/24 读取数据出错？？？？
                Object obj = inputStream.readObject();
                Message msg = handler.obtainMessage();
                // 读取内容
                msg.what = BluetoothTools.MESSAGE_RECEIVE_MESSAGE;
                msg.obj = obj;
                msg.sendToTarget();
            } catch (Exception ex) {
                handler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
                ex.printStackTrace();
                return;
            }
        }

        // 当不运行时，关闭IO流以及socket
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeObject(Object obj) {
        try {
            outputStream.flush();
            // TODO: 2015/9/23  write之后就可以发送了？
            outputStream.writeObject(obj);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
