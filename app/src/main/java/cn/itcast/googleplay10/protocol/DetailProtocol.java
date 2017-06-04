package cn.itcast.googleplay10.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.itcast.googleplay10.bean.AppInfo;
import cn.itcast.googleplay10.bean.SafeInfo;

/**
 * Created by zhengping on 2016/12/7,10:02.
 */

public class DetailProtocol extends BaseProtocol<AppInfo> {
    private String packageName;

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParams() {
        return "?packageName=" + packageName;
    }

    @Override
    public AppInfo parseJson(String json) {
        try {
            JSONObject jo2 = new JSONObject(json);
            AppInfo info = new AppInfo();
            info.des = jo2.getString("des");
            info.downloadUrl = jo2.getString("downloadUrl");
            info.iconUrl = jo2.getString("iconUrl");
            info.id = jo2.getString("id");
            info.name = jo2.getString("name");
            info.packageName = jo2.getString("packageName");
            info.size = jo2.getString("size");
            info.stars = jo2.getString("stars");


            info.author = jo2.getString("author");
            info.date = jo2.getString("date");
            info.downloadNum = jo2.getString("downloadNum");
            info.version = jo2.getString("version");

            JSONArray safeJa = jo2.getJSONArray("safe");
            ArrayList<SafeInfo> safeList = new ArrayList<>();
            for(int i=0;i<safeJa.length();i++) {
                JSONObject jsonObject = safeJa.getJSONObject(i);
                SafeInfo safeInfo = new SafeInfo();
                safeInfo.safeDes = jsonObject.getString("safeDes");
                safeInfo.safeDesColor = jsonObject.getString("safeDesColor");
                safeInfo.safeDesUrl = jsonObject.getString("safeDesUrl");
                safeInfo.safeUrl = jsonObject.getString("safeUrl");
                safeList.add(safeInfo);
            }
            info.safe = safeList;


            JSONArray screenJa = jo2.getJSONArray("screen");
            ArrayList<String> scrrenList = new ArrayList<>();
            for(int i=0;i<screenJa.length();i++) {
                String screenPic = screenJa.getString(i);
                scrrenList.add(screenPic);
            }

            info.screen = scrrenList;
            return info;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
