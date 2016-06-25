package com.ailk.api.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ailk.api.AuditIfc;
import com.ailk.common.ServerProperties;

/**
 * 审计管理，对于重要的请求，需要记录，以作为系统审计的依据
 * 
 * @author xugq
 * 
 */
public class AuditManager implements AuditIfc {

	private final static Logger logger = Logger.getLogger(AuditManager.class);
	public static final String AUDIT_DIR = "server.audit.dir";

	private static AuditManager instance;

	private String auditDir; // 审计目录
	private OutputStream syslogger;
	private OutputStream auditor;

	/**
	 * 获取审计管理实例
	 * 
	 * @return 审计管理实例
	 */
	public static AuditManager getInstance() {
		if (instance != null)
			return instance;
		
		synchronized (AuditManager.class) {
			if (instance != null)
				return instance;
			
			instance = new AuditManager();
			return instance;
		}
	}

	/**
	 * 构造函数
	 * 
	 * @throws FileNotFoundException
	 */
	private AuditManager() {
		Properties properties = ServerProperties.getInstance().getProperties();

		auditDir = properties.getProperty(AUDIT_DIR);
		if (!auditDir.endsWith("/"))
			auditDir += "/";

		try {
			File file = new File(auditDir + "syslog");
			if (!file.exists())
				file.mkdirs();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateStr = sdf.format(new Date(System.currentTimeMillis()));

			String filename = file.getAbsolutePath() + "/" + dateStr;
			syslogger = new BufferedOutputStream(new FileOutputStream(filename, true));

			file = new File(auditDir + "audit");
			if (!file.exists())
				file.mkdirs();

			filename = file.getAbsolutePath() + "/" + dateStr;
			auditor = new BufferedOutputStream(new FileOutputStream(filename, true));
		} catch (FileNotFoundException e) {
			logger.error("系统异常，" + e);
		}
	}

	@Override
	public void syslog(long userId, String data) {
		write(syslogger, userId, data);
	}

	@Override
	public void audit(long userId, String data) {
		write(auditor, userId, data);
	}
	
	/**
	 * 写入文件
	 * 
	 * @param os
	 *            输出流
	 * @param userId
	 *            用户ID
	 * @param data
	 *            数据
	 */
	private void write(OutputStream os, long userId, String data) {
		if (os == null)
			return;

		synchronized (os) {
			try {
				os.write(toBytes(userId));
				os.write(toBytes(System.currentTimeMillis()));

				byte[] bytes = data.getBytes();
				os.write(toBytes(bytes.length));
				os.write(bytes);
				os.flush();
			} catch (Exception e) {
				logger.error("系统异常，" + e);
			}
		}
	}

	/**
	 * 转换成字节流
	 * @param value 长整形数
	 * @return 字节流
	 */
	private static byte[] toBytes(long value) {
		byte[] result = new byte[8];

		result[0] = (byte) value;
		value >>>= 8;
		result[1] = (byte) value;
		value >>>= 8;
		result[2] = (byte) value;
		value >>>= 8;
		result[3] = (byte) value;
		value >>>= 8;
		result[4] = (byte) value;
		value >>>= 8;
		result[5] = (byte) value;
		value >>>= 8;
		result[6] = (byte) value;
		value >>>= 8;
		result[7] = (byte) value;

		return result;
	}

	/**
	 * 转换成字节流
	 * @param value 整形数
	 * @return 字节流
	 */
	private static byte[] toBytes(int value) {
		byte[] result = new byte[4];

		result[0] = (byte) value;
		value >>>= 8;
		result[1] = (byte) value;
		value >>>= 8;
		result[2] = (byte) value;
		value >>>= 8;
		result[3] = (byte) value;

		return result;
	}

	/**
	 * 转换成长整形
	 * @param value 字节流
	 * @return 长整形
	 */
	public static long parseLong(byte[] bytes) {
		long result = bytes[7];

		result <<= 8;
		result |= bytes[6];
		result <<= 8;
		result |= bytes[5];
		result <<= 8;
		result |= bytes[4];
		result <<= 8;
		result |= bytes[3];
		result <<= 8;
		result |= bytes[2];
		result <<= 8;
		result |= bytes[1];
		result <<= 8;
		result |= bytes[0];

		return result;
	}

	/**
	 * 转换成整形
	 * @param value 字节流
	 * @return 整形
	 */
	public static int parseInt(byte[] bytes) {
		int result = bytes[3];

		result <<= 8;
		result |= bytes[2];
		result <<= 8;
		result |= bytes[1];
		result <<= 8;
		result |= bytes[0];

		return result;
	}

}
