package com.x2bee.common.base.encrypt;

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
public @interface Encrypt {
	/**
	 * Type of desensitization
	 * @return {@link String}
	 */
	EncryptType type() default EncryptType.DEFAULT;

}