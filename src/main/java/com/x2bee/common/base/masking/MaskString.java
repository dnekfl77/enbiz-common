package com.x2bee.common.base.masking;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Desensitization field
*/
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskString {
	/**
	 * Type of desensitization
	 * @return {@link String}
	 */
	MaskingType type() default MaskingType.DEFAULT;

}