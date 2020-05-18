package org.roc.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author roc
 * @since 2020/4/30 14:46
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    //初始内存容量
    private int capacity;

    public LRULinkedHashMap(int initialCapacity) {
        super(16, 0.75f, true);
        this.capacity = capacity;
    }

    public static void main(String[] args) throws Exception {
        Scanner cin = new Scanner(System.in);

        System.out.println("请输入总共内存页面数： ");
        int n = cin.nextInt();
        Map<Integer, Integer> map = new LRULinkedHashMap<Integer, Integer>(n);

        System.out.println("请输入按顺序输入要访问内存的总共页面数： ");
        int y = cin.nextInt();

        System.out.println("请输入按顺序输入访问内存的页面序列： ");
        for (int i = 1; i <= y; i++) {
            int x = cin.nextInt();
            map.put(x, x);
        }
        System.out.println("此时内存中包含的页面数是有:");
        //用for-each语句，遍历此时内存中的页面并输出
        for (java.util.Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //，每加入一个元素，就判断是size是否超过了已定的容量
        System.out.println("此时的size大小=" + size());
        if ((size() > capacity)) {
            System.out.println("超出已定的内存容量，把链表顶端元素移除：" + eldest.getValue());
        }
        return size() > capacity;
    }
}