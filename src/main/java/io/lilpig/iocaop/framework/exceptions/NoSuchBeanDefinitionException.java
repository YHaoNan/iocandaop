package io.lilpig.iocaop.framework.exceptions;

public class NoSuchBeanDefinitionException extends BeansException {
    public NoSuchBeanDefinitionException(Class<?> clz) {
        super("There's no bean has type <"+clz.getName()+"> in current factory.");
    }
    public NoSuchBeanDefinitionException(String beanName) {
        super("There's no bean named <"+beanName+"> in current factory.");
    }
    public NoSuchBeanDefinitionException(String beanName, Class<?> clz) {
        super("There's no bean named <"+beanName+"> has type <" + clz.getName() + "> in current factory.");
    }
}
