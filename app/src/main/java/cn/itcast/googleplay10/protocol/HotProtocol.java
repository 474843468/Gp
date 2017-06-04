package cn.itcast.googleplay10.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by zhengping on 2016/12/5,10:23.
 */

public class HotProtocol extends BaseProtocol<ArrayList<String>> {
    @Override
    public String getKey() {
        return "hot";
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public ArrayList<String> parseJson(String json) {

        Gson gson = new Gson();
        TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
        ArrayList<String> dataList = gson.fromJson(json,typeToken.getType());

        return dataList;
    }
}
