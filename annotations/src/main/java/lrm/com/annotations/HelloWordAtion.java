package lrm.com.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建日期：2019/6/11
 * 作者:baiyang
 * hello test
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface HelloWordAtion {
    String value();
}
