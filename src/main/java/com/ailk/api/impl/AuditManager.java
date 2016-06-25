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
 * ��ƹ���������Ҫ��������Ҫ��¼������Ϊϵͳ��Ƶ�����
 * 
 * @author xugq
 * 
 */
public class AuditManager implements AuditIfc {

	private final static Logger logger = Logger.getLogger(AuditManager.class);
	public static final String AUDIT_DIR = "server.audit.dir";

	private static AuditManager instance;

	private String auditDir; // ���Ŀ¼
	private OutputStream syslogger;
	private OutputStream auditor;

	/**
	 * ��ȡ��ƹ���ʵ��
	 * 
	 * @return ��ƹ���ʵ��
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
	 * ���캯��
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
			logger.error("ϵͳ�쳣��" + e);
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
	 * д���ļ�
	 * 
	 * @param os
	 *            �����
	 * @param userId
	 *            �û�ID
	 * @param data
	 *            ����
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
				logger.error("ϵͳ�쳣��" + e);
			}
		}
	}

	/**
	 * ת�����ֽ���
	 * @param value ��������
	 * @return �ֽ���
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
	 * ת�����ֽ���
	 * @param value ������
	 * @return �ֽ���
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
	 * ת���ɳ�����
	 * @param value �ֽ���
	 * @return ������
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
	 * ת��������
	 * @param value �ֽ���
	 * @return ����
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
