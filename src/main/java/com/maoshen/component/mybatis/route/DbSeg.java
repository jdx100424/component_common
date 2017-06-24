package com.maoshen.component.mybatis.route;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DbSeg {
	/**
	 * 
	 * 根据什么字段分表
	 * 
	 * @return
	 * 
	 */
	public String shardBy();

}