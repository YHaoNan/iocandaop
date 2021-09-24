package io.lilpig.iocaop.framework.aop.exceptions;

public class CannotCreateAspectException extends RuntimeException{
    public CannotCreateAspectException(String aspectClass) {
        super("Cannot create aspect <" + aspectClass + ">! Please make sure your aspect class has an public constructor with no parameter!");
    }
}
