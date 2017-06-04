package cn.itcast.googleplay10.protocol;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zhengping on 2016/12/4,15:38.
 */

public class RecommendProtocol extends BaseProtocol<ArrayList<String>> {

    private ArrayList<String> dataList = new ArrayList<String>();
    @Override
    public String getKey() {
        return "recommend";
    }

    @Override
    public String getParams() {
        return "";//如果没有参数，不能够返回ｎｕｌｌ，应该返回空串
    }

    @Override
    public ArrayList<String> parseJson(String json) {
        try {
            JSONArray ja = new JSONArray(json);
            for(int i=0;i<ja.length();i++) {
                String keyword = ja.getString(i);
                dataList.add(keyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            dataList = null;
        }
        return dataList;
    }
}
