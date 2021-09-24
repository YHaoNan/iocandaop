package io.lilpig.iocaop.framework.exceptions;

public class BeansException extends RuntimeException {
    public BeansException(){

    }
    public BeansException(String s) {
        super(s);
    }
    public BeansException(String s, Throwable throwable) {
        super(s,throwable);
    }
}
