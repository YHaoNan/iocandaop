package io.lilpig.iocaop.framework.exceptions;

public class CannotCreateBeanException extends BeansException{
    public CannotCreateBeanException(String beanName, String beanClz, Throwable throwable) {
        super(
                "Cannot create bean <" + beanName + "> clz: <" + beanClz + ">. Maybe your bean doesn't has a constructor with empty parameter.",
                throwable
        );
    }
}
