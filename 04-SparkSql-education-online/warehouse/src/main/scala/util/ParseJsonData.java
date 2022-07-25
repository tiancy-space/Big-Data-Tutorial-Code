package util;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用 alibaba.fastJson解析json字符串.如果解析成功则返回一个`JsonObject`对象,如果解析失败,则通过抓异常的方式处理,返回一个null.
 */
public class ParseJsonData {

    public static JSONObject getJsonData(String data) {
        try {
            return JSONObject.parseObject(data);
        } catch (Exception e) {
            return null;
        }
    }
}
