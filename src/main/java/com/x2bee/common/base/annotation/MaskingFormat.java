package com.x2bee.common.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.x2bee.common.base.util.masking.AbstractMasker;
import com.x2bee.common.base.util.masking.Masker;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MaskingFormat {
    String regexPattern() default "^(.*)";
    String replacePattern() default "$1";
    Class<? extends Masker> maskerClass() default AbstractMasker.class;
}
