package io.lilpig.iocaop.test.beans;

import io.lilpig.iocaop.framework.annotations.Component;

@Component
public class KillTheDragonTask implements Task{

    @Override
    public void execute(Fighter fighter) {
        System.out.println(fighter.getClass().getSimpleName() + " kill the dragon");
    }
}
