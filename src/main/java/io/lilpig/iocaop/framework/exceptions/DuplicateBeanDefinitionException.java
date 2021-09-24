package io.lilpig.iocaop.framework.exceptions;

public class DuplicateBeanDefinitionException extends BeansException{
    public DuplicateBeanDefinitionException(String beanName, String clz, String oldClz) {
        super(
                "Cannot create bean because there is already a bean named <" + beanName +"> in factory. " +
                        "Old Bean Class: <" + oldClz + "> " +
                        "New Bean Class: <" + clz + ">"
        );
    }
}
