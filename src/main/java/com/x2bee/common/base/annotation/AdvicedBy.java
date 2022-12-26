package com.x2bee.common.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정 클래스가 AOP Advice 클래스에 의해 advicing되는 경우 JAVADOC에서 표시하기 위해 사용하는 TAG Annotation
 * 일반적으로 사용되는 Controller/Service/DAO 레벨 외의 Spring Bean에 대해 Advice가 걸리는 경우 사용한다.
 * 
 * @author Administrator
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdvicedBy {
    Class<?> adviceClass();
    String pointcut();
}