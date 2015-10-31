package example.hp.com.myclient.Tools;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hp on 2015/9/23.
 */
public class BluetoothTools {
    private static BluetoothAdapter bluetoothAdapter;

    public static BluetoothAdapter getBTAdapter(){
        if(bluetoothAdapter==null){
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }

    // 记录可用地址列表
    public static List<String> ParkingDeviceAddressList=new ArrayList<>();


    private static BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();


    public static final int MESSAGE_TYPE_DETIALS=1;
    public static final int MESSAGE_TYPE_CHECKIN=2;
    public static final int MESSAGE_TYPE_CHECKOUT=3;

    public static final String REMOTE_DEVICE_NAME ="REMOTE_DEVICE_NAME";
//    public static final UUID PRIVATE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final UUID PRIVATE_UUID = UUID.fromString("0f3561b9-bda5-4672-84ff-ab1f98e349b6");
    public static final String MY_BLUETOOTH_DEVICE="MY_BLUETOOTH_DEVICE";
    public static final String REMOTE_DEVICE_ADDRESS="REMOTE_DEVICE_ADDRESS";

    public static final String DEVICE = "DEVICE";
    public static final String RSSI="RSSI";
    public static final String DISTANCE="DISTANCE";
    public static final String SERVER_INDEX = "SERVER_INDEX";
    public static final String DATA = "DATA";
    public static final String ACTION_READ_DATA = "ACTION_READ_DATA";
    public static final String ACTION_NOT_FOUND_SERVER = "ACTION_NOT_FOUND_DEVICE";

    public static final String ACTION_START_DISCOVERY = "ACTION_START_DISCOVERY";
    public static final String ACTION_FOUND_DEVICE = "ACTION_FOUND_DEVICE";

    public static final String ACTION_SELECTED_DEVICE = "ACTION_SELECTED_DEVICE";
    public static final String ACTION_START_SERVER = "ACTION_STARRT_SERVER";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String ACTION_DATA_TO_SERVICE = "ACTION_DATA_TO_SERVICE";

    public static final String ACTION_MESSAGE_TO_ACTIVITY = "ACTION_MESSAGE_TO_ACTIVITY";

    public static final String ACTION_CONNECT_SUCCESS = "ACTION_CONNECT_SUCCESS";

    public static final String ACTION_CONNECT_ERROR = "ACTION_CONNECT_ERROR";
    public static final int MESSAGE_CONNECT_SUCCESS = 0x00000002;
    public static final int MESSAGE_CONNECT_ERROR = 0x00000003;
    public static final int MESSAGE_RECEIVE_MESSAGE = 0x00000004;

    public static void openBluetooth() {
        adapter.enable();
    }
    public static void closeBluetooth() {
        adapter.disable();
    }
    public static void openDiscovery(int duration) {
        if (duration <= 0 || duration > 300) {
            duration = 200;
        }
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
    }
    public static void stopDiscovery() {
        adapter.cancelDiscovery();
    }

}

