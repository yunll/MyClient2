package example.hp.com.myclient.BaiduMap;

import android.text.Editable;
import android.text.TextWatcher;

import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

/**
 * Created by lenovo on 2015/10/8.
 */
public class Text_watch implements TextWatcher {

    private SuggestionSearch mSuggestionSearch;
    private String city;
    public Text_watch(String str,SuggestionSearch suggestionSearch){
        city=str;
        mSuggestionSearch=suggestionSearch;
    }
    @Override
    public void afterTextChanged(Editable arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1,
                                  int arg2, int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence cs, int arg1, int arg2,
                              int arg3) {
        if (cs.length() <= 0) {
            return;
        }
        String city = this.city;
        /**
         */
        mSuggestionSearch
                .requestSuggestion((new SuggestionSearchOption())
                        .keyword(cs.toString()).city(city));
    }
}