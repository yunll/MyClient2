package example.hp.com.myclient.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import example.hp.com.myclient.BTClient.Module.MyBTDeviceAdapter;
import example.hp.com.myclient.BTClient.Module.MyBluetoothDevice;
import example.hp.com.myclient.R;
import example.hp.com.myclient.Tools.BluetoothTools;
import example.hp.com.myclient.Tools.MyApplication;


/**
 * Created by hp on 2015/10/21.
 */
public class BluetoothFragment extends Fragment {


    private Button btntest;

    private ListView btDeviceList;



    // 用于显示listview的adapter
    private MyBTDeviceAdapter myBTDeviceAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        btntest=(Button)view.findViewById(R.id.btn_test);
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btDeviceList =(ListView)view.findViewById(R.id.btDeviceList);
        myBTDeviceAdapter=new MyBTDeviceAdapter(
                MyApplication.getContext(),R.layout.bt_device,
                MyBluetoothDevice.myBluetoothDeviceList);
        btDeviceList.setAdapter(myBTDeviceAdapter);

        return view;
    }
//  region # Public Function
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
