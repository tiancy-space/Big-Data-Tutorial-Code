package fastJsonUseDemo;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.math3.dfp.Dfp;

import java.util.Date;

/**
 * @Description: 定义一个标准的 JavaBean,并在当前类上添加JSON的注解,用来控制当前类的对象转化为字json字符串的字段名称.
 * @Author: tiancy
 * @Create: 2022/6/27
 */
public class PersonAddJSONField {

    /**
     * 使用 serialize 属性来控制当前实体类对象转化为json字符串时,是否进行序列化操作,也就是是否写到json字符串中.这里给定 false,则不写入.
     * 使用 deserialize@JSONField(name = "DATE OF BIRTH", deserialize=false)来控制 当前json字符串转化为对应的实体类对象时,是否展示.
     */
    @JSONField(name = "AGE", serialize = false)
    private int age;

    /**
     * ordinal: 通过当前注解的`ordinal`属性来控制当前字段在json字符串中出现顺序.
     */
    @JSONField(name = "LAST NAME", ordinal = 2)
    private String lastName;

    /**
     * 当前注解 @JSONField(name = "FIRST NAME"),用来标识: 当前JavaBean的对象中的这个 `firstName`属性的值,在json字符串中是 `FIRST NAME`这个名字.
     */
    @JSONField(name = "FIRST NAME", ordinal = 1)
    private String firstName;

    /**
     * format 参数用于格式化 date 属性。默认写入当前时间是毫秒值.
     * defaultPatttern : "yyyy-MM-dd HH:mm:ss";
     */
    @JSONField(name = "DATE OF BIRTH", format = "yyyy-MM-dd HH:mm:ss", ordinal = 3)
    private Date dateOfBirth;

    public PersonAddJSONField(int age, String lastName, String firstName, Date dateOfBirth) {
        this.age = age;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "PersonAddJSONField{" +
                "age=" + age +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
