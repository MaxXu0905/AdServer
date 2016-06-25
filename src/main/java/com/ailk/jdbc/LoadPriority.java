package com.ailk.jdbc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * �����������ȼ������ȼ�ԽС��Խ�ȱ�����
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
