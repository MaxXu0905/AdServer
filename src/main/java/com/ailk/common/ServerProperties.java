package com.ailk.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 服务端属性管理对象，配置文件优先从PROPERTIES_FILE获取，其次从资源路径获取
 * 
 * @author xugq
 * 
 */
public class ServerProperties {

	private static final Logger logger = Logger.getLogger(ServerProperties.class);

	private static ServerProperties instance = null;
	private static String PROPERTIES_FILE = "/server.properties";

	private Properties properties = null;

	/**
	 * 获取属性管理对象实例
	 * 
	 * @return 属性管理对象
	 */
	public static ServerProperties getInstance() {
		if (instance != null)
			return instance;

		synchronized (ServerProperties.class) {
			if (instance == null)
				instance = new ServerProperties();

			return instance;
		}
	}

	/**
	 * 构造函数
	 */
	private ServerProperties() {
		try {
			properties = new Properties();
			File configFile = new File(PROPERTIES_FILE);
			InputStream in = null;

			if (configFile.exists())
				in = new FileInputStream(configFile);
			else
				in = getClass().getResourceAsStream(PROPERTIES_FILE);

			if (in != null) {
				try {
					properties.load(in);
				} finally {
					in.close();
				}
			}
		} catch (IOException e) {
			logger.fatal(e);
			System.exit(1);
		}
	}

	/**
	 * 获取属性列表
	 * 
	 * @return 属性列表
	 */
	public Properties getProperties() {
		return properties;
	}

}
