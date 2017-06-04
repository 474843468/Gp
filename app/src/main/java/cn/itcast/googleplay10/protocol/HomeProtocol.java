package cn.itcast.googleplay10.protocol;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.bean.HomeData;

/**
 * Created by zhengping on 2016/12/2,14:57.
 */

public class HomeProtocol extends  BaseProtocol<ArrayList<AppInfo>> {

    private ArrayList<AppInfo> dataList = new ArrayList<AppInfo>();
    private ArrayList<String> picList = new ArrayList<String>();
    private int index;

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParams() {
        return "?index=" + index;
    }

    public ArrayList<String> getPicList() {
        return picList;
    }

    /**
     * Gson\FastJson
     * JSONObject
     * {}--JSONObject
     * []--JSONArray
     * 无符号--基本数据类型
     * @param json
     * @return
     */
    @Override
    public ArrayList<AppInfo> parseJson(String json) {

        Gson gson = new Gson();
        HomeData homeData = gson.fromJson(json, HomeData.class);
        picList = homeData.picture;
        return homeData.list;
        /*try {
            JSONObject jo = new JSONObject(json);
            JSONArray ja = jo.getJSONArray("list");
            for(int i=0;i<ja.length();i++) {
                JSONObject jo2 = ja.getJSONObject(i);
                AppInfo info = new AppInfo();
                info.des = jo2.getString("des");
                info.downloadUrl = jo2.getString("downloadUrl");
                info.iconUrl = jo2.getString("iconUrl");
                info.id = jo2.getString("id");
                info.name = jo2.getString("name");
                info.packageName = jo2.getString("packageName");
                info.size = jo2.getString("size");
                info.stars = jo2.getString("stars");
                dataList.add(info);

            }

            JSONArray ja3 = jo.getJSONArray("picture");
            for(int i=0;i<ja3.length();i++) {
                String picUrl = ja3.getString(i);
                picList.add(picUrl);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            dataList = null;
        }
        return dataList;*/
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
