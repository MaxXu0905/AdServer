package com.ailk.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 加载数据优先级，优先级越小，越先被加载
 * @author xugq
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadPriority {
	
	public static final int PRIORITY_MIN = 0;
	public static final int PRIORITY_DFLT = 50;
	public static final int PRIORITY_MAX = 100;
	
	public int value() default PRIORITY_DFLT;
	
}
