package io.lilpig.iocaop.framework.aop;

public interface AOPHelper {
    /**
     * 判断是否有指定类的接入点
     * @param targetClass
     * @return 是否有指定类的接入点
     */
    boolean hasPointCut(Class<?> targetClass);

    /**
     * 织入，返回织入后的对象
     * @param targetClass
     * @return 织入后的对象
     */
    Object weaving(Object orignalBean,Class<?> targetClass);
}
