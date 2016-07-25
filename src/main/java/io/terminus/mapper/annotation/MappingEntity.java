package io.terminus.mapper.annotation;

import io.terminus.mapper.code.MappingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xgs on 16/6/30.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingEntity {
    Class<?> entityClass();
    MappingType type() default MappingType.GROUP;
}
