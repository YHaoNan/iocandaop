package io.lilpig.iocaop;

import io.lilpig.iocaop.test.beans.BraveFighter;
import io.lilpig.iocaop.test.beans.KillTheDragonTask;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;

public class CGLibTest {
    @Test
    public void testProxy() {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(BraveFighter.class);
//        enhancer.setCallback(new MethodInterceptor() {
//            @Override
//            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                System.out.println("我先来一个花手给你摇迷糊辣！");
//                Object result = methodProxy.invokeSuper(o, objects);
//                System.out.println("然后我直接一个 好友跟 直接把你给干死！");
//                return result;
//            }
//        });
//        BraveFighter fighter = (BraveFighter) enhancer.create();
//        fighter.setTask(new KillTheDragonTask());
//        fighter.explore();
    }
}
