package com.winphysoft.basic.structure.queue;

import java.util.concurrent.LinkedBlockingDeque;

public class TaskDeque extends LinkedBlockingDeque<Task> {
    public TaskDeque(int capacity) {
        super(capacity);
    }

    @Override
    public void put(Task task) throws InterruptedException {
        if (task.getTaskType() == TaskType.FIRST){
            super.putFirst(task);
        } else {
            super.putLast(task);
        }
    }
}
