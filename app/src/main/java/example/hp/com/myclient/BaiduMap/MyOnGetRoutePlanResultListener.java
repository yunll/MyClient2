package example.hp.com.myclient.BaiduMap;

import android.app.Activity;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * Created by lenovo on 2015/10/23.
 */
public class MyOnGetRoutePlanResultListener implements OnGetRoutePlanResultListener {
    private BaiduMap mBaiduMap;
    private Activity activity;
    public MyOnGetRoutePlanResultListener(BaiduMap baiduMap,Activity activity2){
        activity = activity2;
        mBaiduMap = baiduMap;
    }
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        //获取步行线路规划结果
    }
    public void onGetTransitRouteResult(TransitRouteResult result) {
        //获取公交换乘路径规划结果
    }
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if(result==null||result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
            Toast.makeText(activity, "没有找到路线", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(activity,"路线规划成功",Toast.LENGTH_SHORT).show();
        if(result.error == SearchResult.ERRORNO.NO_ERROR){
            mBaiduMap.clear();
            DrivingRouteOverlay routeOverlay = new DrivingRouteOverlay(mBaiduMap);
            routeOverlay.setData(result.getRouteLines().get(0));
            routeOverlay.addToMap();
            routeOverlay.zoomToSpan();
        }
    }
}
