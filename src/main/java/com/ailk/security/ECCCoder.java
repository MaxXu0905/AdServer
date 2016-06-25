package com.ailk.security;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;

import sun.security.ec.ECKeyFactory;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

/**
 * ECC��ȫ�������
 * 
 * @author 
 * @version 1.0
 * @since 1.0
 */
public abstract class ECCCoder extends Coder {

	public static final String ALGORITHM = "EC";
	private static final String PUBLIC_KEY = "ECCPublicKey";
	private static final String PRIVATE_KEY = "ECCPrivateKey";

	/**
	 * ����<br>
	 * ��˽Կ����
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		// ����Կ����
		byte[] keyBytes = decryptBASE64(key);

		// ȡ��˽Կ
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = ECKeyFactory.INSTANCE;

		ECPrivateKey priKey = (ECPrivateKey) keyFactory
				.generatePrivate(pkcs8KeySpec);

		ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),
				priKey.getParams());

		// �����ݽ���
		// TODO Chipher��֧��EC�㷨 δ��ʵ��
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

		return cipher.doFinal(data);
	}

	/**
	 * ����<br>
	 * �ù�Կ����
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String privateKey)
			throws Exception {
		// �Թ�Կ����
		byte[] keyBytes = decryptBASE64(privateKey);

		// ȡ�ù�Կ
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = ECKeyFactory.INSTANCE;

		ECPublicKey pubKey = (ECPublicKey) keyFactory
				.generatePublic(x509KeySpec);

		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
				pubKey.getParams());

		// �����ݼ���
		// TODO Chipher��֧��EC�㷨 δ��ʵ��
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());

		return cipher.doFinal(data);
	}

	/**
	 * ȡ��˽Կ
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);

		return encryptBASE64(key.getEncoded());
	}

	/**
	 * ȡ�ù�Կ
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);

		return encryptBASE64(key.getEncoded());
	}

	/**
	 * ��ʼ����Կ
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		BigInteger x1 = new BigInteger(
				"2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
		BigInteger x2 = new BigInteger(
				"289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);

		ECPoint g = new ECPoint(x1, x2);

		// the order of generator
		BigInteger n = new BigInteger(
				"5846006549323611672814741753598448348329118574063", 10);
		// the cofactor
		int h = 2;
		int m = 163;
		int[] ks = { 7, 6, 3 };
		ECFieldF2m ecField = new ECFieldF2m(m, ks);
		// y^2+xy=x^3+x^2+1
		BigInteger a = new BigInteger("1", 2);
		BigInteger b = new BigInteger("1", 2);

		EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);

		ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
				n, h);
		// ��Կ
		ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);

		BigInteger s = new BigInteger(
				"1234006549323611672814741753598448348329118574063", 10);
		// ˽Կ
		ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);

		Map<String, Object> keyMap = new HashMap<String, Object>(2);

		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);

		return keyMap;
	}

}