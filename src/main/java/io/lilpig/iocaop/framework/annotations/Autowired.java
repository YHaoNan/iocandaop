package io.lilpig.iocaop.framework.annotations;


import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.CONSTRUCTOR,
        ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
