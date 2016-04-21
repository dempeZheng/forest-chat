package com.dempe.ocean.sdk.example;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/4/21
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public class ThreadTest {
    public static void main(String[] args) {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("-------");

            }
        },1L,1L, TimeUnit.SECONDS);
    }
}
