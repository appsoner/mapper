package tk.mybatis.mapper.annotation;

import tk.mybatis.mapper.code.MappingType;
import tk.mybatis.mapper.code.SqlFunction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xuguosheng on 16/6/30.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MappingField {
    String name();
    MappingType type() default MappingType.COMMON;
    SqlFunction method() default SqlFunction.NONE;
}
