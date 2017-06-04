package cn.itcast.googleplay10.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

import cn.itcast.googleplay10.http.HttpHelper;
import cn.itcast.googleplay10.utils.IOUtils;
import cn.itcast.googleplay10.utils.MD5Encoder;
import cn.itcast.googleplay10.utils.UiUtils;

/**
 * Created by zhengping on 2016/12/2,14:45.
 *
 * 为每一个模块访问网络提供一个基类
 *
 */

public abstract class BaseProtocol<T> {

    private String url;

    public T getData() {
        return getDataFromServer();
    }

    /*public T getDataFromServerByOKHttp() {

    }*/

    private T getDataFromServer() {
        //访问服务器的数据
        //url:组成   主域名+模块名称+参数
        //  http://127.0.0.1:8090/home?index=0
        url = HttpHelper.URL + getKey() + getParams();
        String cacheJson = getCache();
        if(cacheJson != null) {
            return parseJson(cacheJson);
        }
        HttpHelper.HttpResult httpResult = HttpHelper.get(url);
        if (httpResult != null) {
            String json = httpResult.getString();
            if (json != null) {
                setCache(json);
            }
            return parseJson(json);
        }
        return null;
    }

    private void setCache(String json) {
        //SharedPreference:存储的配置信息
        //Sqlite：关系型数据库，
        //文件：图片，json数据
        //   /sdcard/GooglePlay/xxxxx      /data/data/包名/cache/
        FileWriter fw = null;
        try {
            File cacheDir = UiUtils.getContext().getCacheDir();
            String fileName = MD5Encoder.encode(url);
            File cacheFile = new File(cacheDir,fileName);

            long empireTime = System.currentTimeMillis() + 5*1000*60;//3000000
            String strEmpireTime = empireTime + "";


            fw =  new FileWriter(cacheFile);

            fw.write(strEmpireTime + "\n");

            fw.write(json);
            fw.flush();

        } catch (Exception e) {

        } finally {
            IOUtils.close(fw);
        }

    }

    private String getCache() {
        BufferedReader bReader = null;
        try {
            File cacheDir = UiUtils.getContext().getCacheDir();
            String fileName = MD5Encoder.encode(url);
            File cacheFile = new File(cacheDir,fileName);

            bReader = new BufferedReader(new FileReader(cacheFile));
            String strEmpireTime = bReader.readLine();
            long empireTime = Long.parseLong(strEmpireTime);
            if(empireTime > System.currentTimeMillis()) {
                //缓存有效

                StringBuffer sb = new StringBuffer();
                String str = null;
                while ((str = bReader.readLine()) != null) {
                    sb.append(str);
                }

                return sb.toString();

            } else {
                //超时
                return null;
            }


        } catch (Exception e) {

        } finally {
            IOUtils.close(bReader);
        }

        return null;
    }


    public abstract String getKey();
    public abstract String getParams();
    public abstract T parseJson(String json) ;
}
