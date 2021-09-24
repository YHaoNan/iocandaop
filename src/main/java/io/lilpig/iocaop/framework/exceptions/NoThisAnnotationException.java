package io.lilpig.iocaop.framework.exceptions;

public class NoThisAnnotationException extends Exception{
    public NoThisAnnotationException(String sourceClz, String annotationClz) {
        super("There is no <@" + annotationClz + "> on class: <" + sourceClz + ">");
    }
}
