package io.lilpig.iocaop.test.beans;

import io.lilpig.iocaop.framework.annotations.Autowired;
import io.lilpig.iocaop.framework.annotations.Component;

@Component
public class BraveFighter implements Fighter{

    @Autowired
    private Task task;

//    public void setTask(Task task) {
//        this.task = task;
//    }

    @Override
    public void explore() {
        System.out.println("BraveFighter will execute task...");
        task.execute(this);
        System.out.println("BraveFighter complete the mission...");
    }
}
