package com.hit.java;

public class MyTask implements Runnable {
    private String parameter;

    public MyTask(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void run() {
        // 在这里使用 parameter 参数
    }
}

