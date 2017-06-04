package cn.itcast.googleplay10.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.itcast.googleplay10.bean.AppData;
import cn.itcast.googleplay10.bean.AppInfo;

/**
 * Created by zhengping on 2016/12/4,11:14.
 */

public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {

    private ArrayList<AppInfo> dataList = new ArrayList<>();
    private int index;

    @Override
    public String getKey() {
        return "app";
    }

    @Override
    public String getParams() {
        return "?index=" + index;
    }

    /**
     * 数据解析：ＪＳＯＮＯｂｊｅｃｔ＼Ｇｓｏｎ＼ＦａｓｔＪｓｏｎ
     * 选项原则：
     *  JsonObject解析的效率是最高，代码麻烦
     *
     *  一般情况下，使用Ｇｓｏｎ来进行解析，特殊情况下会使用ＪｓｏｎＯｂｊｅｃｔ
     *  １＼当数据结构相当复杂的情况，使用ＪｓｏｎＯｂｊｅｃｔ
     *  ２＼当数据结构相当简单的情况下，使用ＪｓｏｎＯｂｊｅｃｔ，比如{"success":"true"}
     *
     *  Gson的使用：
     *      {}-->类
     *      []-->ArrayList
     *      无符号：基本的数据类型
     *      当数据集合没有名称的时候，使用ＴｙｐｅＴｏｋｅｎ
     *
     * @param json
     * @return
     */
    @Override
    public ArrayList<AppInfo> parseJson(String json) {

        Gson gson = new Gson();
        TypeToken<ArrayList<AppInfo>> typeToken = new TypeToken<ArrayList<AppInfo>>(){};//创建一种类型
        return gson.fromJson(json, typeToken.getType());
        //return appData.list;

        /*try {
            JSONArray ja = new JSONArray(json);
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
