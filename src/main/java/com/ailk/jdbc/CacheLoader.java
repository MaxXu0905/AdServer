package com.ailk.jdbc;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * ���������
 * 
 * @author xugq
 * 
 */
public class CacheLoader {

	private final static Logger logger = Logger.getLogger(CacheLoader.class);

	/**
	 * �����ڴ�����
	 * 
	 * @param globalManager
	 *            ȫ�ֹ������
	 */
	public static void inMemLoad(GlobalManager globalManager) {
		try {
			execute(InMemCacheIfc.class, globalManager);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * �޸ı�
	 */
	public static void alterTable() {
		try {
			Method method = AlterTableIfc.class.getMethod("alterTable");
			execute(AlterTableIfc.class, method);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * ִ���ض��ӿڵ��ض�����
	 * 
	 * @param ifc
	 *            �����ӿ�
	 * @param globalManager
	 *            ȫ�ֹ������
	 * @throws Exception
	 */
	private static void execute(Class<?> ifc, GlobalManager globalManager) throws Exception {
		TreeMap<Integer, List<Method>> methodMap = new TreeMap<Integer, List<Method>>();
		String packageName = CacheLoader.class.getPackage().getName();
		String resourceName = packageName.replace('.', '/');
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(resourceName);

		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			File file = new File(new URI(url.toString()));
			String[] names = file.list();

			for (String name : names) {
				// ֻƥ����
				if (!name.endsWith(".class"))
					continue;

				// ������Ƿ���չ�˸������ʵ���˸����ӿ�
				String className = packageName + "." + name.substring(0, name.length() - 6);
				Class<?> claz = Class.forName(className);
				if (!ifc.isAssignableFrom(claz) || ifc.equals(claz))
					continue;

				try {
					// ��ȡload(GlobalManager)��̬����
					Method method = claz.getMethod("load", GlobalManager.class);
					if (method == null)
						continue;

					int priority;
					Annotation annotation = claz.getAnnotation(LoadPriority.class);
					if (annotation == null)
						priority = LoadPriority.PRIORITY_DFLT;
					else
						priority = (Integer) annotation.annotationType().getMethod("value").invoke(annotation);

					List<Method> methods = methodMap.get(priority);
					if (methods == null) {
						methods = new ArrayList<Method>();
						methodMap.put(priority, methods);
					}
					methods.add(method);
				} catch (NoSuchMethodException e) {
					logger.error("������" + name + "ʧ�ܣ�" + e);
				} catch (Exception e) {
					logger.error("������" + name + "ʧ�ܣ�" + e);
				}
			}
		}

		for (List<Method> methods : methodMap.values()) {
			for (Method method : methods) {
				// ִ�к���
				method.invoke(null, globalManager);
			}
		}
	}

	/**
	 * ִ���ض��ӿڵ��ض�����
	 * 
	 * @param ifc
	 *            �����ӿ�
	 * @param method
	 *            ����
	 * @throws Exception
	 */
	private static void execute(Class<?> ifc, Method method) throws Exception {
		TreeMap<Integer, List<Object>> instanceMap = new TreeMap<Integer, List<Object>>();
		String packageName = CacheLoader.class.getPackage().getName();
		String resourceName = packageName.replace('.', '/');
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(resourceName);

		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			File file = new File(new URI(url.toString()));
			String[] names = file.list();

			for (String name : names) {
				// ֻƥ����
				if (!name.endsWith(".class"))
					continue;

				// ������Ƿ���չ�˸������ʵ���˸����ӿ�
				String className = packageName + "." + name.substring(0, name.length() - 6);
				Class<?> claz = Class.forName(className);
				if (!ifc.isAssignableFrom(claz) || ifc.equals(claz))
					continue;

				try {
					// ��ȡgetInstance()��̬����
					Method getInstance = claz.getMethod("getInstance");
					if (getInstance == null)
						continue;

					// ����getInstance()������ö���
					Object mgr = getInstance.invoke(null);

					int priority;
					Annotation annotation = claz.getAnnotation(LoadPriority.class);
					if (annotation == null)
						priority = LoadPriority.PRIORITY_DFLT;
					else
						priority = (Integer) annotation.annotationType().getMethod("value").invoke(annotation);

					List<Object> objs = instanceMap.get(priority);
					if (objs == null) {
						objs = new ArrayList<Object>();
						instanceMap.put(priority, objs);
					}
					objs.add(mgr);
				} catch (NoSuchMethodException e) {
					logger.error("������" + name + "ʧ�ܣ�" + e);
				} catch (Exception e) {
					logger.error("������" + name + "ʧ�ܣ�" + e);
				}
			}
		}

		for (List<Object> objs : instanceMap.values()) {
			for (Object obj : objs) {
				// ִ�к���
				method.invoke(obj);
			}
		}
	}

}
