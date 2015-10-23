package example.hp.com.myclient.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import example.hp.com.myclient.BTClient.Module.MyBTDeviceAdapter;
import example.hp.com.myclient.BTClient.Module.MyBluetoothDevice;
import example.hp.com.myclient.PullToRefresh.RefreshableView;
import example.hp.com.myclient.R;
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

//region #开始扫描的接口
    private onBeginScanningListener beginScanningListener;

    public interface onBeginScanningListener{
        // Mainactivity实现onBeginScanningListener接口中的onBeginScanning方法
        // fragment中直接调用beginScanningListener.onBeginScanning()即可
        void onBeginScanning();
    }

    /**
     * 检查activity中是否已经实现了onBeginScanningListener接口
     * @param activity
     */
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            beginScanningListener =(onBeginScanningListener)activity;
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
                // TODO: 2015/10/22 更改释放时的动画，太生硬了
                try {
                    beginScanningListener.onBeginScanning();
                    // 这个sleep是表示显示搜索键5s
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
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
                // TODO: 2015/10/22 点击设备，开始连接
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
