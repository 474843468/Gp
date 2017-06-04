package cn.itcast.googleplay10.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cn.itcast.googleplay10.bean.SubjectInfo;

/**
 * Created by zhengping on 2016/12/4,14:06.
 */

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {

    private int index;

    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public String getParams() {
        return "?index=" + index;
    }

    @Override
    public ArrayList<SubjectInfo> parseJson(String json) {
        Gson gson = new Gson();
        TypeToken<ArrayList<SubjectInfo>> typeToken = new TypeToken<ArrayList<SubjectInfo>>(){};
        ArrayList<SubjectInfo> datas = gson.fromJson(json, typeToken.getType());
        return datas;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
