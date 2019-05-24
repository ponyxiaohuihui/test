package com.winphysoft.test.queue;

public class DequeueTest {
    public static void main(String[] args) {
        TaskDeque queue = new TaskDeque(100);
        for (int i = 0; i < 100; i++){
            try {
                queue.put(new Task(TaskType.LAST));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new Thread(){
            public void run(){
                while (true){
                    Task task = null;
                    try {
                        task = queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    task.doTask();
                }
            }
        }.start();

        new Thread(){
            public void run(){
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double d = Math.random();
                    TaskType taskType;
                    if (d > -1){
                        taskType = TaskType.FIRST;
                    } else {
                        taskType = TaskType.LAST;
                    }
                    try {
                        queue.put(new Task(taskType));
                        System.out.println("put" + taskType);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
