package example.hp.com.myclient.BaiduMap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;

/**
 * Created by lenovo on 2015/10/8.
 */
public class Sensorlistener implements SensorEventListener {
    private MyLocationData.Builder data;
    private BaiduMap mBaiduMap;
    public void setlocation(MyLocationData.Builder data_build){
        data=data_build;
    }
    public Sensorlistener(MyLocationData.Builder data_build,BaiduMap baiduMap){
        data=data_build;
        mBaiduMap=baiduMap;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    int mIncrement;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];
            mIncrement++;
            if (mIncrement == 10) {
                data.direction(x);
                mBaiduMap.setMyLocationData(data.build());
                mIncrement = 0;
            }
        }
    }
}
