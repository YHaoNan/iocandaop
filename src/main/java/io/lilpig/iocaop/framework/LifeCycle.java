package io.lilpig.iocaop.framework;

import java.lang.reflect.Field;
import java.util.Map;

public interface LifeCycle {
    /**
     * 执行bean扫描前回调
     * @param factory 当前bean工厂（其实LifeCycle不应该和Bean工厂耦合，应该再抽象出一层Context啥的）
     */
    void beforeBeanScan(BeanFactory factory);

    /**
     * 执行bean扫描后回调
     * @param factory 当前Bean工厂
     */
    void afterBeanScan(BeanFactory factory);

    /**
     * 每次扫描到一个Bean但还没设置到factory中时调用
     * @param beanEntry key代表当前bean的名字，value代表即将设置到factory中的bean
     * @return 该方法主要提供一个机会，给即将设置到工厂中的Bean做一些包装或者其他工作，返回的对象会代替原对象进入工厂
     *          如果不希望对Bean进行进一步的处理，可以返回原对象或者返回null
     */
    Object onBeanScaning(Map.Entry<String, Object> beanEntry);

    void beforeInjection(BeanFactory factory);

    void afterInjection(BeanFactory factory);

    Object afterOnceInjecting(Object bean, Field injectedField, Object appropriateBean);

    void onInjecting(Field injectingField, Object appropriateBean);
}
