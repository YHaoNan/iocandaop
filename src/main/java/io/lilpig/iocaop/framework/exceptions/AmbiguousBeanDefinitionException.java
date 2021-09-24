package io.lilpig.iocaop.framework.exceptions;

public class AmbiguousBeanDefinitionException extends BeansException{
    public AmbiguousBeanDefinitionException(Class<?> parent, Class<?> classes[]){
        super(generateErrorMessage(parent,classes));
    }

    private static String generateErrorMessage(Class<?> parent, Class<?>[] classes) {
        StringBuffer sb = new StringBuffer("Need 1 <");
        sb.append(parent.getName());
        sb.append("> but found ");
        sb.append(classes.length);
        sb.append("<");
        for (Class<?> clz: classes) {
            sb.append(clz.getName());
            sb.append(" ");
        }
        sb.append(">");
        return sb.toString();
    }
}
