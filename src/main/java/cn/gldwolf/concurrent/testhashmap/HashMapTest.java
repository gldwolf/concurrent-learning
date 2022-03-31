package cn.gldwolf.concurrent.testhashmap;

import java.util.HashMap;
import java.util.Hashtable;

public class HashMapTest {
    public static void main(String[] args) {
        final HashMap<String, String> map = new HashMap<>();
        // 返回之前该 key 对应的 value
        String before = map.put("Hello", "world");
        System.out.println(before);
        before = map.put("Hello", "World");
        System.out.println(before);
        final Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("Hello", "World!");
    }
}
