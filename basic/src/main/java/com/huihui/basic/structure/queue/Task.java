package com.huihui.basic.structure.queue;

public class Task {
    private TaskType taskType;

    public Task(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void doTask()  {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(taskType);
    }
}
