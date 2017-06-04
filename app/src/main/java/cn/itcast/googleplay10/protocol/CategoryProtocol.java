package cn.itcast.googleplay10.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cn.itcast.googleplay10.bean.CategoryInfo;

/**
 * Created by zhengping on 2016/12/5,9:19.
 */

public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {

    private ArrayList<CategoryInfo> dataList = new ArrayList<>();
    @Override

    public String getKey() {
        return "category";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<CategoryInfo> parseJson(String json) {

        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);

                String title = jo.getString("title");
                CategoryInfo titleInfo = new CategoryInfo();
                titleInfo.setIsTitle(true);
                titleInfo.setTitle(title);
                dataList.add(titleInfo);

                JSONArray ja2 = jo.getJSONArray("infos");
                for (int j = 0; j < ja2.length(); j++) {
                    JSONObject jo2 = ja2.getJSONObject(j);
                    CategoryInfo categoryInfo = new CategoryInfo();
                    categoryInfo.setName1(jo2.getString("name1"));
                    categoryInfo.setName2(jo2.getString("name2"));
                    categoryInfo.setName3(jo2.getString("name3"));

                    categoryInfo.setUrl1(jo2.getString("url1"));
                    categoryInfo.setUrl2(jo2.getString("url2"));
                    categoryInfo.setUrl3(jo2.getString("url3"));
                    dataList.add(categoryInfo);

                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
            dataList = null;
        }


        return dataList;
    }
}
