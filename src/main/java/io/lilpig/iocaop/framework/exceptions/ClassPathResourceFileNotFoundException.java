package io.lilpig.iocaop.framework.exceptions;

public class ClassPathResourceFileNotFoundException extends RuntimeException{
    public ClassPathResourceFileNotFoundException(String fileName) {
        super("Cannot find <"+fileName+"> in classpath.");
    }
}
