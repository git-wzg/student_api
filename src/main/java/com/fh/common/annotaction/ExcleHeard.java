package com.fh.common.annotaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*作用在类上*/
@Target(ElementType.TYPE)
/*项目启动时就有效*/
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcleHeard {
        String title() default "";
}
