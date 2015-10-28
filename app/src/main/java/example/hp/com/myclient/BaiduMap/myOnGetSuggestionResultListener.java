package example.hp.com.myclient.BaiduMap;

import android.widget.ArrayAdapter;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;

/**
 * Created by lenovo on 2015/10/24.
 */
public class myOnGetSuggestionResultListener implements OnGetSuggestionResultListener {
    private ArrayAdapter<String> sugAdapter;
    public myOnGetSuggestionResultListener(ArrayAdapter<String> sugadapter){
        sugAdapter = sugadapter;
    }
    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null)
                sugAdapter.add(info.key);
        }
        sugAdapter.notifyDataSetChanged();
    }
}
