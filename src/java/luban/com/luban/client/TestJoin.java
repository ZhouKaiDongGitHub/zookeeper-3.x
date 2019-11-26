package com.luban.client;

public class TestJoin {
    public static void main(String[] args) {

        int count = 1000;
        Task t1 = new Task(count, null);
        t1.setName("t1");
        Task t2 = new Task(count, t1);
        t2.setName("t2");
        t2.start();
        t1.start();
    }

    public static class Task extends Thread {
        private final int count;

        private Task task;

        public Task(int count, Task task) {
            this.count = count;
            this.task = task;
        }

        @Override
        public void run() {
            if (task != null) {
                try {
                    task.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < count; i++) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                Thread.yield();
            }
        }
    }
}
