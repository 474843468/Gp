package cn.itcast.googleplay10.widget;

/**
 * Created by zhengping on 2016/12/1,16:46.
 */

public class Person {

    public static Person p1 = new Person(1);
    public static Person p2 = new Person(2);
    public static Person p3 = new Person(3);

    public int age;

    public Person(int age) {
        this.age = age;
    }
}
