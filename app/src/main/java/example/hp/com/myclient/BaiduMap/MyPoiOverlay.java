package example.hp.com.myclient.BaiduMap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;

/**
 * Created by lenovo on 2015/10/23.
 */
public class MyPoiOverlay extends PoiOverlay {
    private PoiSearch mpoiSearch;
    public MyPoiOverlay(BaiduMap baiduMap, PoiSearch poiSearch) {
        super(baiduMap);
        mpoiSearch = poiSearch;
    }
    @Override
    public boolean onPoiClick(int index) {
        super.onPoiClick(index);
        PoiInfo poi = getPoiResult().getAllPoi().get(index);
        // if (poi.hasCaterDetails) {
        mpoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                .poiUid(poi.uid));
        // }
        return true;
    }
}
