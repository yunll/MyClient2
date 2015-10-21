package example.hp.com.myclient.BTClient.Module;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import example.hp.com.myclient.R;


/**
 * Created by hp on 2015/10/3.
 */
public class MyBTDeviceAdapter extends ArrayAdapter<MyBluetoothDevice> {

    private int resoureceId;

    public MyBTDeviceAdapter(Context context, int resource, List<MyBluetoothDevice> objects) {
        super(context, resource, objects);
        resoureceId=resource;
    }

//    public MyBTDeviceAdapter(Context context, int resource) {
//        super(context, resource);
//        resoureceId=resource;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyBluetoothDevice myBluetoothDevice=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resoureceId, null);
        // 设备名(停车场名)
        TextView txtName=(TextView)view.findViewById(R.id.txtName);
        // 距离多远
        TextView txtDis=(TextView)view.findViewById(R.id.txtDis);

        // 连接状态
        TextView txtConnected=(TextView)view.findViewById(R.id.txtConnected);
        // 额外信息
        TextView txtExtra=(TextView)view.findViewById(R.id.txtExtra);

        // 设置输出样式
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);

        txtName.setText("设备名字：" + myBluetoothDevice.deviceName);
        txtDis.setText("距离:"+(nf.format(myBluetoothDevice.distance))+" 米");


        String stringExtra="额外信息zzzzzzzzzzzzzzzz";
        // 显示连接状态
        if(myBluetoothDevice.isConnected()){
            txtConnected.setText("已连接");
            txtExtra.setText(stringExtra);
        }else{
            LinearLayout layout = (LinearLayout)view.findViewById(R.id.bt_device_layout1);
            txtConnected.setText("未连接");
            layout.removeView(txtExtra);
        }
        // TODO: 2015/10/17  根据myBluetoothDevice的额外信息，添加文字，

        // TODO: 2015/10/17  比如connectd==false 那么就removeview
        return view;
    }
}
