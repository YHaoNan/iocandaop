package io.lilpig.iocaop.framework.annotations;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
    String methodName() default "*";
}
