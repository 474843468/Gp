package cn.itcast.googleplay10.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhengping on 2016/12/2,15:30.
 */

public class AppInfo {

    public String des;
    public String downloadUrl;
    public String iconUrl;
    public String id;
    public String name;
    public String packageName;
    public String size;
    public String stars;



    public String author;
    public String date;
    public String downloadNum;
    public ArrayList<SafeInfo> safe;
    public ArrayList<String> screen;
    public String version;
}
