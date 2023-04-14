package com.margo.iPopUp.domain.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnifiedCache {

    long expire() default 1 * 60 * 1000;
    //缓存标识 key
    String name() default "";

}
