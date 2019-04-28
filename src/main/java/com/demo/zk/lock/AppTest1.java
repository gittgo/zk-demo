package com.demo.zk.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: zk-demo
 * @Auther: GERRY
 * @Date: 2019/01/06 12:07
 * @Description:
 */
public class AppTest1 {
    static int n = 10;
    private static CountDownLatch latch = new CountDownLatch(10);

    public static void secondSkill() {
        System.out.println(--n);

    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0 ; i <10; i++) {
            new Thread(){
                @Override
                public void run() {
                    DistributedLock lock = null;
                    try {
                        latch.await();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        lock = new DistributedLock("192.168.100.161:2181,192.168.100.162:2181,192.168.100.163:2181", "test1");
                        lock.lock();
                        secondSkill();
                    } finally {
                        if (lock != null) {
                            lock.unlock();
                        };
                    };

                }
            }.start(); //每循环一次，就启动一个线程,具有一定的随机性

            latch.countDown();
        };
    }
}
