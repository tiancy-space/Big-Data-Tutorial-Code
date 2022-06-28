package fastJsonUseDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 将json字符串转化为JSON对象. 或者将当前json字符串转化为 JavaBean.
 * @Author: tiancy
 * @Create: 2022/6/27
 */
public class Json02_将Json字符串转化为JSON对象_Java {

    public static void main(String[] args) {

        /*
            TODO JSON对象的创建
                创建 JSON 对象非常简单，只需使用 JSONObject（fastJson提供的json对象） 和 JSONArray（fastJson提供json数组对象） 对象即可。
                JSONObject 当成一个 Map<String,Object> 来看.
                JSONArray 当做一个 List<Object>来看.
         */
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 2; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("AGE", 10);
            jsonObject.put("FULL NAME", "Doe " + i);
            jsonObject.put("DATE OF BIRTH", "2016/12/12 12:12:12");
            jsonArray.add(jsonObject);
        }
        String jsonOutput = jsonArray.toJSONString();
        /*
            [
                {
                    "DATE OF BIRTH": "2016/12/12 12:12:12",
                    "FULL NAME": "Doe 0",
                    "AGE": 10
                },
                {
                    "DATE OF BIRTH": "2016/12/12 12:12:12",
                    "FULL NAME": "Doe 1",
                    "AGE": 10
                }
        ]
    */
        System.out.println(jsonOutput);

        // TODO 将字符串转化为 json对象.
        String personInfo = "{\"FIRST NAME\":\"刘\",\"LAST NAME\":\"华强\",\"DATE OF BIRTH\":\"2022-06-27 17:22:35\"}";
        JSONObject jsonObject = JSON.parseObject(personInfo);
        String firstName = jsonObject.getString("FIRST NAME");
        System.out.println("firstName = " + firstName); // firstName = 刘

        // TODO 将 json字符拆转化为Java对象.
        PersonAddJSONField person = JSON.parseObject(personInfo, PersonAddJSONField.class);
        // PersonAddJSONField{age=0, lastName='华强', firstName='刘', dateOfBirth=Mon Jun 27 17:22:35 CST 2022}
        System.out.println(person);


        String personInfo2 = "{\n" +
                "    \"FIRST NAME\": \"刘\",\n" +
                "    \"LAST NAME\": \"华强\",\n" +
                "    \"DATE OF BIRTH\": \"2022-06-27 17:22:35\",\n" +
                "    \"age\": \"25\"\n" +
                "}";
        PersonAddJSONField person2 = JSON.parseObject(personInfo2, PersonAddJSONField.class);
        System.out.println(person2);


        // 如果没有将当前实体类和json字符串中的key做映射,直接转转换会怎样. 如果对应不上,则返序列化的对象内,是null.
        String person3 = "{\"age1\":25,\"dateOfBirth\":1656319202701,\"fullName3\":\"ashe\"}";
        Person p3 = JSON.parseObject(person3, Person.class);
        // 如果使用当前获取的属性值,是一个null值,在调用方法会报空指针问题.
        System.out.println(p3.getFullName());
        // Person{age=0, fullName='null', dateOfBirth=null}
        System.out.println(p3);

    }


}
