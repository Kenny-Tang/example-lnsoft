package org.example.concurrent;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteListTest {
    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(1);
        copyOnWriteArrayList.add(2);
        copyOnWriteArrayList.add(3);
        copyOnWriteArrayList.add(4);
        copyOnWriteArrayList.add(5);
        copyOnWriteArrayList.add(6);
        new Thread(()->{copyOnWriteArrayList.add(8000);}).start();
        for (Object object : copyOnWriteArrayList) {
            System.out.println(object);
            Thread.sleep(100);
        }
        new Thread(() -> copyOnWriteArrayList.add(1888)).start();
        Iterator iterator = copyOnWriteArrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            Thread.sleep(100);
        }
    }
}
