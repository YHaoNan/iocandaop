package io.lilpig.iocaop.test.beans;

import io.lilpig.iocaop.framework.annotations.Autowired;
import io.lilpig.iocaop.framework.annotations.Component;

@Component
public class BraveFighter implements Fighter{

    @Autowired
    private Task task;

    @Override
    public void explore() {
        task.execute(this);
    }
}
