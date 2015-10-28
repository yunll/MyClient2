package example.hp.com.myclient.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.util.ArrayList;
import java.util.List;

import example.hp.com.myclient.BaiduMap.BNDemoGuideActivity;
import example.hp.com.myclient.BaiduMap.MyOnGetRoutePlanResultListener;
import example.hp.com.myclient.BaiduMap.MyPoiOverlay;
import example.hp.com.myclient.BaiduMap.Sensorlistener;
import example.hp.com.myclient.BaiduMap.Text_watch;
import example.hp.com.myclient.BaiduMap.init_key;
import example.hp.com.myclient.BaiduMap.myOnGetSuggestionResultListener;
import example.hp.com.myclient.R;
import example.hp.com.myclient.Tools.MyApplication;


/**
 * Created by hp on 2015/10/21.
 */
public class MapFragment extends Fragment implements OnGetPoiSearchResultListener{

        //定位功能
        MyLocationData locData = null;
        MyLocationData.Builder data_build = new MyLocationData.Builder();
        private MapView mapView;
        private BaiduMap mBaiduMap;
        private boolean isFirstLocate = true;
        public MyLocationListenner myListener = new MyLocationListenner();
        private BDLocation location1=null;
        private LocationClient mLocClient;
        private BitmapDescriptor mCurrentMarker;//定位图标
        private MyLocationConfiguration.LocationMode mCurrentMode
                = MyLocationConfiguration.LocationMode.NORMAL;//显示定位图模式
        //方向传感器申明
        private Sensorlistener sensorlistener;
        private SensorManager sm = null;
        private boolean first = true;


        //检索功能
        private PoiSearch mPoiSearch = null;
        private SuggestionSearch mSuggestionSearch = null;
        /**
         * 搜索关键字输入窗口
         */
        private AutoCompleteTextView keyWorldsView = null;
        private ArrayAdapter<String> sugAdapter = null;
        private int load_Index = 0;


        public static final String ROUTE_PLAN_NODE = "routePlanNode";
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        Button daohang = null;

        RoutePlanSearch msearch;
        LatLng elatlng = null;
        OnGetRoutePlanResultListener listener;
        EditText editcity;
        EditText editSearchKey;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
            SDKInitializer.initialize(getActivity().getApplicationContext());
            View view = inflater.inflate(R.layout.fragment_map,container,false);
            daohang = (Button)view.findViewById(R.id.daohang);
            editcity = (EditText) view.findViewById(R.id.city);
            editSearchKey = (EditText) view.findViewById(R.id.searchkey);
            mapView = (MapView) view.findViewById(R.id.map_view);
            mBaiduMap = mapView.getMap();
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, mCurrentMarker));
            // 定位初始化
            mLocClient = new LocationClient(getActivity());
            sensorlistener = new Sensorlistener(data_build,mBaiduMap);
            sm = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
            sm.registerListener(sensorlistener, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
            mLocClient.registerLocationListener(myListener);
            mLocClient.requestLocation();
            initlocation();
            ((Button) view.findViewById(R.id.request)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mLocClient.requestLocation();
                    isFirstLocate = true;
                }
            });

            //检索
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(this);
            /**
             * 当输入关键字变化时，动态更新建议列表
             */
            sugAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line);
            mSuggestionSearch = SuggestionSearch.newInstance();
            mSuggestionSearch.setOnGetSuggestionResultListener(new myOnGetSuggestionResultListener(sugAdapter));
            keyWorldsView = (AutoCompleteTextView) view.findViewById(R.id.searchkey);
            keyWorldsView.setAdapter(sugAdapter);
            String city = ((EditText) view.findViewById(R.id.city)).getText()
                    .toString();
            keyWorldsView.addTextChangedListener(new Text_watch(city,mSuggestionSearch));
            listener = new MyOnGetRoutePlanResultListener(mBaiduMap,getActivity());

            view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchButtonProcess();
                }
            });
            view.findViewById(R.id.daohang).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daohang();
                }
            });
            view.findViewById(R.id.guihua).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guihua();
                }
            });
            new init_key((Activity)getActivity());
            return view;
        }

    public void initlocation(){
        //去掉百度图标
        int count = mapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ImageView || child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }
        //定位属性设置
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mLocClient.requestLocation();
    }
    @Override
    public void onStart(){
        mLocClient.requestLocation();
        super.onStart();
    }
    @Override
    public void onDestroy(){
        if (BaiduNaviManager.isNaviInited())
            BaiduNaviManager.getInstance().uninit();
        super.onDestroy();
        sm.unregisterListener(sensorlistener);
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null)
                return;
            location1 = location;
            data_build.accuracy(location1.getRadius())
                    .latitude(location1.getLatitude())
                    .longitude(location1.getLongitude());
            sensorlistener.setlocation(data_build);
            if (isFirstLocate) {
                isFirstLocate = false;
                editcity.setText("附近");
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
    }


    public void searchButtonProcess() {
        mBaiduMap.clear();
        LatLng latLng = new LatLng(location1.getLatitude(),location1.getLongitude());
        Toast.makeText(MyApplication.getContext(), latLng.longitude + "::" + latLng.latitude, Toast.LENGTH_SHORT).show();
        if(editcity.getText().toString().equals("附近")){
            Toast.makeText(MyApplication.getContext(),"dsdsd",Toast.LENGTH_SHORT).show();
            mPoiSearch.searchNearby((new PoiNearbySearchOption()).location(latLng)
                    .keyword(editSearchKey.getText().toString())
                    .pageNum(0)
                    .sortType(PoiSortType.distance_from_near_to_far)
                    .radius(100000)
                    .pageCapacity(10));
        }else{
            Toast.makeText(MyApplication.getContext(),editcity.getText().toString(),Toast.LENGTH_SHORT).show();
            mPoiSearch.searchInCity(new PoiCitySearchOption()
                    .city(editcity.getText().toString().trim())
                    .keyword(editSearchKey.getText().toString()));
        }

    }

    public void guihua() {
        if(elatlng==null||location1==null) return;
        msearch = RoutePlanSearch.newInstance();
        mBaiduMap.clear();
        msearch.setOnGetRoutePlanResultListener(listener);
        LatLng latLng = new LatLng(location1.getLatitude(),location1.getLongitude());
        msearch.drivingSearch((new DrivingRoutePlanOption())
                .from(PlanNode.withLocation(latLng))
                .to(PlanNode.withLocation(elatlng))
                .policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM));
        mPoiSearch.searchNearby((new PoiNearbySearchOption()).location(elatlng)
                .keyword("停车场")
                .pageNum(0)
                .sortType(PoiSortType.distance_from_near_to_far)
                .radius(10000)
                .pageCapacity(10));
    }

    public void daohang(){
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, true, new DemoRoutePlanListener(sNode));

        }
    }
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MyApplication.getContext(), "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap,mPoiSearch);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MyApplication.getContext(), strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MyApplication.getContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(MyApplication.getContext(), result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
            LatLng location = result.getLocation();
            BDLocation end = new BDLocation();
            end.setLatitude(location.latitude);
            end.setLongitude(location.longitude);
            BDLocation end1 = LocationClient.getBDLocationInCoorType(end, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
            BDLocation start = LocationClient.getBDLocationInCoorType(location1, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
            //Toast.makeText(MainActivity.this,qstr+"::"+qlat+"::"+qlong,Toast.LENGTH_SHORT).show();
            sNode = new BNRoutePlanNode(start.getLongitude(),start.getLatitude(),
                    start.getAddrStr().toString(), null, BNRoutePlanNode.CoordinateType.GCJ02);
            eNode = new BNRoutePlanNode(end1.getLongitude(),end1.getLatitude(),
                    result.getAddress(), null, BNRoutePlanNode.CoordinateType.GCJ02);
            elatlng = result.getLocation();
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public DemoRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent = new Intent(MyApplication.getContext(), BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        @Override
        public void onRoutePlanFailed() {
        }
    }
}