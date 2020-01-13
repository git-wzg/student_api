package com.fh.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //此注解用在属性上
@Retention(RetentionPolicy.RUNTIME)//一直有效
public @interface Excel {


    String name();

    String value() default "za";


}
