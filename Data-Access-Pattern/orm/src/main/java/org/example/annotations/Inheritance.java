package org.example.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Inheritance
{
    String strategy() default "SINGLE_TABLE"; // table_per_class
}