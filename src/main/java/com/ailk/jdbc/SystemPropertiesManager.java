package com.ailk.jdbc;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.ailk.common.GlobalVariables;
import com.ailk.jdbc.entity.SystemProperties;

/**
 * 系统参数对象
 * 
 * @author xugq
 * 
 */
public class SystemPropertiesManager implements InMemCacheIfc, AlterTableIfc {

	private static final Logger logger = Logger.getLogger(SystemPropertiesManager.class);

	public static final String ANDROID_VERSION = "android_version";
	public static final String ANDROID_VERSION_NAME = "android_version_name";
	public static final String ANDROID_INFO = "android_info";

	private int androidVersion;
	private String androidVersionName;
	private String androidInfo;

	/**
	 * 获取系统参数表的实例
	 * 
	 * @return 操作系统参数表的实例
	 */
	public static SystemPropertiesManager getInstance(GlobalManager globalManager) {
		return globalManager.getSystemPropertiesManager();
	}

	public int getAndroidVersion() {
		return androidVersion;
	}

	public String getAndroidVersionName() {
		return androidVersionName;
	}

	public String getAndroidInfo() {
		return androidInfo;
	}

	/**
	 * 构造函数
	 * 
	 * @param globalManager
	 *             全局管理对象
	 */
	private SystemPropertiesManager(GlobalManager globalManager) {
	}

	/**
	 * 加载数据到缓存
	 */
	@Override
	public void load() {
		int count = 0;

		synchronized (SystemPropertiesManager.class) {
			// 加载数据
			int partition = 0;
			Session session = HibernateUtil.getSessionFactory(partition).openSession();
			Iterator<?> iter = session.createQuery("FROM SystemProperties").iterate();

			while (iter.hasNext()) {
				SystemProperties entity = (SystemProperties) iter.next();
				String propertyName = entity.getPropertyName();
				String propertyValue = entity.getPropertyValue();

				if (propertyName.equalsIgnoreCase(ANDROID_VERSION))
					androidVersion = Integer.parseInt(propertyValue);
				else if (propertyName.equalsIgnoreCase(ANDROID_VERSION_NAME))
					androidVersionName = propertyValue;
				else if (propertyName.equalsIgnoreCase(ANDROID_INFO))
					androidInfo = propertyValue;

				count++;
			}

			session.close();
		}

		// 设置版本号
		GlobalVariables.getAndroidproperties().setVersion(androidVersion);

		logger.info("系统参数表加载完毕，共" + count + "条记录");
	}

	/**
	 * 加载数据到缓存
	 * 
	 * @param globalManager
	 *            全局管理对象
	 */
	public static void load(GlobalManager globalManager) {
		SystemPropertiesManager systemPropertiesManager = new SystemPropertiesManager(globalManager);

		systemPropertiesManager.load();
		globalManager.setSystemPropertiesManager(systemPropertiesManager);
	}

	/**
	 * 修改表结构
	 */
	@Override
	public void alterTable() {
		logger.info("成功修改表：system_properties");
	}

}
