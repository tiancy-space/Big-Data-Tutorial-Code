package fastJsonUseDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 将Java对象转化为Json字符串.
 * @Author: tiancy
 * @Create: 2022/6/27
 */
public class Json01_将Java对象转Json字符串_Java {

    public static void main(String[] args) {

        // TODO 使用 JSON.toJSONString() 将 Java 对象转换换为 JSON 字符串：

        List<Person> listOfPersons = new ArrayList<Person>();
        /**1、将一个 集合对象 转化为 数组json字符串. */
        listOfPersons.add(new Person(25, "ashe", new Date()));
        listOfPersons.add(new Person(20, "Janette Doe", new Date()));
        /*
            [{"age":25,"dateOfBirth":1656319202701,"fullName":"ashe"},{"age":20,"dateOfBirth":1656319202702,"fullName":"Janette Doe"}]
         */
        String jsonString1 = JSON.toJSONString(listOfPersons);
        System.out.println("jsonString1 = " + jsonString1);

        /*
            SerializerFeature.BeanToArray: 通过当前方式,将对象转化为数组表示.
            jsonString2 = [[25,1656322138503,"ashe"],[20,1656322138503,"Janette Doe"]]
         */
        String jsonString2 = JSON.toJSONString(listOfPersons, SerializerFeature.BeanToArray);
        System.out.println("jsonString2 = " + jsonString2);


        /**我们还可以自定义输出，并控制字段的排序，日期显示格式，序列化标记等。例如新定义一个JavaBean,在当前类的属性上使用json的注解.*/

        PersonAddJSONField p1 = new PersonAddJSONField(25, "华强", "刘", new Date());
        String p1Str = JSON.toJSONString(p1);
        /*
            忽略当前对象中的某个属性,可以直接使用  @JSONField(name = "AGE", serialize = false) 上述注解的方式控制.
            p1Str = {"FIRST NAME":"刘","LAST NAME":"华强","DATE OF BIRTH":"2022-06-27 17:22:35"}
         */
        System.out.println("p1Str = " + p1Str);

    }
}
