package io.lilpig.iocaop.framework.aop.exceptions;

public class NoPointCutInThisClassException extends RuntimeException {
    public NoPointCutInThisClassException(String className) {
        super("There's no point cut definition in class <"+className+">.");
    }
}
