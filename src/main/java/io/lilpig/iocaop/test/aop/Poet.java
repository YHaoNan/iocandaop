package io.lilpig.iocaop.test.aop;

import io.lilpig.iocaop.framework.annotations.*;

@Aspect(
        beanClasses = "io.lilpig.iocaop.test.beans.BraveFighter"
)
public class Poet {

    @Before(methodName = "explore")
    public void beforeExplore() {
        System.out.println("咱们先展示一个花手给他摇迷糊辣！");
    }

    @AfterReturning(methodName = "explore")
    public void afterExploreReturning() {
        System.out.println("然后我直接一个，耗油跟，直接把你给干死！");
    }

    @AfterThrowing(methodName = "explore")
    public void afterThrowing() {
        System.out.println("哎呀没摇迷糊Q^Q");
    }

}
