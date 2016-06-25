package com.ailk.api.impl;

import java.net.URL;
import java.security.PrivateKey;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ailk.api.TLSServerIfc;
import com.ailk.common.ServerProperties;
import com.ailk.security.CertificateCoder;
import com.ailk.security.DESCoder;

public class TLSServerManager implements TLSServerIfc {

	private static final Logger logger = Logger.getLogger(TLSServerManager.class);

	private final static String KEYSTORE_PATH = "server.keystore.path";
	private final static String KEYSTORE_PATH_DFLT = "/server.keystore";
	private final static String KEYSTORE_ALIAS = "www.asiainfo-linkage.com";
	private final static String KEYSTORE_PASSWORD = "123456";
	private final static String CHARSET = "UTF-8";

	static private PrivateKey privateKey = null;

	private String desKey;

	static {
		Properties properties = ServerProperties.getInstance().getProperties();
		String keyStorePath = properties.getProperty(KEYSTORE_PATH, KEYSTORE_PATH_DFLT);

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(keyStorePath);
			String path = url.getPath();
			privateKey = CertificateCoder.getPrivateKey(path, KEYSTORE_ALIAS, KEYSTORE_PASSWORD);
		} catch (Exception e) {
			logger.fatal("系统异常，" + e);
			System.exit(1);
		}
	}

	public TLSServerManager() {
	}

	@Override
	public HandshakeResponse handshake(HandshakeRequest request) {
		HandshakeResponse response = new HandshakeResponse();

		try {
			byte[] bytes = DESCoder.decryptBASE64(request.getData());
			byte[] decryptedBytes = CertificateCoder.decryptByPrivateKey(bytes, privateKey);
			desKey = new String(decryptedBytes, CHARSET);

			return response;
		} catch (Exception e) {
			logger.error("系统异常，" + e);
			response.setErrorCode(BaseResponse.ESYSTEM);
			return response;
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
