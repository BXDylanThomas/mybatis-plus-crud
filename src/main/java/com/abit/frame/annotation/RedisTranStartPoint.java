package com.abit.frame.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记事务的起点,用于解决统一事务中对同一对象进行插入查询更新删除的操作后, 缓存查询不一致的问题
 * 需要在最外层的事务方法上打上该标签
 * @author SRX
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisTranStartPoint {

}
