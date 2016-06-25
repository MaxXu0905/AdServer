package com.ailk.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * ��������Թ�����������ļ����ȴ�PROPERTIES_FILE��ȡ����δ���Դ·����ȡ
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
	 * ��ȡ���Թ������ʵ��
	 * 
	 * @return ���Թ������
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
	 * ���캯��
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
	 * ��ȡ�����б�
	 * 
	 * @return �����б�
	 */
	public Properties getProperties() {
		return properties;
	}

}
