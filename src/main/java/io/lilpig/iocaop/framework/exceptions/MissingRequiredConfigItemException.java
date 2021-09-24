package io.lilpig.iocaop.framework.exceptions;

public class MissingRequiredConfigItemException extends RuntimeException{
    public MissingRequiredConfigItemException(String itemname, String configSource) {
        super("Missing required config item <"+itemname+"> in <"+configSource+">");
    }
}
