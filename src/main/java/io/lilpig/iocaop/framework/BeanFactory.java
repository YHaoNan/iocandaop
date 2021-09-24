package io.lilpig.iocaop.framework;

import io.lilpig.iocaop.framework.exceptions.BeansException;
import io.lilpig.iocaop.framework.exceptions.NoSuchBeanDefinitionException;

import java.util.Map;

/**
 * 管理Bean
 * 一个BeanFactory要在构造方法中读取所有Bean，并满足其依赖关系，读取的方式由子类自行定义
 */
public interface BeanFactory extends LifeCycle{
    /**
     * BeanFactory进行初始化，在这个阶段中，BeanFactory需要扫描Bean。
     */
    void init();

    /**
     * 根据Bean名获取Bean
     * @param name
     * @return 获取到的Bean
     * @throws BeansException 创建Bean的过程中出现了一些错误，如Bean未定义，Bean是一个接口等
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据Bean名和类型获取一个Bean
     * @param name
     * @param requiredType
     * @param <T>
     * @return 获取到的Bean
     * @throws BeansException
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 根据类型获取一个Bean
     * @param requiredType
     * @param <T>
     * @return 获取到的Bean
     * @throws BeansException
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    /**
     * Bean工厂中是否包含Bean定义
     * @param name Bean名
     * @return
     */
    boolean containsBean(String name);

    /**
     * Bean是否是单例
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isSingleTon(String name) throws NoSuchBeanDefinitionException;

    /**
     * Bean是否是原型
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    /**
     * 指定名字的Bean是否具有特定类型
     * @param name
     * @param targetType
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;

    /**
     * 获取Bean的类型
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    Map<String, Object> getBeans();
}
