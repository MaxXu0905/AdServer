package com.ailk.api.impl;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;

import com.ailk.api.TLSClientIfc;
import com.ailk.common.ServerProperties;
import com.ailk.security.CertificateCoder;
import com.ailk.security.DESCoder;

public class TLSClientManager implements TLSClientIfc {

	private static final Logger logger = Logger.getLogger(TLSClientManager.class);

	private final static String KEYSTORE_PATH = "server.keystore.path";
	private final static String KEYSTORE_PATH_DFLT = "/server.keystore";
	private final static String KEYSTORE_ALIAS = "www.asiainfo-linkage.com";
	private final static String KEYSTORE_PASSWORD = "123456";

	static private PublicKey publicKey = null;
	static private X509Certificate x509Certificate = null;
	static private Random random = null;

	private String desKey = null;

	static {
		Properties properties = ServerProperties.getInstance().getProperties();
		String keyStorePath = properties.getProperty(KEYSTORE_PATH, KEYSTORE_PATH_DFLT);

		try {
			x509Certificate = (X509Certificate) CertificateCoder.getCertificate(keyStorePath, KEYSTORE_ALIAS,
					KEYSTORE_PASSWORD);
			publicKey = x509Certificate.getPublicKey();
		} catch (Exception e) {
			logger.fatal("系统异常，" + e);
			System.exit(1);
		}

		random = new Random();
		random.setSeed(System.currentTimeMillis());
	}

	public TLSClientManager() {
	}

	@Override
	public byte[] generateKey() {
		byte[] key = new byte[16];
		for (int i = 0; i < 16; i++) {
			key[i] = (byte) random.nextInt();
		}

		desKey = new String(key);

		try {
			return CertificateCoder.encryptByPublicKey(desKey.getBytes(), publicKey);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			return null;
		}
	}

	@Override
	public String getSignature() {
		try {
			return new String(x509Certificate.getSignature());
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			return null;
		}
	}

	@Override
	public byte[] encrypt(byte[] data) {
		try {
			return DESCoder.encrypt(data, desKey);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			return null;
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		try {
			return DESCoder.decrypt(data, desKey);
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			return null;
		}
	}

}
