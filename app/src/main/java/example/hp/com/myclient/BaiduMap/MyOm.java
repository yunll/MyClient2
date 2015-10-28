package example.hp.com.myclient.BaiduMap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import example.hp.com.myclient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2015/10/8.
 */
public class MyOm extends OverlayManager {
    private PoiResult result;
    private BaiduMap mbaiduMap;
    private PoiSearch mPoiSearch;

    public boolean onPolylineClick(Polyline polyline){
        PoiInfo poi = result.getAllPoi().get(polyline.getZIndex());
        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                .poiUid(poi.uid));
        return true;
    }
    public void setResult(PoiResult result) {
        this.result = result;
    }

    public MyOm(BaiduMap mBaiduMap,PoiSearch poiSearch) {
        super(mBaiduMap);
        this.mbaiduMap=mBaiduMap;
        mPoiSearch=poiSearch;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        PoiInfo poi = result.getAllPoi().get(marker.getZIndex());
        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                .poiUid(poi.uid));
        return true;
    }

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        List<OverlayOptions> ops = new ArrayList<OverlayOptions>();
        List<PoiInfo> pois = result.getAllPoi();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        for(int i = 0;i < pois.size();i++){
            OverlayOptions op = new MarkerOptions().position(pois.get(i).location).icon(bitmap);
            ops.add(op);
            mbaiduMap.addOverlay(op).setZIndex(i);
        }
        return ops;
    }
}
