package io.lilpig.iocaop.framework.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    String[] beanClasses() default {};
}
